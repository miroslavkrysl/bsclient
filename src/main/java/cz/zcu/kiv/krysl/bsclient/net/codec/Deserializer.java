package cz.zcu.kiv.krysl.bsclient.net.codec;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.connection.IDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.message.Message;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * This class decodes a stream of bytes into stream of Unicode text using the UTF-8 encoding
 * and then deserializes the stream into separated messages.
 * Every connection must have only one Decoder, because the decoder remembers all previous
 * stream segments until a Message is built.
 */
public class Deserializer implements IDeserializer<Message> {

    public static final byte MESSAGE_END = '\n';
    public static final byte ESCAPE = '\\';
    public static final Charset ENCODING = StandardCharsets.UTF_8;

    private List<byte[]> parts;

    public Deserializer() {
        this.parts = new ArrayList<>();
    }

    /**
     * Try to deserialize first message from buffered stream parts.
     * Data belonging to following messages is preserved between calls.
     *
     * @return First message from the stream or null, if the message is not complete yet
     */
    private Message deserializeNextMessage() throws DeserializeException {
        int termPos = findSeparator();

        if (termPos == -1) {
            // message is not complete yet
            return null;
        }

        byte[] messageBytes = new byte[termPos];

        int remaining = termPos;
        int offset = 0;

        // iterate over buffered parts and copy fragmented message data into one messageBytes buffer
        Iterator<byte[]> iterator = parts.iterator();
        while (iterator.hasNext()) {
            byte[] part = iterator.next();

            if (remaining - part.length < 0) {
                // message is complete, but there is more data pending for the next one

                // copy data from the first message
                System.arraycopy(part, 0, messageBytes, offset, remaining);

                // copy remaining data belonging to second message back to the parts buffer
                // and skip the termination symbol
                int nextPos = remaining + 1;
                byte[] rest = new byte[part.length - nextPos];
                System.arraycopy(part, nextPos, rest, 0, part.length - nextPos);

                parts.set(0, rest);

                break;
            }
            else {
                // message is not complete yet
                // copy whole part
                System.arraycopy(part, 0, messageBytes, offset, part.length);
                offset += part.length;
                remaining -= part.length;

                // remove part from buffer
                iterator.remove();
            }
        }

        return Message.deserialize(new String(messageBytes, ENCODING));
    }

    /**
     * Unescape control bytes used for placing messages into the stream.
     * Removes all escape bytes followed by a control byte.
     *
     * @param data Data to unescape.
     * @return Escaped data.
     */
    private byte[] unescape(byte[] data) {
        ByteArrayOutputStream unescaped = new ByteArrayOutputStream();

        // unescape bytes
        boolean escape = false;

        for (byte b : data) {
            if (escape) {
                escape = false;

                if (b != MESSAGE_END) {
                    unescaped.write(ESCAPE);
                }
            }

            if (b == ESCAPE) {
                escape = true;
                continue;
            }

            unescaped.write(b);
        }

        return unescaped.toByteArray();
    }

    /**
     * Find the message end symbol in buffered message parts.
     *
     * @return Position of the first message end symbol occurrence or -1 if not present.
     */
    private int findSeparator() {
        int offset = 0;
        boolean escape = false;

        for (byte[] part : parts) {
            for (int i = 0; i < part.length; i++) {
                if (escape) {
                    escape = false;
                    continue;
                }

                if (part[i] == ESCAPE) {
                    escape = true;
                    continue;
                }

                if (part[i] == MESSAGE_END) {
                    return offset + i;
                }
            }
        }

        return -1;
    }

    // --- IDeserializer
    @Override
    public List<Message> deserialize(byte[] data) throws DeserializeException {
        parts.add(data);

        ArrayList<Message> messages = null;

        while (!parts.isEmpty()) {
            Message msg = deserializeNextMessage();

            if (msg == null) {
                break;
            }

            if (messages == null) {
                messages = new ArrayList<>();
            }

            messages.add(msg);
        }

        return messages;
    }
}
