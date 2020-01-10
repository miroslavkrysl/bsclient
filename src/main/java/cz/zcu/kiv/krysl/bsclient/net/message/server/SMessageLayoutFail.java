package cz.zcu.kiv.krysl.bsclient.net.message.server;

public class SMessageLayoutFail extends ServerMessage {

    public SMessageLayoutFail() {
        super(ServerMessageKind.LAYOUT_FAIL);
    }
}
