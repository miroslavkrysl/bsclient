package cz.zcu.kiv.krysl.bsclient.net.messages.server;

public class SMessageReconnectFail extends ServerMessage {

    SMessageReconnectFail() {
        super(ServerMessageKind.RECONNECT_FAIL);
    }
}