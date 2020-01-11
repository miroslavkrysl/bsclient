package cz.zcu.kiv.krysl.bsclient.net.client;

/**
 * Handles connection loss.
 */
public interface IConnectionManager<MessageIn> {

    /**
     * Handle the incoming message.
     *
     * @param message The received message.
     */
    void handleMessageReceived(MessageIn message);

    /**
     * Handle the connection loss.
     *
     * @param cause Cause of the connection loss.
     */
    void handleConnectionLost(ConnectionLossCause cause);
}