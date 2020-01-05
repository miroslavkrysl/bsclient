package cz.zcu.kiv.krysl.bsclient.net;

import cz.zcu.kiv.krysl.bsclient.game.Position;
import cz.zcu.kiv.krysl.bsclient.net.connection.Connection;
import cz.zcu.kiv.krysl.bsclient.net.connection.IConnectionEventHandler;
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

    public ConnectResult connect() throws AlreadyConnectedException, IOException {
        if (isConnected()) {
            throw new AlreadyConnectedException();
        }

        connection = new Connection(serverAddress, new BsCodec(), );

        // TODO


        return null;
    }

    private boolean isConnected() {
        return connection != null && !connection.isConnected();
    }

    public DisconnectResult disconnect() throws NotConnectedException {
        if (!isConnected()) {
            throw new NotConnectedException();
        }

        // TODO
        return null;
    }

    public JoinGameResult joinGame() {
        // TODO
        return null;
    }

    public LeaveGameResult leaveGame() {
        // TODO
        return null;
    }

    public ChooseLayoutResult chooseLayout(Layout layout) {
        // TODO
        return null;
    }

    public ShootResult shoot(Position position) {
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
    public void handleConnectionClosed(ConnectionCloseCause cause) {
        eventHandler.handleConnectionClosed(cause);
    }
}