package cz.zcu.kiv.krysl.bsclient.net.connection;

import cz.zcu.kiv.krysl.bsclient.net.ConnectionCloseCause;
import cz.zcu.kiv.krysl.bsclient.net.IMessage;

public interface IConnectionEventHandler {
    /**
     * Handle message incoming from the server.
     */
    void handleServerMessage(IMessage message);

    /**
     * Handle connection loss caused by a network error.
     */
    void handleConnectionLost();

    /**
     * Handle connection loss caused by proper server notification.
     */
    void handleConnectionClosed(ConnectionCloseCause cause);
}
