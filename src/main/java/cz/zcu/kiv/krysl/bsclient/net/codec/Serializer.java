package cz.zcu.kiv.krysl.bsclient.net.codec;

import cz.zcu.kiv.krysl.bsclient.net.connection.ISerializer;
import cz.zcu.kiv.krysl.bsclient.net.message.Message;
import cz.zcu.kiv.krysl.bsclient.util.Helper;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

public class Serializer implements ISerializer<Message> {

    private static final String CHARS_TO_ESCAPE = String.valueOf(Codec.MESSAGE_END);
    private static final String MESSAGE_END = String.valueOf(Codec.MESSAGE_END);

    private Charset encoding;

    public Serializer(Charset encoding) {
        this.encoding = encoding;
    }

    @Override
    public byte[] serialize(Message message) {
        ByteArrayOutputStream serialized = new ByteArrayOutputStream();

        MessageSerializer messageSerializer = new MessageSerializer(message);
        String messageString = messageSerializer.serialize();

        // escape message end char
        messageString = Helper.escapeChars(messageString, CHARS_TO_ESCAPE, Codec.ESCAPE);

        serialized.writeBytes(encoding.encode(messageString).array());
        serialized.writeBytes(encoding.encode(MESSAGE_END).array());

        return serialized.toByteArray();
    }
}
