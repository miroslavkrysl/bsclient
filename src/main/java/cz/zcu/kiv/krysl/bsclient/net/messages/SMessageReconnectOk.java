package cz.zcu.kiv.krysl.bsclient.net.messages;

public class SMessageReconnectOk extends ServerMessage {
    public SMessageReconnectOk() {
        super(ServerMessageKind.RECONNECT_OK);
    }
}