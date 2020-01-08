package cz.zcu.kiv.krysl.bsclient.net.messages;

public class CMessageDisconnect extends ClientMessage {
    public CMessageDisconnect() {
        super(ClientMessageKind.DISCONNECT);
    }
}