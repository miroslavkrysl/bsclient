package cz.zcu.kiv.krysl.bsclient.net.connection;

import java.io.IOException;

public interface IDeserializer<MessageIn> {
    /**
     * Append bytes into internal buffer and deserialize all available messages.
     *
     * @param bytes Data to append into internal buffer.
     * @return All available messages. It could be empty.
     */
    MessageIn[] deserialize(byte[] bytes) throws IOException;
}
