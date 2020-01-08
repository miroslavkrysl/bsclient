package cz.zcu.kiv.krysl.bsclient.net.messages;

public class SMessageJoinGameJoined extends ServerMessage {
    public SMessageJoinGameJoined() {
        super(ServerMessageKind.JOIN_GAME_JOINED);
    }
}