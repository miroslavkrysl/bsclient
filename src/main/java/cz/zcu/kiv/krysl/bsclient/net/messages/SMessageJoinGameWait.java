package cz.zcu.kiv.krysl.bsclient.net.messages;

public class SMessageJoinGameWait extends ServerMessage {
    public SMessageJoinGameWait() {
        super(ServerMessageKind.JOIN_GAME_WAIT);
    }
}