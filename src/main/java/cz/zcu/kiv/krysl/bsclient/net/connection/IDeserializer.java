package cz.zcu.kiv.krysl.bsclient.net.connection;

import java.io.IOException;

/**
 * Deserializes messages from a stream of bytes.
 *
 * @param <MessageIn> Messages type.
 */
public interface IDeserializer<MessageIn> {
    /**
     * Append bytes into internal buffer and deserialize all available messages.
     *
     * @param bytes Data to append into internal buffer.
     * @return All available messages. It could be empty.
     */
    MessageIn[] deserialize(byte[] bytes) throws IOException;
}
