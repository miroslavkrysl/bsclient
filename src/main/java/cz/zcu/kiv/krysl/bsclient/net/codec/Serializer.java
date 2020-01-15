package cz.zcu.kiv.krysl.bsclient.net.codec;

import cz.zcu.kiv.krysl.bsclient.net.connection.ISerializer;
import cz.zcu.kiv.krysl.bsclient.net.messages.client.ClientMessage;
import cz.zcu.kiv.krysl.bsclient.util.Helper;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

public class Serializer implements ISerializer<ClientMessage> {

    private static final String CHARS_TO_ESCAPE = String.valueOf(Constants.MESSAGE_END);
    private static final String MESSAGE_END = String.valueOf(Constants.MESSAGE_END);

    private Charset encoding;

    public Serializer() {
        this.encoding = Constants.ENCODING;
    }

    @Override
    public byte[] serialize(ClientMessage message) {
        ByteArrayOutputStream serialized = new ByteArrayOutputStream();

        String messageString = message.serialize();

        // escape message end char and append it to the message end
        messageString = Helper.escapeChars(messageString, CHARS_TO_ESCAPE, Constants.ESCAPE) + MESSAGE_END;

        serialized.writeBytes(encoding.encode(messageString).array());

        return serialized.toByteArray();
    }
}
