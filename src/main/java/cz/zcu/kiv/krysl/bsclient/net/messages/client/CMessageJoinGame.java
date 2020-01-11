package cz.zcu.kiv.krysl.bsclient.net.messages.client;

public class CMessageJoinGame extends ClientMessage {
    public CMessageJoinGame() {
        super(ClientMessageKind.JOIN_GAME);
    }
}