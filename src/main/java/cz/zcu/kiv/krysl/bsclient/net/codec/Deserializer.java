package cz.zcu.kiv.krysl.bsclient.net.codec;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.connection.IDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.message.server.ServerMessage;
import cz.zcu.kiv.krysl.bsclient.util.Helper;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.*;

/**
 * This class decodes a stream of bytes into stream of Unicode text using the specified encoding
 * and then deserializes the stream into separated messages.
 * Every connection must have only one Decoder, because the decoder remembers all previous
 * stream segments until a Message is built.
 */
public class Deserializer implements IDeserializer<ServerMessage> {

    private CharsetDecoder stringDecoder;
    private CharBuffer charBuffer;
    private StringBuilder stringBuffer;
    private static final String CHARS_TO_ESCAPE = String.valueOf(Constants.MESSAGE_END);

    public Deserializer() {
        this.stringDecoder = Constants.ENCODING.newDecoder();
        this.stringDecoder.onMalformedInput(CodingErrorAction.REPORT);
        this.stringDecoder.onUnmappableCharacter(CodingErrorAction.REPORT);
        this.charBuffer = CharBuffer.allocate(1024);
        this.stringBuffer = new StringBuilder(Constants.MAX_MESSAGE_LENGTH * 2);
    }

    // --- IDeserializer
    @Override
    public List<ServerMessage> deserialize(byte[] data) throws DeserializeException {
        LinkedList<ServerMessage> messages = new LinkedList<>();

        // decode bytes into string
        while(true) {
            // decode bytes into the charBuffer
            charBuffer.clear();
            CoderResult result = stringDecoder.decode(ByteBuffer.wrap(data), charBuffer, false);

            if (result.isError()) {
                throw new DeserializeException("String decoding failed: stream of chars is encoded improperly.");
            }

            // append decoded string into the stringBuffer
            charBuffer.flip();
            stringBuffer.append(charBuffer.toString());

            if (result.isUnderflow()) {
                // all bytes was decoded
                break;
            }
        }


        // deserialize string into messages
        int offset = 0;

        while (true) {
            int separatorPos = Helper.findChar(stringBuffer, offset, Constants.MESSAGE_END, Constants.ESCAPE);

            if (separatorPos == -1) {
                // message is not complete yet

                if ((stringBuffer.length() - offset) > Constants.MAX_MESSAGE_LENGTH) {
                    throw new DeserializeException("Too long stream segment to be a valid message.");
                }

                break;
            }

            String messageString = stringBuffer.subSequence(offset, separatorPos).toString();
            offset = separatorPos + 1;

            // unescape message end char
            messageString = Helper.unescapeChars(messageString, CHARS_TO_ESCAPE, Constants.ESCAPE);

            // build message
            ServerMessage message = ServerMessage.deserialize(messageString);

            messages.add(message);
        }

        stringBuffer = new StringBuilder(stringBuffer.subSequence(offset, stringBuffer.length()).toString());

        return messages;
    }
}
