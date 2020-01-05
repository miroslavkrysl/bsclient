package cz.zcu.kiv.krysl.bsclient.net.connection;

public interface ISocketClosedHandler {
    void handleSocketClosed(SocketCloseCause cause);
}
