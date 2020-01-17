package cz.zcu.kiv.krysl.bsclient.net.messages.server;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.types.Placement;
import cz.zcu.kiv.krysl.bsclient.net.types.ShipKind;

public class SMessageShootSunk extends ServerMessage {

    private ShipKind shipId;
    private final Placement placement;

    public SMessageShootSunk(ShipKind shipId, Placement placement) {
        super(ServerMessageKind.SHOOT_SUNK);
        this.shipId = shipId;
        this.placement = placement;
    }

    public ShipKind getShipKind() {
        return shipId;
    }

    public Placement getPlacement() {
        return placement;
    }

    public static SMessageShootSunk deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        ShipKind shipId = ShipKind.deserialize(deserializer);
        Placement placement = Placement.deserialize(deserializer);
        return new SMessageShootSunk(shipId, placement);
    }
}
