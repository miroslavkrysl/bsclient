package cz.zcu.kiv.krysl.bsclient.net.codec;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.connection.IDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.message.Message;
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
public class Deserializer implements IDeserializer<Message> {

    private CharsetDecoder stringDecoder;
    private CharBuffer charBuffer;
    private StringBuilder stringBuffer;
    private static final String CHARS_TO_ESCAPE = String.valueOf(CodecConstants.MESSAGE_END);

    public Deserializer(Charset encoding) {
        this.stringDecoder = encoding.newDecoder();
        this.stringDecoder.onMalformedInput(CodingErrorAction.REPORT);
        this.stringDecoder.onUnmappableCharacter(CodingErrorAction.REPORT);
        this.charBuffer = CharBuffer.allocate(1024);
        this.stringBuffer = new StringBuilder(CodecConstants.MAX_MESSAGE_LENGTH * 2);
    }

    // --- IDeserializer
    @Override
    public List<Message> deserialize(byte[] data) throws DeserializeException {
        LinkedList<Message> messages = new LinkedList<>();

        // decode bytes into string
        while(true) {
            // decode bytes into the charBuffer
            charBuffer.clear();
            CoderResult result = stringDecoder.decode(ByteBuffer.wrap(data), charBuffer, false);

            if (result.isError()) {
                throw new DeserializeException("String decoding failed: stream of chars is encoded improperly.");
            }

            // append decoded string into the stringBuffer
            stringBuffer.append(charBuffer.toString());

            if (result.isUnderflow()) {
                // all bytes was decoded
                break;
            }
        }


        // deserialize string into messages
        int offset = 0;

        while (true) {
            int separatorPos = Helper.findChar(stringBuffer, offset, CodecConstants.MESSAGE_END, CodecConstants.ESCAPE);

            if (separatorPos == -1) {
                // message is not complete yet

                if ((stringBuffer.length() - offset) > CodecConstants.MAX_MESSAGE_LENGTH) {
                    throw new DeserializeException("Too long stream segment to be a valid message.");
                }

                break;
            }

            String messageString = stringBuffer.subSequence(offset, separatorPos).toString();
            offset = separatorPos + 1;

            // unescape message end char
            messageString = Helper.unescapeChars(messageString, CHARS_TO_ESCAPE, CodecConstants.ESCAPE);

            // build message
            MessageDeserializer messageDeserializer = new MessageDeserializer(messageString);
            Message message = messageDeserializer.deserialize();

            messages.add(message);
        }

        return messages;
    }
}
