package cz.zcu.kiv.krysl.bsclient.net.message.client;

public class CMessageDisconnect extends ClientMessage {
    public CMessageDisconnect() {
        super(ClientMessageKind.DISCONNECT);
    }
}