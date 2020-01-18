package cz.zcu.kiv.krysl.bsclient.net.client;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.DisconnectedException;
import cz.zcu.kiv.krysl.bsclient.net.client.responses.Response;
import cz.zcu.kiv.krysl.bsclient.net.client.responses.ResponseDisconnected;
import cz.zcu.kiv.krysl.bsclient.net.client.responses.ResponseMessage;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class Client implements BattleshipsClient {
    private static Logger logger = LogManager.getLogger(Client.class);

    private static final Duration TIMEOUT_ALIVE = Duration.ofSeconds(3);
//    private static final Duration TIMEOUT_RECEIVE = TIMEOUT_ALIVE.dividedBy(2);
    private static final Duration TIMEOUT_RECEIVE = Duration.ofSeconds(1);

    private IClientEventHandler eventHandler;
    private final InetSocketAddress serverAddress;

    private final Connection<ServerMessage, ClientMessage> connection;

    private final ReentrantLock requestLock;
    private final BlockingQueue<Response> responseBox;

    private final AtomicBoolean disconnected;

    private final Thread receiverThread;
    private final RestoreState restoreState;
    private Nickname nickname;

    public Client(InetSocketAddress serverAddress, Nickname nickname) throws ConnectException {
        this.nickname = nickname;
        this.eventHandler = null;
        this.serverAddress = serverAddress;
        this.requestLock = new ReentrantLock();
        this.responseBox = new SynchronousQueue<>();
        this.disconnected = new AtomicBoolean(false);

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
        } catch (DisconnectedException e) {
            throw new ConnectException("Connection lost too early: " + e.getMessage());
        } catch (InvalidStateException e) {
            // should not happen, server must be dumb
            this.connection.close();
            throw new ConnectException("Server is responding incorrectly.");
        }

        switch (response.getKind()) {
            case LOGIN_OK:
                this.restoreState = null;
                break;
            case LOGIN_FULL:
                this.connection.close();
                throw new ConnectException("Server is full.");
            case LOGIN_TAKEN:
                this.connection.close();
                throw new ConnectException("Nickname is taken.");
            case LOGIN_RESTORED:
                SMessageLoginRestored m = (SMessageLoginRestored) response;
                this.restoreState = m.getState();
                break;
            default:
                // unexpected response
                this.connection.close();
                throw new ConnectException("Server is responding incorrectly.");
        }
    }

    public RestoreState getRestoreState() {
        return restoreState;
    }

    public void setEvenHandler(IClientEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public InetSocketAddress getServerAddress() {
        return this.serverAddress;
    }

    public Nickname getNickname() {
        return this.nickname;
    }

    private void runReceiving() {
        logger.info("starting client receiver thread");
        Instant lastReceived = Instant.now();

        ConnectionLossCause connectionLossCause = null;

        while (!disconnected.get()) {
            try {
                ServerMessage message = connection.receive();

                if (message == null) {
                    // receive timeout happened

                    // measure elapsed time
                    Instant now = Instant.now();
                    Duration elapsed = Duration.between(lastReceived, now);

                    if (elapsed.compareTo(TIMEOUT_ALIVE) >= 0) {
                        // connection timeout happened
                        disconnected.set(true);
                        connectionLossCause = ConnectionLossCause.UNAVAILABLE;

                        logger.warn("connection timed out");

                        if (!requestLock.tryLock()) {
                            // response expected
                            responseBox.offer(new ResponseDisconnected());
                        } else {
                            requestLock.unlock();
                        }
                        break;
                    }

                    if (requestLock.tryLock()) {
                        // response not expected
                        new Thread(this::aliveRequest).start();

                        requestLock.unlock();
                    }

                    continue;
                }

                lastReceived = Instant.now();

                if (message.isResponse()) {
                    logger.trace("response received");
                    if (requestLock.tryLock()) {
                        logger.error("response not expected");
                        disconnected.set(true);
                        connectionLossCause = ConnectionLossCause.INVALID_MESSAGE;

                        requestLock.unlock();
                        break;
                    }

                    // hand response to the waiting request
                    responseBox.offer(new ResponseMessage(message));
                    continue;
                }

                logger.trace("notification received");

                switch (message.getKind()) {
                    case DISCONNECT:
                        disconnected.set(true);
                        connectionLossCause = ConnectionLossCause.SERVER_DISCONNECTED;

                        logger.info("server sent disconnect notification");

                        if (!requestLock.tryLock()) {
                            // response expected
                            responseBox.offer(new ResponseDisconnected());
                        } else {
                            requestLock.unlock();
                        }

                        break;
                    case OPPONENT_JOINED:
                        SMessageOpponentJoined messageOpponentJoined = (SMessageOpponentJoined) message;
                        eventHandler.handleOpponentJoined(messageOpponentJoined.getNickname());
                        break;
                    case OPPONENT_READY:
                        eventHandler.handleOpponentReady();
                        break;
                    case OPPONENT_LEFT:
                        eventHandler.handleOpponentLeft();
                        break;
                    case OPPONENT_MISSED:
                        SMessageOpponentMissed messageOpponentMissed = (SMessageOpponentMissed) message;
                        eventHandler.handleOpponentMissed(messageOpponentMissed.getPosition());
                        break;
                    case OPPONENT_HIT:
                        SMessageOpponentHit messageOpponentHit = (SMessageOpponentHit) message;
                        eventHandler.handleOpponentHit(messageOpponentHit.getPosition());
                        break;
                    case GAME_OVER:
                        SMessageGameOver messageGameOver = (SMessageGameOver) message;
                        eventHandler.handleGameOver(messageGameOver.getWinner());
                        break;
                }
            } catch (DisconnectedException e) {
                // stream closed
                logger.warn("connection lost: " + e.getMessage());
                disconnected.set(true);
                connectionLossCause = ConnectionLossCause.CLOSED;

                if (!requestLock.tryLock()) {
                    // response expected
                    responseBox.offer(new ResponseDisconnected());
                    break;
                } else {
                    requestLock.unlock();
                }
            } catch (DeserializeException e) {
                // stream corrupted
                logger.error("stream corrupted: " + e.getMessage());
                disconnected.set(true);
                connectionLossCause = ConnectionLossCause.CORRUPTED;

                if (!requestLock.tryLock()) {
                    // response expected
                    responseBox.offer(new ResponseDisconnected());
                    break;
                } else {
                    requestLock.unlock();
                }
            }
        }

        logger.info("closing the connection");
        connection.close();

        if (connectionLossCause != null) {
            // unwanted disconnection
            logger.error("connection lost");
            if (eventHandler != null) {
                eventHandler.handleDisconnected(connectionLossCause);
            }
        }

        logger.info("ending the client receiver thread");
    }

    private void aliveRequest() {
        try {
            logger.trace("sending alive request");
            ServerMessage response = request(new CMessageAlive());
            if (response.getKind() != ServerMessageKind.ALIVE_OK) {
                throw handleInvalidMessage();
            }
            logger.trace("alive response received");
        } catch (DisconnectedException | InvalidStateException e) {
            // don't care for this errors
        }
    }

    private DisconnectedException handleInvalidMessage() {
        this.connection.close();
        this.disconnected.set(true);

        return new DisconnectedException("Invalid message was received");
    }

    private void checkConnected() throws DisconnectedException {
        if (disconnected.get()) {
            throw new DisconnectedException("Can't perform a request on disconnected client.");
        }
    }

    private ServerMessage request(ClientMessage request) throws DisconnectedException, InvalidStateException {
        requestLock.lock();
        logger.debug("request");

        try {
            checkConnected();
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
    public Nickname joinGame() throws DisconnectedException, InvalidStateException {
        ServerMessage response = request(new CMessageJoinGame());

        switch (response.getKind()) {
            case JOIN_GAME_OK:
                SMessageJoinGameOk r = (SMessageJoinGameOk) response;
                return r.getOpponent();
            case JOIN_GAME_WAIT:
                return null;
            default:
                throw handleInvalidMessage();
        }
    }

    @Override
    public boolean chooseLayout(Layout layout) throws DisconnectedException, InvalidStateException {
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
    public ShootResult shoot(Position position) throws DisconnectedException, InvalidStateException {
        ServerMessage response = request(new CMessageShoot(position));

        switch (response.getKind()) {
            case SHOOT_HIT:
                return new ShootResultHit();
            case SHOOT_MISSED:
                return new ShootResultMiss();
            case SHOOT_SUNK:
                SMessageShootSunk r = (SMessageShootSunk) response;
                return new ShootResultSunk(r.getShipKind(), r.getPlacement());
            default:
                throw handleInvalidMessage();
        }
    }

    @Override
    public void leaveGame() throws DisconnectedException, InvalidStateException {
        ServerMessage response = request(new CMessageLeaveGame());

        if (response.getKind() != ServerMessageKind.LEAVE_GAME_OK) {
            throw handleInvalidMessage();
        }
    }

    @Override
    public void disconnect() throws DisconnectedException {
        try {
            ServerMessage response = request(new CMessageLogout());

            if (response.getKind() != ServerMessageKind.LOGOUT_OK) {
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
