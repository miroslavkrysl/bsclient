package cz.zcu.kiv.krysl.bsclient.net.message.client;

import cz.zcu.kiv.krysl.bsclient.net.message.ClientMessageKind;

public class CMessageAlive extends ClientMessage {
    public CMessageAlive() {
        super(ClientMessageKind.ALIVE);
    }
}
