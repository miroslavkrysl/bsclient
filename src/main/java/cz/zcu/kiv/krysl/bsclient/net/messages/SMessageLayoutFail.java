package cz.zcu.kiv.krysl.bsclient.net.messages;

public class SMessageLayoutFail extends ServerMessage {
    public SMessageLayoutFail() {
        super(ServerMessageKind.LAYOUT_FAIL);
    }
}