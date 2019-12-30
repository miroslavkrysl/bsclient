package cz.zcu.kiv.krysl.bsclient.game;

import java.util.stream.Stream;

public class Ship {
    private ShipKind kind;
    private int health;
    private Cell[] cells;


    public Ship(ShipKind kind) {
        this.kind = kind;
        this.health = kind.getCellCount();
        this.cells = Stream.generate(Cell::new)
                .limit(kind.getCellCount())
                .toArray(Cell[]::new);
    }

    public void hit() {
        if (health > 0) {
            health -= 1;
        }
    }

    public boolean isSunk() {
        return health == 0;
    }

    public ShipKind getKind() {
        return kind;
    }

    public Position[] getPositions(Placement from) {
        return this.kind.getPositions(from);
    }
}
