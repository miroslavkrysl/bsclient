package cz.zcu.kiv.krysl.bsclient.net.messages;

public class SMessageOpponentMissed extends ServerMessage {
    public SMessageOpponentMissed() {
        super(ServerMessageKind.OPPONENT_MISSED);
    }
}