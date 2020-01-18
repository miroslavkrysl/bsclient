package cz.zcu.kiv.krysl.bsclient.net.messages.server;

public class SMessageOpponentOffline extends ServerMessage {
    public SMessageOpponentOffline() {
        super(ServerMessageKind.OPPONENT_OFFLINE);
    }
}
