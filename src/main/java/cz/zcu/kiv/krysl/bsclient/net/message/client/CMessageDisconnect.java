package cz.zcu.kiv.krysl.bsclient.net.message.client;

import cz.zcu.kiv.krysl.bsclient.net.message.ClientMessageKind;

public class CMessageDisconnect extends ClientMessage {
    public CMessageDisconnect() {
        super(ClientMessageKind.DISCONNECT);
    }
}