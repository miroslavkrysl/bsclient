package cz.zcu.kiv.krysl.bsclient.net.codec;

import cz.zcu.kiv.krysl.bsclient.net.messages.Message;

import java.util.*;

/**
 * This class decodes a stream of bytes into separated messages and builds
 * a Message using the Deserializer.
 * Every connection must have only one Decoder, because the decoder remembers all previous
 * stream segments until a Message is built.
 */
public class Decoder {

    private Deserializer deserializer;

    private static final Byte TERMINATION = '\n';
    private static final Byte ESCAPE = '\\';
    public List<byte[]> parts;

    public Decoder(Deserializer deserializer) {
        this.deserializer = deserializer;
        this.parts = new ArrayList<>();
    }

    /**
     * Try to build first message from buffered stream parts.
     * Data belonging to following messages is preserved between calls.
     *
     * @return First message from the stream or null, if the message is not complete yet
     */
    private Message buildMessage() {
        int termPos = findTermination();

        if (termPos == -1) {
            // message is not complete yet
            return null;
        }

        byte[] messageData = new byte[termPos];

        int remaining = termPos;
        int offset = 0;

        // iterate over buffered parts and copy fragmented message data into one buffer
        Iterator<byte[]> iterator = parts.iterator();
        while (iterator.hasNext()) {
            byte[] part = iterator.next();

            if (remaining - part.length < 0) {
                // message is complete, but there is more data pending for the next one

                // copy data from the first message
                System.arraycopy(part, 0, messageData, offset, remaining);

                // copy remaining data belonging to second message back to the parts buffer
                // and skip the termination symbol
                int nextPos = remaining + 1;
                byte[] rest = new byte[part.length - nextPos];
                System.arraycopy(part, nextPos, rest, 0, part.length - nextPos);

                parts.set(0, rest);

                break;
            }
            else {
                System.arraycopy(part, 0, messageData, offset, part.length);
                offset += part.length;
                remaining -= part.length;

                // remove part from buffer
                iterator.remove();
            }
        }

        return deserializer.deserialize(messageData);
    }

    /**
     * Find the message termination symbol in buffered message parts.
     *
     * @return Position of the first termination symbol occurrence or -1 if not present.
     */
    private int findTermination() {
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

                if (part[i] == TERMINATION) {
                    return offset + i;
                }
            }
        }

        return -1;
    }

    /**
     * Append data to the internal buffer and try to build a message (or more messages if possible).
     *
     * @param data Data segment to append into buffer.
     * @return All complete messages that can be constructed from buffer.
     *  Messages are sorted in the same order they were constructed.
     */
    public List<Message> decode(byte[] data) {
        parts.add(data);

        List<Message> messages = null;

        while (!parts.isEmpty()) {
            Message msg = buildMessage();

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
