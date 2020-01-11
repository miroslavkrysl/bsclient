package cz.zcu.kiv.krysl.bsclient.net.message.server;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.message.items.Placement;
import cz.zcu.kiv.krysl.bsclient.net.message.items.ShipId;

public class SMessageShootSunk extends ServerMessage {

    private final Placement placement;

    public SMessageShootSunk(ShipId shipId, Placement placement) {
        super(ServerMessageKind.SHOOT_SUNK);
        this.placement = placement;
    }

    public static SMessageShootSunk deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        ShipId shipId = ShipId.deserialize(deserializer);
        Placement placement = Placement.deserialize(deserializer);
        return new SMessageShootSunk(shipId, placement);
    }

    public Placement getPlacement() {
        return placement;
    }
}
