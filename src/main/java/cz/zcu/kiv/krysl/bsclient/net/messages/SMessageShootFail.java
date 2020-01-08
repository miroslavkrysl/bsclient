package cz.zcu.kiv.krysl.bsclient.net.messages;

public class SMessageShootFail extends ServerMessage {
    public SMessageShootFail() {
        super(ServerMessageKind.SHOOT_FAIL);
    }
}