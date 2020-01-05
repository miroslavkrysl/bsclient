package cz.zcu.kiv.krysl.bsclient.net.connection;

import cz.zcu.kiv.krysl.bsclient.net.IMessage;

public interface IDeserializer {
    /**
     * Append bytes into internal buffer and deserialize all available messages.
     *
     * @param bytes Data to append into internal buffer.
     * @return All available messages. It could be empty.
     */
    IMessage[] deserialize(byte[] bytes);
}
