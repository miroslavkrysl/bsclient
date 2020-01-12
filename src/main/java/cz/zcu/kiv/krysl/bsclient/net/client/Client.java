package cz.zcu.kiv.krysl.bsclient.net.client;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.DisconnectedException;
import cz.zcu.kiv.krysl.bsclient.net.codec.Deserializer;
import cz.zcu.kiv.krysl.bsclient.net.codec.Serializer;
import cz.zcu.kiv.krysl.bsclient.net.connection.Connection;
import cz.zcu.kiv.krysl.bsclient.net.messages.client.CMessageAlive;
import cz.zcu.kiv.krysl.bsclient.net.messages.client.CMessageLogin;
import cz.zcu.kiv.krysl.bsclient.net.messages.client.ClientMessage;
import cz.zcu.kiv.krysl.bsclient.net.messages.server.ServerMessageKind;
import cz.zcu.kiv.krysl.bsclient.net.results.ConnectResult;
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
    private static final Duration TIMEOUT_ALIVE = Duration.ofSeconds(10);
    private static final Duration TIMEOUT_REQUEST = Duration.ofSeconds(5);

    private final InetSocketAddress serverAddress;
    private final Nickname nickname;
    private final ExecutorService aliveRequestExecutor;
    private final ExecutorService receiverExecutor;

    private final BlockingQueue<IncomingMessageQueueItem> responseQueue;
    private final Lock requestLock;
    private final Lock stateLock;
    private boolean disconnected;
    private boolean offline;
    private ConnectionLossCause offlineCause;
    private Connection<ServerMessage, ClientMessage> connection;

    /**
     * Create a client, that can be connected to the server.
     *
     * @param serverAddress The address of the server which the client will connect to.
     * @param nickname      The nickname which will be used to login on the server.
     */
    public Client(InetSocketAddress serverAddress, Nickname nickname) {
        this.serverAddress = serverAddress;
        this.nickname = nickname;

        this.requestLock = new ReentrantLock();

        this.stateLock = new ReentrantLock();
        this.offline = false;
        this.offlineCause = null;
        this.disconnected = true;

        this.responseQueue = new SynchronousQueue<>();
        this.aliveRequestExecutor = Executors.newFixedThreadPool(1);
        this.receiverExecutor = Executors.newFixedThreadPool(1);
    }

    /**
     * Send the request to the server and receive a response.
     *
     * @param request Request to send.
     * @return A received response.
     * @throws DisconnectedException If the client is disconnected before or during the call.
     * @throws OfflineException      If the underlying connection is lost during the call.
     * @throws IllegalStateException If the request is illegal in the current connection state, or the state was changed during the call.
     * @throws InterruptedException  If the call is interrupted.
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
        IncomingMessageQueueItem item = responseQueue.take();

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
     * Check if the client is disconnected.
     *
     * @return True if disconnected, false otherwise.
     */
    public boolean isDisconnected() {
        stateLock.lock();
        boolean isDisconnected = disconnected;
        stateLock.unlock();
        return isDisconnected;
    }

    /**
     * Check if the client is offline.
     *
     * @return True if offline, false otherwise.
     */
    public boolean isOffline() {
        stateLock.lock();
        boolean isOffline = offline;
        stateLock.unlock();
        return isOffline;
    }

    /**
     * Get the offline state cause.
     *
     * @return The offline state cause or null if not offline.
     */
    public ConnectionLossCause getOfflineCause() {
        stateLock.lock();
        ConnectionLossCause cause = offlineCause;
        stateLock.unlock();
        return cause;
    }

    /**
     * Switch this client to the disconnected state.
     */
    private void goDisconnected() {
        stateLock.lock();
        offline = false;
        offlineCause = null;
        disconnected = true;
        stateLock.unlock();
    }

    /**
     * Switch this client to the offline state.
     *
     * @param cause The cause of the connection loss.
     */
    private void goOffline(ConnectionLossCause cause) {
        stateLock.lock();
        offline = true;
        offlineCause = cause;
        disconnected = false;
        stateLock.unlock();
    }

    /**
     * Switch this client to the offline state.
     */
    private void goOnline() {
        stateLock.lock();
        offline = false;
        offlineCause = null;
        disconnected = false;
        stateLock.unlock();
    }

    private void runReceiving() {
        Instant lastReceived = Instant.now();

        while (!isOffline() && !isDisconnected()) {
            try {
                ServerMessage message = connection.receive();

                if (message == null) {
                    // receive timeout happened

                    Instant now = Instant.now();
                    Duration elapsed = Duration.between(lastReceived, now);

                    if (elapsed.compareTo(TIMEOUT_ALIVE) >= 0) {
                        // alive timeout happened

                        if (responseQueue.offer(new OfflineItem())) {
                            // response expected but server not responding, probably network error
                            goOffline(ConnectionLossCause.UNAVAILABLE);
                            break;
                        } else {
                            // no response expected
                            // send alive message
                            aliveRequestExecutor.submit(() -> {
                                try {
                                    ServerMessage response = request(new CMessageAlive());
                                    if (response.getKind() != ServerMessageKind.ALIVE_OK) {
                                        goOffline(ConnectionLossCause.CORRUPTED);
                                    }
                                } catch (DisconnectedException | OfflineException | IllegalStateException | InterruptedException e) {
                                    // don't care for errors
                                }
                            });
                        }
                    }
                    continue;
                }

                lastReceived = Instant.now();

                if (message.isResponse()) {
                    if (!responseQueue.offer(new ResponseItem(message))) {
                        // response not expected
                        goOffline(ConnectionLossCause.CORRUPTED);
                        break;
                    }
                    continue;
                }

                // TODO: handle all server notifications
//                switch (message.getKind()) {
//                    case DISCONNECT:
//                        break;
//                    case OPPONENT_JOINED:
//                        break;
//                    case OPPONENT_READY:
//                        break;
//                    case OPPONENT_LEFT:
//                        break;
//                    case OPPONENT_MISSED:
//                        break;
//                    case OPPONENT_HIT:
//                        break;
//                    case GAME_OVER:
//                        break;
//                }

                System.out.println("server notification: " + message.getClass().getName());

            } catch (DisconnectedException e) {
                // stream closed
                goOffline(ConnectionLossCause.CLOSED);
                break;
            } catch (DeserializeException e) {
                // stream corrupted
                goOffline(ConnectionLossCause.CORRUPTED);
                break;
            }
        }

        connection.close();
    }

    /**
     * Connect to the server.
     * Internally it creates a connection requests a login.
     *
     * @return A result of connecting to the server.
     * @throws AlreadyConnectedException If the client is already connected to the server.
     */
    synchronized public ConnectResult connect() throws AlreadyConnectedException, InterruptedException, ClientConnectException {
        if (!isDisconnected()) {
            throw new AlreadyConnectedException("Can't connect an already connected client.");
        }

        try {
            connection = new Connection<>(serverAddress, new Deserializer(), new Serializer(), TIMEOUT_REQUEST);
            goOnline();
            receiverExecutor.submit(this::runReceiving);

            ServerMessage response = request(new CMessageLogin(nickname));

            switch (response.getKind()) {
                case LOGIN_OK:
                    return new ConnectResult();
                case LOGIN_FAIL:
                    return new ConnectResult();
                default:
                    // unexpected response
                    goDisconnected();
                    throw new ClientConnectException("Can't connect to the server.");
            }
        } catch (OfflineException | IllegalStateException | DisconnectedException e) {
            goDisconnected();
            throw new ClientConnectException("Can't connect to the server.");
        } catch (IOException e) {
            goDisconnected();
            throw new ClientConnectException("Can't connect to the server: " + e.getMessage());
        }
    }
}