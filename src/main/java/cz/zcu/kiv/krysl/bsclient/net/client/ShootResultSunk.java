package cz.zcu.kiv.krysl.bsclient.net.client;

import cz.zcu.kiv.krysl.bsclient.net.types.Placement;

public class ShootResultSunk extends ShootResult{
    private Placement sunkShip;

    public ShootResultSunk(Placement sunkShip) {
        this.sunkShip = sunkShip;
    }

    public Placement getSunkShip() {
        return sunkShip;
    }
}
