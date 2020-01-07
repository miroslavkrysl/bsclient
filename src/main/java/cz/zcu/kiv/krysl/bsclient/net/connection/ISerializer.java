package cz.zcu.kiv.krysl.bsclient.net.connection;

public interface ISerializer {
    byte[] serialize(IMessage message);
}
