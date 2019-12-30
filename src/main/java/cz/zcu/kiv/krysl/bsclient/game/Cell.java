package cz.zcu.kiv.krysl.bsclient.game;

public class Cell {
    private boolean marked;
    private Ship ship;

    public Cell() {
        this.marked = false;
        this.ship = null;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }
}
