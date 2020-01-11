package cz.zcu.kiv.krysl.bsclient.net.connection;

import cz.zcu.kiv.krysl.bsclient.net.DisconnectedException;

/**
 * A stream that messages can be received from.
 *
 * @param <MessageOut> Outgoing messages type
 */
public interface IMessageOutputStream<MessageOut> {
    /**
     * Send a message.
     *
     * @param message Message to send.
     * @throws DisconnectedException If the connection is disconnected before or during the call.
     */
    void send(MessageOut message) throws DisconnectedException;
}
