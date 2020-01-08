package cz.zcu.kiv.krysl.bsclient.net.messages;

public class SMessageShootOk extends ServerMessage {
    public SMessageShootOk() {
        super(ServerMessageKind.SHOOT_OK);
    }
}