package cz.zcu.kiv.krysl.bsclient.net.message.client;

import cz.zcu.kiv.krysl.bsclient.net.message.ClientMessageKind;

public class CMessageJoinGame extends ClientMessage {
    public CMessageJoinGame() {
        super(ClientMessageKind.JOIN_GAME);
    }
}