package cz.zcu.kiv.krysl.bsclient.net.client.results;

import cz.zcu.kiv.krysl.bsclient.net.types.Placement;
import cz.zcu.kiv.krysl.bsclient.net.types.ShipKind;

public class ShootResultSunk extends ShootResult{
    private ShipKind shipKind;
    private Placement placement;

    public ShootResultSunk(ShipKind shipKind, Placement placement) {
        this.shipKind = shipKind;
        this.placement = placement;
    }

    public ShipKind getShipKind() {
        return shipKind;
    }

    public Placement getPlacement() {
        return placement;
    }
}
