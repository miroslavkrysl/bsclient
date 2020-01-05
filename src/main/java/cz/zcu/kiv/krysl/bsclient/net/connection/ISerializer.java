package cz.zcu.kiv.krysl.bsclient.net.connection;

import cz.zcu.kiv.krysl.bsclient.net.IMessage;

public interface ISerializer {
    byte[] serialize(IMessage message);
}
