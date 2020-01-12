package cz.zcu.kiv.krysl.bsclient.net.client;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.DisconnectedException;
import cz.zcu.kiv.krysl.bsclient.net.client.responses.ResponseDisconnected;
import cz.zcu.kiv.krysl.bsclient.net.client.responses.ResponseMessage;
import cz.zcu.kiv.krysl.bsclient.net.client.responses.ResponseOffline;
import cz.zcu.kiv.krysl.bsclient.net.client.results.ShootResult;
import cz.zcu.kiv.krysl.bsclient.net.codec.Deserializer;
import cz.zcu.kiv.krysl.bsclient.net.codec.Serializer;
import cz.zcu.kiv.krysl.bsclient.net.connection.Connection;
import cz.zcu.kiv.krysl.bsclient.net.messages.client.CMessageAlive;
import cz.zcu.kiv.krysl.bsclient.net.messages.client.CMessageLogin;
import cz.zcu.kiv.krysl.bsclient.net.messages.client.CMessageRestoreSession;
import cz.zcu.kiv.krysl.bsclient.net.messages.client.ClientMessage;
import cz.zcu.kiv.krysl.bsclient.net.messages.server.SMessageLoginOk;
import cz.zcu.kiv.krysl.bsclient.net.messages.server.SMessageRestoreSessionOk;
import cz.zcu.kiv.krysl.bsclient.net.messages.server.ServerMessageKind;
import cz.zcu.kiv.krysl.bsclient.net.types.*;
import cz.zcu.kiv.krysl.bsclient.net.messages.server.ServerMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OldClient {
//
//    private static final Duration TIMEOUT_ALIVE = Duration.ofSeconds(10);
//    private static final Duration TIMEOUT_REQUEST = Duration.ofSeconds(5);
//
//    private final ExecutorService aliveRequestExecutor;
//    private final ExecutorService receiverExecutor;
//
//    private final BlockingQueue<IncomingMessageQueueItem> responseBox;
//    private final Lock requestLock;
//
//    private AtomicBoolean disconnected;
//    private AtomicBoolean offline;
//
//    private Connection<ServerMessage, ClientMessage> connection;
//    private final SessionKey sessionKey;
//    private InetSocketAddress serverAddress;
//    private AtomicBoolean expectingResponse;
//
//    /**
//     * Create a client session on the server.
//     *
//     * @param serverAddress Address of the server.
//     * @param nickname      Nickname to use for login.
//     * @throws ConnectException When an error occurs while connecting or logging in to the server.
//     */
//    public OldClient(InetSocketAddress serverAddress, Nickname nickname) throws ConnectException {
//        this.serverAddress = serverAddress;
//        this.requestLock = new ReentrantLock();
//        this.expectingResponse = new AtomicBoolean(false);
//
//        this.offline = new AtomicBoolean(false);
//        this.disconnected = new AtomicBoolean(false);
//
//        this.responseBox = new SynchronousQueue<>();
//        this.aliveRequestExecutor = Executors.newFixedThreadPool(1);
//        this.receiverExecutor = Executors.newFixedThreadPool(1);
//
//        // setup connection
//        try {
//            // create connection
//            connection = new Connection<>(serverAddress, new Deserializer(), new Serializer(), TIMEOUT_REQUEST);
//            receiverExecutor.submit(this::runReceiving);
//
//            // login
//            ServerMessage response = request(new CMessageLogin(nickname));
//            switch (response.getKind()) {
//                case LOGIN_OK:
//                    SMessageLoginOk m = (SMessageLoginOk) response;
//                    this.sessionKey = m.getSessionKey();
//                case LOGIN_FAIL:
//                    connection.close();
//                    throw new ConnectException("Server is full.");
//                default:
//                    // unexpected response
//                    connection.close();
//                    throw new ConnectException("Server is responding incorrectly.");
//            }
//        } catch (DisconnectedException e) {
//            throw new ConnectException("Server disconnected too early: " + e);
//        } catch (InvalidStateException e) {
//            // should not happen
//            // server must be dumb
//
//            try {
//                invalidMessageReceived();
//            } catch (OfflineException ex) {
//                // we don't care about the error int this situation
//            }
//
//            throw new ConnectException("Server is responding incorrectly.");
//        } catch (IOException e) {
//            throw new ConnectException("Can't connect to the server: " + e.getMessage());
//        }
//    }
//
//    private void invalidMessageReceived() throws OfflineException {
//        offline.set(true);
//        connection.close();
//        throw new OfflineException(OfflineCause.INVALID_MESSAGE);
//    }
//
//    /**
//     * Send the request to the server and receive a response.
//     *
//     * @param request Request to send.
//     * @return A received response.
//     * @throws DisconnectedException If the client is disconnected before or during the call.
//     * @throws OfflineException      If the underlying connection is lost during the call.
//     * @throws InvalidStateException If the request is illegal in the current connection state, or the state was changed during the call.
//     */
//    private ServerMessage request(ClientMessage request) throws DisconnectedException, OfflineException, InvalidStateException {
//        requestLock.lock();
//
//        if (disconnected.get()) {
//            throwDisconnected()
//        }
//
//        //# if online - throw throw Offline
//
//        // send request
//        connection.send(request);
//        expectingResponse.set(true);
//
//        // get response
//        IncomingMessageQueueItem item;
//        while (true) {
//            try {
//                item = responseBox.take();
//                break;
//            } catch (InterruptedException e) {
//                // interrupted
//            }
//        }
//
//        if (item.isOffline()) {
//            // no response because client went offline
//            ResponseOffline offline = (ResponseOffline) item;
//            throw new OfflineException(offline.getCause());
//        }
//
//        if (item.isDisconnected()) {
//            // no response because the server disconnected
//            ResponseDisconnected disconnected = (ResponseDisconnected) item;
//            throw new DisconnectedException("Server disconnected.");
//        }
//
//        ServerMessage response = ((ResponseMessage) item).getResponse();
//
//        if (response.getKind() == ServerMessageKind.ILLEGAL_STATE) {
//            throw new InvalidStateException("Action is illegal in the current client state.");
//        }
//
//        requestLock.unlock();
//        return response;
//    }
//
//    /**
//     * Check if the client is offline.
//     *
//     * @return True if offline, false otherwise.
//     */
//    public boolean isOffline() {
//        return offline.get();
//    }
//
//
//
//
//
//    private void runReceiving() {
//        Instant lastReceived = Instant.now();
//
//        while (!offline.get() && !disconnected.get()) {
//            try {
//                ServerMessage message = connection.receive();
//
//                if (message == null) {
//                    // receive timeout happened
//
//                    Instant now = Instant.now();
//                    Duration elapsed = Duration.between(lastReceived, now);
//
//                    if (elapsed.compareTo(TIMEOUT_ALIVE) >= 0) {
//                        // alive timeout happened
//
//                        if (responseBox.offer(new ResponseOffline(OfflineCause.UNAVAILABLE))) {
//                            // response expected but server not responding, probably network error
//                            offline.set(true);
//                            break;
//                        } else {
//                            // no response expected
//                            // send alive message
//                            aliveRequestExecutor.submit(() -> {
//                                try {
//                                    ServerMessage response = request(new CMessageAlive());
//                                    if (response.getKind() != ServerMessageKind.ALIVE_OK) {
//                                        invalidMessageReceived();
//                                    }
//                                } catch (DisconnectedException | OfflineException | InvalidStateException e) {
//                                    // don't care for errors
//                                }
//                            });
//                        }
//                    }
//                    continue;
//                }
//
//                lastReceived = Instant.now();
//
//                if (message.isResponse()) {
//                    // hand response to waiting request
//                    if (!responseBox.offer(new ResponseMessage(message))) {
//                        // response not expected
//                        offline.set(true);
//                        responseBox.offer(new ResponseOffline(OfflineCause.INVALID_MESSAGE));
//                        break;
//                    }
//                    continue;
//                }
//
//                // TODO: handle all server notifications
//                switch (message.getKind()) {
//                    case DISCONNECT:
//                        disconnected.set(true);
//                        responseBox.offer(new ResponseDisconnected());
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
//            } catch (DisconnectedException e) {
//                // stream closed
//                offline.set(true);
//                responseBox.offer(new ResponseOffline(OfflineCause.CLOSED));
//                break;
//            } catch (DeserializeException e) {
//                // stream corrupted
//                offline.set(true);
//                responseBox.offer(new ResponseOffline(OfflineCause.CORRUPTED));
//                break;
//            }
//        }
//
//        if (disconnected.get()) {
//            shutdown();
//        } else {
//            connection.close();
//        }
//    }
//
//    private void shutdown() {
//        this.connection.close();
//        this.receiverExecutor.shutdown();
//        this.aliveRequestExecutor.shutdown();
//    }
//
//    private void throwExceptionIfDisconnected() throws DisconnectedException {
//        if (disconnected.get()) {
//            throw new DisconnectedException("Can't do any action with disconnected Client.");
//        }
//    }
//
//    @Override
//    synchronized public RestoreState restore() throws AlreadyOnlineException, ConnectException, DisconnectedException, OfflineException {
//        throwExceptionIfDisconnected();
//
//        if (!isOffline()) {
//            throw new AlreadyOnlineException("Client is already online.");
//        }
//
//        try {
//            connection = new Connection<>(serverAddress, new Deserializer(), new Serializer(), TIMEOUT_REQUEST);
//            offline.set(false);
//            receiverExecutor.submit(this::runReceiving);
//
//            ServerMessage response = request(new CMessageRestoreSession(sessionKey));
//
//            switch (response.getKind()) {
//                case RESTORE_SESSION_OK:
//                    SMessageRestoreSessionOk m = (SMessageRestoreSessionOk) response;
//                    return m.getState();
//                case RESTORE_SESSION_FAIL:
//                    disconnected.set(true);
//                    throw new DisconnectedException("Session has expired.");
//                default:
//                    // unexpected response
//                    invalidMessageReceived();
//            }
//        } catch (IOException e) {
//            offline.set(true);
//            connection.close();
//            throw new ConnectException("Can't connect to the server: " + e.getMessage());
//        } catch (InvalidStateException e) {
//            // should not happen
//            // server must be dumb
//            offline.set(true);
//            connection.close();
//            throw new OfflineException(OfflineCause.INVALID_MESSAGE);
//        }
//    }
//
//    @Override
//    synchronized public boolean joinGame() throws DisconnectedException, OfflineException, InvalidStateException {
//        return false;
//    }
//
//    @Override
//    synchronized public boolean chooseLayout(Layout layout) throws DisconnectedException, OfflineException, InvalidStateException {
//        return false;
//    }
//
//    @Override
//    synchronized public ShootResult shoot(Position position) throws DisconnectedException, OfflineException, InvalidStateException {
//        return null;
//    }
//
//    @Override
//    synchronized public void leaveGame() throws DisconnectedException, OfflineException, InvalidStateException {
//
//    }
//
//    @Override
//    synchronized public void disconnect() throws DisconnectedException, OfflineException {
//
//    }
//
//    @Override
//    synchronized public void close() {
//        shutdown();
//    }
}