package cz.zcu.kiv.krysl.bsclient.net.messages;

public class SMessageOpponentLeft extends ServerMessage {
    public SMessageOpponentLeft() {
        super(ServerMessageKind.OPPONENT_LEFT);
    }
}