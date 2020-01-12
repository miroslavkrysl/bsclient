package cz.zcu.kiv.krysl.bsclient.net.client;

public class ShootResult {
    public boolean isHit() {
        return this instanceof ShootResultHit;
    }

    public boolean isMiss() {
        return this instanceof ShootResultMiss;
    }

    public boolean isSunk() {
        return this instanceof ShootResultSunk;
    }
}
