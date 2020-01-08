package cz.zcu.kiv.krysl.bsclient.net.messages;

public class SMessageOpponentReady extends ServerMessage {
    public SMessageOpponentReady() {
        super(ServerMessageKind.OPPONENT_READY);
    }
}