package cz.zcu.kiv.krysl.bsclient.net.messages;

public class SMessageHandshakeFail extends ServerMessage {
    public SMessageHandshakeFail() {
        super(ServerMessageKind.HANDSHAKE_FAIL);
    }
}