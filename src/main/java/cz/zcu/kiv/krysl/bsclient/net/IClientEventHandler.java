package cz.zcu.kiv.krysl.bsclient.net;

public interface IClientEventHandler {
    void handle();
    void handleOnline();
    void handleOffline();
    void handleConnectionClosed(ConnectionCloseCause cause);
}
