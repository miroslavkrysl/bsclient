package cz.zcu.kiv.krysl.bsclient.net.messages;

public class CMessageHandshake extends ClientMessage {
    public CMessageHandshake() {
        super(ClientMessageKind.HANDSHAKE);
    }
}
