package cz.zcu.kiv.krysl.bsclient.net.messages.server;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.types.Placement;
import cz.zcu.kiv.krysl.bsclient.net.types.ShipId;

public class SMessageShootSunk extends ServerMessage {

    private ShipId shipId;
    private final Placement placement;

    public SMessageShootSunk(ShipId shipId, Placement placement) {
        super(ServerMessageKind.SHOOT_SUNK);
        this.shipId = shipId;
        this.placement = placement;
    }

    public ShipId getShipId() {
        return shipId;
    }

    public Placement getPlacement() {
        return placement;
    }

    public static SMessageShootSunk deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        ShipId shipId = ShipId.deserialize(deserializer);
        Placement placement = Placement.deserialize(deserializer);
        return new SMessageShootSunk(shipId, placement);
    }
}
