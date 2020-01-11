package cz.zcu.kiv.krysl.bsclient.net.messages.client;

import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;
import cz.zcu.kiv.krysl.bsclient.net.types.Layout;

public class CMessageLayout extends ClientMessage {
    private final Layout layout;

    public CMessageLayout(Layout layout) {
        super(ClientMessageKind.LAYOUT);
        this.layout = layout;
    }

    public Layout getLayout() {
        return layout;
    }

    @Override
    protected void serializePayload(PayloadSerializer serializer) {
        layout.serialize(serializer);
    }
}