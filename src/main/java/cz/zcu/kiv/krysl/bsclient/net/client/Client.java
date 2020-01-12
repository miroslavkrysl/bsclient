package cz.zcu.kiv.krysl.bsclient.net.client;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.DisconnectedException;
import cz.zcu.kiv.krysl.bsclient.net.client.responses.Response;
import cz.zcu.kiv.krysl.bsclient.net.client.responses.ResponseDisconnected;
import cz.zcu.kiv.krysl.bsclient.net.client.responses.ResponseMessage;
import cz.zcu.kiv.krysl.bsclient.net.client.responses.ResponseOffline;
import cz.zcu.kiv.krysl.bsclient.net.client.results.ShootResult;
import cz.zcu.kiv.krysl.bsclient.net.client.results.ShootResultHit;
import cz.zcu.kiv.krysl.bsclient.net.client.results.ShootResultMiss;
import cz.zcu.kiv.krysl.bsclient.net.client.results.ShootResultSunk;
import cz.zcu.kiv.krysl.bsclient.net.codec.Deserializer;
import cz.zcu.kiv.krysl.bsclient.net.codec.Serializer;
import cz.zcu.kiv.krysl.bsclient.net.connection.Connection;
import cz.zcu.kiv.krysl.bsclient.net.messages.client.*;
import cz.zcu.kiv.krysl.bsclient.net.messages.server.*;
import cz.zcu.kiv.krysl.bsclient.net.types.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class Client implements BattleshipsClient {

    private static final Duration TIMEOUT_ALIVE = Duration.ofSeconds(10);
    private static final Duration TIMEOUT_RECEIVE = TIMEOUT_ALIVE.dividedBy(2);

    private final InetSocketAddress serverAddress;
    private final SessionKey sessionKey;

    private Connection<ServerMessage, ClientMessage> connection;

    private final ReentrantLock requestLock;
    private BlockingQueue<Response> responseBox;

    private final AtomicBoolean offline;
    private final AtomicBoolean disconnected;

    private final AtomicReference<OfflineCause> offlineCause;

    private Thread receiverThread;

    public Client(InetSocketAddress serverAddress, Nickname nickname) throws ConnectException {
        this.serverAddress = serverAddress;

        this.requestLock = new ReentrantLock();
        this.responseBox = new SynchronousQueue<>();

        this.offline = new AtomicBoolean(false);
        this.disconnected = new AtomicBoolean(false);

        this.offlineCause = new AtomicReference<>(null);


        // setup connection
        try {
            // create connection
            this.connection = new Connection<>(serverAddress, new Deserializer(), new Serializer(), TIMEOUT_RECEIVE);
            this.receiverThread = new Thread(this::runReceiving);
            this.receiverThread.start();
        } catch (IOException e) {
            throw new ConnectException("Can't connect to the server: " + e.getMessage());
        }

        // login
        ServerMessage response;

        try {
            response = request(new CMessageLogin(nickname));
        } catch (OfflineException e) {
            throw new ConnectException("Connection closed too early: " + e.getMessage());
        } catch (DisconnectedException e) {
            throw new ConnectException("Server disconnected too early: " + e.getMessage());
        } catch (InvalidStateException e) {
            // should not happen, server must be dumb
            connection.close();
            throw new ConnectException("Server is responding incorrectly.");
        }

        switch (response.getKind()) {
            case LOGIN_OK:
                SMessageLoginOk m = (SMessageLoginOk) response;
                this.sessionKey = m.getSessionKey();
                break;
            case LOGIN_FAIL:
                this.connection.close();
                throw new ConnectException("Server is full.");
            default:
                // unexpected response
                this.connection.close();
                throw new ConnectException("Server is responding incorrectly.");
        }
    }

    private void runReceiving() {
        Instant lastReceived = Instant.now();

        while (!offline.get() && !disconnected.get()) {
            try {
                ServerMessage message = connection.receive();

                if (message == null) {
                    // receive timeout happened

                    // measure elapsed time
                    Instant now = Instant.now();
                    Duration elapsed = Duration.between(lastReceived, now);

                    if (elapsed.compareTo(TIMEOUT_ALIVE) >= 0) {
                        // alive timeout happened
                        offlineCause.set(OfflineCause.UNAVAILABLE);
                        offline.set(true);
                        if (!requestLock.tryLock()) {
                            // response expected
                            responseBox.offer(new ResponseOffline());
                        } else {
                            requestLock.unlock();
                        }
                        break;
                    }

                    if (requestLock.tryLock()) {
                        // no response expected -> send alive request
                        new Thread(this::aliveRequest).start();

                        requestLock.unlock();
                    }

                    continue;
                }

                lastReceived = Instant.now();

                if (message.isResponse()) {
                    if (requestLock.tryLock()) {
                        // response not expected
                        offlineCause.set(OfflineCause.INVALID_MESSAGE);
                        offline.set(true);
                        requestLock.unlock();
                        break;
                    }

                    // hand response to waiting request
                    responseBox.offer(new ResponseMessage(message));
                    continue;
                }

                // TODO: handle all server notifications
                switch (message.getKind()) {
                    case DISCONNECT:
                        disconnected.set(true);
                        if (!requestLock.tryLock()) {
                            // response expected
                            responseBox.offer(new ResponseDisconnected());
                        } else {
                            requestLock.unlock();
                        }
                        break;
                    case OPPONENT_JOINED:
                        break;
                    case OPPONENT_READY:
                        break;
                    case OPPONENT_LEFT:
                        break;
                    case OPPONENT_MISSED:
                        break;
                    case OPPONENT_HIT:
                        break;
                    case GAME_OVER:
                        break;
                }
            } catch (DisconnectedException e) {
                // stream closed
                offlineCause.set(OfflineCause.CLOSED);
                offline.set(true);

                if (!requestLock.tryLock()) {
                    // response expected
                    responseBox.offer(new ResponseOffline());
                    break;
                } else {
                    requestLock.unlock();
                }
            } catch (DeserializeException e) {
                // stream corrupted
                offlineCause.set(OfflineCause.CORRUPTED);
                offline.set(true);

                if (!requestLock.tryLock()) {
                    // response expected
                    responseBox.offer(new ResponseOffline());
                    break;
                } else {
                    requestLock.unlock();
                }
            }
        }

        connection.close();
    }

    private void aliveRequest() {
        try {
            ServerMessage response = request(new CMessageAlive());
            if (response.getKind() != ServerMessageKind.ALIVE_OK) {
                throw handleInvalidMessage();
            }
        } catch (DisconnectedException | OfflineException | InvalidStateException e) {
            // don't care for this errors
        }
    }

    private OfflineException handleInvalidMessage() {
        this.connection.close();
        this.offlineCause.set(OfflineCause.INVALID_MESSAGE);
        this.offline.set(true);

        return new OfflineException(this.offlineCause.get());
    }

    private void checkOnline() throws DisconnectedException, OfflineException {
        if (disconnected.get()) {
            throw new DisconnectedException("Can't perform a request on disconnected client.");
        }
        if (offline.get()) {
            throw new OfflineException(offlineCause.get());
        }
    }

    private ServerMessage request(ClientMessage request) throws DisconnectedException, OfflineException, InvalidStateException {
        requestLock.lock();

        try {
            checkOnline();
        } catch (Exception e) {
            requestLock.unlock();
            throw e;
        }

        // send request
        connection.send(request);

        // get response
        Response response;
        while (true) {
            try {
                response = responseBox.take();
                break;
            } catch (InterruptedException e) {
                // interrupted
            }
        }

        if (response.isOffline()) {
            // no response because client went offline
            requestLock.unlock();
            throw new OfflineException(offlineCause.get());
        }

        if (response.isDisconnected()) {
            // no response because the server disconnected
            requestLock.unlock();
            throw new DisconnectedException("Server disconnected.");
        }

        ServerMessage message = ((ResponseMessage) response).getResponse();

        if (message.getKind() == ServerMessageKind.ILLEGAL_STATE) {
            requestLock.unlock();
            throw new InvalidStateException("Action is illegal in the current client state.");
        }

        requestLock.unlock();
        return message;
    }

    @Override
    public RestoreState restore() throws AlreadyOnlineException, ConnectException, DisconnectedException, OfflineException {
        if (disconnected.get()) {
            throw new DisconnectedException("Can't restore disconnected client.");
        }

        if (!offline.get()) {
            throw new AlreadyOnlineException("Client is already online.");
        }

        // setup connection
        try {
            // create connection
            connection = new Connection<>(serverAddress, new Deserializer(), new Serializer(), TIMEOUT_RECEIVE);
            offline.set(false);
            receiverThread = new Thread(this::runReceiving);
            receiverThread.start();
        } catch (IOException e) {
            throw new ConnectException("Can't connect to the server: " + e.getMessage());
        }

        // restore session
        ServerMessage response;

        try {
            response = request(new CMessageRestoreSession(sessionKey));
        } catch (OfflineException e) {
            throw new ConnectException("Connection closed too early: " + e.getMessage());
        } catch (DisconnectedException e) {
            throw new ConnectException("Server disconnected too early: " + e.getMessage());
        } catch (InvalidStateException e) {
            // should not happen, server must be dumb
            throw handleInvalidMessage();
        }

        switch (response.getKind()) {
            case RESTORE_SESSION_OK:
                SMessageRestoreSessionOk m = (SMessageRestoreSessionOk) response;
                return m.getState();
            case RESTORE_SESSION_FAIL:
                disconnected.set(true);
                connection.close();
                throw new DisconnectedException("Session is expired.");
            default:
                // unexpected response
                offlineCause.set(OfflineCause.INVALID_MESSAGE);
                connection.close();
                throw new OfflineException(offlineCause.get());
        }
    }

    @Override
    public boolean joinGame() throws DisconnectedException, OfflineException, InvalidStateException {
        ServerMessage response = request(new CMessageJoinGame());

        switch (response.getKind()) {
            case JOIN_GAME_OK:
                return true;
            case JOIN_GAME_WAIT:
                return false;
            default:
                throw handleInvalidMessage();
        }
    }

    @Override
    public boolean chooseLayout(Layout layout) throws DisconnectedException, OfflineException, InvalidStateException {
        ServerMessage response = request(new CMessageLayout(layout));

        switch (response.getKind()) {
            case LAYOUT_OK:
                return true;
            case LAYOUT_FAIL:
                return false;
            default:
                throw handleInvalidMessage();
        }
    }

    @Override
    public ShootResult shoot(Position position) throws DisconnectedException, OfflineException, InvalidStateException {
        ServerMessage response = request(new CMessageShoot(position));

        switch (response.getKind()) {
            case SHOOT_HIT:
                return new ShootResultHit();
            case SHOOT_MISS:
                return new ShootResultMiss();
            case SHOOT_SUNK:
                SMessageShootSunk r = (SMessageShootSunk) response;
                return new ShootResultSunk(r.getPlacement());
            default:
                throw handleInvalidMessage();
        }
    }

    @Override
    public void leaveGame() throws DisconnectedException, OfflineException, InvalidStateException {
        ServerMessage response = request(new CMessageLeaveGame());

        if (response.getKind() != ServerMessageKind.LEAVE_GAME_OK) {
            throw handleInvalidMessage();
        }
    }

    @Override
    public void disconnect() throws DisconnectedException, OfflineException {
        try {
            ServerMessage response = request(new CMessageDisconnect());

            if (response.getKind() != ServerMessageKind.DISCONNECT_OK) {
                throw handleInvalidMessage();
            }

            disconnected.set(true);
            connection.close();
        } catch (InvalidStateException e) {
            // should not happen, server must be dumb
            throw handleInvalidMessage();
        }
    }

    @Override
    public void close() {
        disconnected.set(true);
        connection.close();
    }
}
