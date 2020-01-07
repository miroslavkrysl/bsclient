package cz.zcu.kiv.krysl.bsclient.net.connection;

public interface IConnectionEventHandler {
    /**
     * Handle the event caused by the closing the connection.
     */
    void handleConnectionClosed(ConnectionLossCause cause);
}
