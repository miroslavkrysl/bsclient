package cz.zcu.kiv.krysl.bsclient.net;

import cz.zcu.kiv.krysl.bsclient.net.connection.ICodec;

public class BsCodec implements ICodec {
    public static final byte MESSAGE_END = '\n';
    public static final byte ITEMS_SEPARATOR = ' ';
    public static final byte ESCAPE = '\\';
    public static final int MAX_MESSAGE_LENGTH = 1024;

    @Override
    public ISerializer newSerializer() {
        return new BsSerializer();
    }

    @Override
    public IDeserializer newDeserializer() {
        return new BsDeserializer();
    }
}
