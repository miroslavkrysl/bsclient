package cz.zcu.kiv.krysl.bsclient.net;

import cz.zcu.kiv.krysl.bsclient.net.connection.ConnectionLossCause;

public interface IClientEventHandler {
    void handle();
    void handleOnline();
    void handleOffline();
    void handleConnectionClosed(ConnectionLossCause cause);
}
