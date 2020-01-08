package cz.zcu.kiv.krysl.bsclient.net.messages;

public class SMessageReconnectFail extends ServerMessage {
    public SMessageReconnectFail() {
        super(ServerMessageKind.RECONNECT_FAIL);
    }
}