package cz.zcu.kiv.krysl.bsclient.net;

import cz.zcu.kiv.krysl.bsclient.game.Position;
import cz.zcu.kiv.krysl.bsclient.net.connection.Connection;
import cz.zcu.kiv.krysl.bsclient.net.connection.ConnectionLossCause;
import cz.zcu.kiv.krysl.bsclient.net.results.*;
import cz.zcu.kiv.krysl.bsclient.net.types.Layout;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;

public class Client implements IConnectionEventHandler {
    private static final Duration TIMEOUT_RECONNECT = Duration.ofSeconds(30);
    private static final Duration TIMEOUT_REQUEST = Duration.ofSeconds(3);
    private static final Duration TIMEOUT_ALIVE = Duration.ofSeconds(5);

    private InetSocketAddress serverAddress;
    private final String nickname;
    private IClientEventHandler eventHandler;

    private Connection connection;
    private SessionManager sessionManager;

    public Client(InetSocketAddress serverAddress, String nickname, IClientEventHandler eventHandler) {
        this.serverAddress = serverAddress;
        this.nickname = nickname;
        this.eventHandler = eventHandler;
    }

    synchronized public ConnectResult connect() throws AlreadyConnectedException, IOException {
        if (isConnected()) {
            throw new AlreadyConnectedException();
        }

        connection = new Connection(serverAddress, new BsCodec(), this);

        // TODO


        return null;
    }

    synchronized private boolean isConnected() {
        return connection != null && !connection.isConnected();
    }

    synchronized public DisconnectResult disconnect() throws NotConnectedException {
        if (!isConnected()) {
            throw new NotConnectedException("Client must be connected to perform disconnect.");
        }

        // TODO
        return null;
    }

    synchronized public JoinGameResult joinGame() throws NotConnectedException {
        if (!isConnected()) {
            throw new NotConnectedException("Client must be connected to perform joinGame.");
        }

        // TODO
        return null;
    }

    synchronized public LeaveGameResult leaveGame() throws NotConnectedException {
        if (!isConnected()) {
            throw new NotConnectedException("Client must be connected to perform leaveGame.");
        }

        // TODO
        return null;
    }

    synchronized public ChooseLayoutResult chooseLayout(Layout layout) throws NotConnectedException {
        if (!isConnected()) {
            throw new NotConnectedException("Client must be connected to perform chooseLayout.");
        }

        // TODO
        return null;
    }

    synchronized public ShootResult shoot(Position position) throws NotConnectedException {
        if (!isConnected()) {
            throw new NotConnectedException("Client must be connected to perform shoot.");
        }

        // TODO
        return null;
    }


    // --- IConnectionManager ---

    @Override
    public void handleServerMessage(IMessage message) {
        // TODO: handle server message
    }

    @Override
    public void handleConnectionLost() {
        // TODO: handle reconnect
    }

    @Override
    public void handleConnectionClosed(ConnectionLossCause cause) {
        eventHandler.handleConnectionClosed(cause);
    }
}