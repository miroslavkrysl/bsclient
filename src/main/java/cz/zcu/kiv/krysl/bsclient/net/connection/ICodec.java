package cz.zcu.kiv.krysl.bsclient.net.connection;

/**
 * Class represents a serialization and deserialization logic.
 */
public interface ICodec<MessageIn, MessageOut> {
    /**
     * Get a new serializer associated with this codec.
     *
     * @return A new serializer.
     */
    ISerializer<MessageOut> newSerializer();

    /**
     * Get a new deserializer associated with this codec.
     *
     * @return A new deserializer.
     */
    IDeserializer<MessageIn> newDeserializer();
}
