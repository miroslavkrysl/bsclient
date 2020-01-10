package cz.zcu.kiv.krysl.bsclient.net.codec;

public class Constants {
    public static final char PAYLOAD_START = ':';
    public static final char PAYLOAD_ITEM_SEPARATOR = ';';
    public static final char MESSAGE_END = '\n';
    public static final char ESCAPE = '\\';
    public static final int MAX_MESSAGE_LENGTH = 1024;
    public static final int CHAR_BUFFER_LENGTH = 2048;
}
