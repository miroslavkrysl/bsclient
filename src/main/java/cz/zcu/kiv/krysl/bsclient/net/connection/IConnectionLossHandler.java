package cz.zcu.kiv.krysl.bsclient.net.connection;

public interface IConnectionLossHandler {
    /**
     * Handle the event caused by loosing the connection.
     *
     * @param cause Cause of the connection loss.
     */
    void handleConnectionLoss(ConnectionLossCause cause);
}
