package cz.zcu.kiv.krysl.bsclient.net.client;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.DisconnectedException;
import cz.zcu.kiv.krysl.bsclient.net.connection.Connection;
import cz.zcu.kiv.krysl.bsclient.net.messages.client.CMessageAlive;
import cz.zcu.kiv.krysl.bsclient.net.messages.client.ClientMessage;
import cz.zcu.kiv.krysl.bsclient.net.messages.server.ServerMessageKind;
import cz.zcu.kiv.krysl.bsclient.net.types.Nickname;
import cz.zcu.kiv.krysl.bsclient.net.messages.server.ServerMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client {
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private final InetSocketAddress serverAddress;
    private final Nickname nickname;
    private final ExecutorService aliveRequestExecutor;

    private BlockingQueue<IncomingMessageQueueItem> incomingQueue;
    private Lock requestLock;
    private AtomicBoolean disconnected;
    private AtomicBoolean offline;
    private Connection<ServerMessage, ClientMessage> connection;
    private ConnectionLossCause offlineCause;

    public Client(InetSocketAddress serverAddress, Nickname nickname) {
        this.serverAddress = serverAddress;
        this.nickname = nickname;

        this.requestLock = new ReentrantLock();
        this.offline = new AtomicBoolean(true);
        this.disconnected = new AtomicBoolean(true);
        this.incomingQueue = new SynchronousQueue<>();
        this.aliveRequestExecutor = Executors.newFixedThreadPool(1, runnable -> new Thread("MessageSender"));
    }

    /**
     * Send the request to the server and receive a response.
     *
     * @param request Request to send.
     * @return A received response.
     * @throws DisconnectedException If the client is disconnected before or during the call.
     * @throws OfflineException If the underlying connection is lost during the call.
     * @throws IllegalStateException If the request is illegal in the current connection state, or the state was changed during the call.
     * @throws InterruptedException If the call is interrupted.
     */
    private ServerMessage request(ClientMessage request) throws DisconnectedException, OfflineException, IllegalStateException, InterruptedException {
        requestLock.lock();

        if (isDisconnected()) {
            throw new DisconnectedException("Can't do a request on disconnected client.");
        }

        if (isOffline()) {
            throw new OfflineException("Can't do a request on offline client.", getOfflineCause());
        }

        // send request
        connection.send(request);

        // get response
        IncomingMessageQueueItem item = incomingQueue.take();

        if (item.isOffline()) {
            // no response, but client is offline
            OfflineItem offline = (OfflineItem) item;
            throw new OfflineException("Can't execute action because the client went offline.", getOfflineCause());
        }

        if (item.isDisconnected()) {
            // no response, because the client is disconnected
            DisconnectedItem offline = (DisconnectedItem) item;
            throw new DisconnectedException("Can't execute action because the connection was disconnected during the call.");
        }

        ServerMessage response = ((ResponseItem) item).getResponse();

        if (response.getKind() == ServerMessageKind.ILLEGAL_STATE) {
            throw new IllegalStateException("Action is illegal in the current client state.");
        }

        requestLock.unlock();
        return response;
    }

    /**
     * Check if the client is offline.
     *
     * @return True if offline, false otherwise.
     */
    private boolean isOffline() {
        return offline.get();
    }

    /**
     * Get the offline state cause.
     *
     * @return The offline state cause or null if not offline.
     */
    private ConnectionLossCause getOfflineCause() {
        return offlineCause;
    }

    /**
     * Check if the client is disconnected.
     *
     * @return True if disconnected, false otherwise.
     */
    private boolean isDisconnected() {
        return disconnected.get();
    }
}