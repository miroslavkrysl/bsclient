package cz.zcu.kiv.krysl.bsclient.net.message.client;

import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;
import cz.zcu.kiv.krysl.bsclient.net.codec.MessageSerializer;

public abstract class ClientMessage {
    private final ClientMessageKind kind;

    public ClientMessage(ClientMessageKind kind) {
        this.kind = kind;
    }

    public final ClientMessageKind getKind() {
        return kind;
    }

    public final String serialize() {
        MessageSerializer serializer = new MessageSerializer(kind);
        serializePayload(serializer.getPayloadSerializer());

        return serializer.serialize();
    }

    protected void serializePayload(PayloadSerializer serializer) {
        // no payload by default
    }
}
