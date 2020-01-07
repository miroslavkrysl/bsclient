package cz.zcu.kiv.krysl.bsclient.net.connection;

/**
 * Handles connection loss.
 */
public interface IConnectionLossHandler {
    /**
     * Handle the event caused by loosing the connection.
     *
     * @param cause Cause of the connection loss.
     */
    void handleConnectionLoss(ConnectionLossCause cause);
}
