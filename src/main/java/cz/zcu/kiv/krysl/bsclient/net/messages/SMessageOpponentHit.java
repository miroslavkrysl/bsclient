package cz.zcu.kiv.krysl.bsclient.net.messages;

public class SMessageOpponentHit extends ServerMessage {
    public SMessageOpponentHit() {
        super(ServerMessageKind.OPPONENT_HIT);
    }
}