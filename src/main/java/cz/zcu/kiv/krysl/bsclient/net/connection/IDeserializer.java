package cz.zcu.kiv.krysl.bsclient.net.connection;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;

import java.util.List;

/**
 * Deserializes messages from a stream of bytes.
 *
 * @param <MessageIn> Messages type.
 */
public interface IDeserializer<MessageIn> {
    /**
     * Append bytes into internal buffer and deserialize all available messages.
     *
     * @param data Data to append into internal buffer.
     * @return All available messages. It could be empty.
     * @throws DeserializeException When the stream can't be properly deserialized.
     */
    List<MessageIn> deserialize(byte[] data) throws DeserializeException;
}
