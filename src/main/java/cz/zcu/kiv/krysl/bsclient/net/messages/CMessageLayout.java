package cz.zcu.kiv.krysl.bsclient.net.messages;

import cz.zcu.kiv.krysl.bsclient.net.types.Layout;

public class CMessageLayout extends ClientMessage {
    private final Layout layout;

    public CMessageLayout(Layout layout) {
        super(ClientMessageKind.LAYOUT);
        this.layout = layout;
    }
}