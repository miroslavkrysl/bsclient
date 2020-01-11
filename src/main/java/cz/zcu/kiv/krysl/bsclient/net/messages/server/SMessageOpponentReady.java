package cz.zcu.kiv.krysl.bsclient.net.messages.server;

public class SMessageOpponentReady extends ServerMessage {

    public SMessageOpponentReady() {
        super(ServerMessageKind.OPPONENT_READY);
    }
}
