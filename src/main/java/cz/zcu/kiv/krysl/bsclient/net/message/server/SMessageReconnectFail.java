package cz.zcu.kiv.krysl.bsclient.net.message.server;

public class SMessageReconnectFail extends ServerMessage {

    SMessageReconnectFail() {
        super(ServerMessageKind.RECONNECT_FAIL);
    }
}
