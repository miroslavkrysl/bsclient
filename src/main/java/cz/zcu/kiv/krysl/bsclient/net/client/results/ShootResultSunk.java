package cz.zcu.kiv.krysl.bsclient.net.client.results;

import cz.zcu.kiv.krysl.bsclient.net.types.Placement;
import cz.zcu.kiv.krysl.bsclient.net.types.ShipKind;

public class ShootResultSunk extends ShootResult{
    private ShipKind shipKind;
    private Placement sunkShip;

    public ShootResultSunk(ShipKind shipKind, Placement sunkShip) {
        this.shipKind = shipKind;
        this.sunkShip = sunkShip;
    }

    public ShipKind getShipKind() {
        return shipKind;
    }

    public Placement getSunkShip() {
        return sunkShip;
    }
}
