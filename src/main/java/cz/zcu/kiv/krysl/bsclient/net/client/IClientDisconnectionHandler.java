package cz.zcu.kiv.krysl.bsclient.net.client;

public interface IClientDisconnectionHandler {
    void handleDisconnected(ConnectionLossCause cause);
}
