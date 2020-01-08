package cz.zcu.kiv.krysl.bsclient.net.messages;

public class SMessageLayoutOk extends ServerMessage {
    public SMessageLayoutOk() {
        super(ServerMessageKind.LAYOUT_OK);
    }
}