package cz.zcu.kiv.krysl.bsclient.net.client.results;

import cz.zcu.kiv.krysl.bsclient.net.types.Placement;
import cz.zcu.kiv.krysl.bsclient.net.types.ShipId;

public class ShootResultSunk extends ShootResult{
    private ShipId shipId;
    private Placement sunkShip;

    public ShootResultSunk(ShipId shipId, Placement sunkShip) {
        this.shipId = shipId;
        this.sunkShip = sunkShip;
    }

    public ShipId getShipId() {
        return shipId;
    }

    public Placement getSunkShip() {
        return sunkShip;
    }
}
