package cz.zcu.kiv.krysl.bsclient.net.connection;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.DisconnectedException;

/**
 * A stream that messages can be sent to.
 *
 * @param <MessageIn> Incoming messages type.
 */
public interface IMessageInputStream<MessageIn> {

    /**
     * Receive a message.
     *
     * @throws DisconnectedException If the connection is disconnected before or during the call.
     */
    MessageIn receive() throws DisconnectedException, DeserializeException;
}
