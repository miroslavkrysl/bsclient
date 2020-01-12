package cz.zcu.kiv.krysl.bsclient.net.messages.client;

public class CMessageLeaveGame extends ClientMessage {

    public CMessageLeaveGame() {
        super(ClientMessageKind.LEAVE_GAME);
    }
}
