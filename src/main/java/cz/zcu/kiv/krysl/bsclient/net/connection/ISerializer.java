package cz.zcu.kiv.krysl.bsclient.net.connection;

public interface ISerializer<MessageOut> {
    byte[] serialize(MessageOut message);
}
