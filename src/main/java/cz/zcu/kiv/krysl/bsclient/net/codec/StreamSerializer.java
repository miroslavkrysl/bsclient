package cz.zcu.kiv.krysl.bsclient.net.codec;

import cz.zcu.kiv.krysl.bsclient.net.messages.Message;

/**
 * This class serializes messages into stream of bytes.
 */
public class StreamSerializer {

    /**
     * Escape control bytes used for message stream serialization.
     * Prepends an escape byte before every message end byte.
     *
     * @param data Data to escape.
     * @return Escaped data.
     */
    private byte[] escape(byte[] data) {

        // count control bytes in array
        int controlBytesCount = 0;
        for (byte b : data) {
            if (b == Protocol.MESSAGE_END) {
                controlBytesCount++;
            }
        }

        byte[] escaped = new byte[data.length + controlBytesCount];

        // escape control bytes
        for (int i = 0, j = 0; i < data.length; i++, j++) {
            if (data[i] == Protocol.MESSAGE_END) {
                escaped[j] = Protocol.ESCAPE;
                j++;
            }
            escaped[j] = data[i];
        }

        return escaped;
    }

    /**
     * Serialize message into stream of bytes.
     * Includes message separator and escaping.
     *
     * @param message Message to encode.
     * @return Bytes of encoded message.
     */
    public byte[] serialize(Message message) {
        messageSerializer
    }
}
