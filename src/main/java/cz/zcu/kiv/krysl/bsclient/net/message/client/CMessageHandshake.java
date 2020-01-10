package cz.zcu.kiv.krysl.bsclient.net.message.client;

import cz.zcu.kiv.krysl.bsclient.net.message.ClientMessageKind;

public class CMessageHandshake extends ClientMessage {
    public CMessageHandshake() {
        super(ClientMessageKind.HANDSHAKE);
    }
}
