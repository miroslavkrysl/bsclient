package cz.zcu.kiv.krysl.bsclient.net.codec;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Constants {
    public static final char PAYLOAD_START = ':';
    public static final char PAYLOAD_ITEM_SEPARATOR = ';';
    public static final char MESSAGE_END = '\n';
    public static final char ESCAPE = '\\';
    public static final int MAX_MESSAGE_LENGTH = 4096;
    public static final Charset ENCODING = StandardCharsets.UTF_8;
}
