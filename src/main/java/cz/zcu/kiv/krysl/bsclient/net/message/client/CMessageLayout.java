package cz.zcu.kiv.krysl.bsclient.net.message.client;

import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;
import cz.zcu.kiv.krysl.bsclient.net.message.ClientMessageKind;
import cz.zcu.kiv.krysl.bsclient.net.message.items.Layout;

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