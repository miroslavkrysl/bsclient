package cz.zcu.kiv.krysl.bsclient.net.message.client;

public class CMessageJoinGame extends ClientMessage {
    public CMessageJoinGame() {
        super(ClientMessageKind.JOIN_GAME);
    }
}