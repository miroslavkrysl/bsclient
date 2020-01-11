package cz.zcu.kiv.krysl.bsclient.net.client;

/**
 * Handles incoming messages.
 *
 * @param <MessageIn> Incoming messages type.
 */
public interface IIncomingMessageHandler<MessageIn> {
    /**
     * Handle an event caused by the incoming message.
     * This can be a blocking call.
     *
     * @param message The incoming message.
     * @throws InterruptedException When the implemented method makes blocking call which is interrupted.
     */
    void handleIncomingMessage(MessageIn message) throws InterruptedException;
}
