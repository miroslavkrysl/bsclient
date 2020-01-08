package cz.zcu.kiv.krysl.bsclient.net.messages;

public class SMessageGameOver extends ServerMessage {
    public SMessageGameOver() {
        super(ServerMessageKind.GAME_OVER);
    }
}