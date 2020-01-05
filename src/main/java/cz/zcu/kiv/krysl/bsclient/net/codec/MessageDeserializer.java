package cz.zcu.kiv.krysl.bsclient.net.codec;

import cz.zcu.kiv.krysl.bsclient.net.messages.Message;

import java.io.InputStream;

public interface MessageDeserializer {

    public Message deserialize(InputStream inputStream);
}
