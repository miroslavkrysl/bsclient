package cz.zcu.kiv.krysl.bsclient.net.connection;

/**
 * Serializes messages into a stream of bytes.
 *
 * @param <MessageOut> Messages type.
 */
public interface ISerializer<MessageOut> {
    /**
     * Serialize message into stream of bytes.
     *
     * @param message A message to serialize.
     * @return Bytes of the serialized message.
     */
    byte[] serialize(MessageOut message);
}
