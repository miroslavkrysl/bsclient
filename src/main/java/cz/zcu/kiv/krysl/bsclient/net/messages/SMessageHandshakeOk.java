package cz.zcu.kiv.krysl.bsclient.net.messages;

public class SMessageHandshakeOk extends ServerMessage {
    public SMessageHandshakeOk() {
        super(ServerMessageKind.HANDSHAKE_OK);
    }
}