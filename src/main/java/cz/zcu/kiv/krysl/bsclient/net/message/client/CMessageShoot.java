package cz.zcu.kiv.krysl.bsclient.net.message.client;

import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;
import cz.zcu.kiv.krysl.bsclient.net.message.ClientMessageKind;
import cz.zcu.kiv.krysl.bsclient.net.message.items.Position;

public class CMessageShoot extends ClientMessage {
    private final Position position;

    public CMessageShoot(Position position) {
        super(ClientMessageKind.SHOOT);
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    protected void serializePayload(PayloadSerializer serializer) {
        position.serialize(serializer);
    }
}