package cz.zcu.kiv.krysl.bsclient.net.messages.client;

import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;
import cz.zcu.kiv.krysl.bsclient.net.types.Position;

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