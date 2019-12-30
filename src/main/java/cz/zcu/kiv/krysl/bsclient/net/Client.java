package cz.zcu.kiv.krysl.bsclient.net;

import cz.zcu.kiv.krysl.bsclient.game.Position;
import cz.zcu.kiv.krysl.bsclient.net.connection.Connection;
import cz.zcu.kiv.krysl.bsclient.net.message.Message;
import cz.zcu.kiv.krysl.bsclient.net.results.*;
import cz.zcu.kiv.krysl.bsclient.net.types.Layout;

import java.net.InetSocketAddress;

public class Client {
    private InetSocketAddress serverAddress;
    private final String nickname;
    private ClientEventHandler eventHandler;
    private Connection connection;
    private SessionManager sessionManager;

    public Client(InetSocketAddress serverAddress, ClientEventHandler eventHandler) {
        this.serverAddress = serverAddress;
        this.eventHandler = eventHandler;
    }

    public ConnectResult connect() throws AlreadyConnectedException {
        if (isConnected()) {
            throw new AlreadyConnectedException();
        }

        connection = new Connection(serverAddress);

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
}