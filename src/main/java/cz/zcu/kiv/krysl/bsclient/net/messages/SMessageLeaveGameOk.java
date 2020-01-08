package cz.zcu.kiv.krysl.bsclient.net.messages;

public class SMessageLeaveGameOk extends ServerMessage {
    public SMessageLeaveGameOk() {
        super(ServerMessageKind.LEAVE_GAME_OK);
    }
}