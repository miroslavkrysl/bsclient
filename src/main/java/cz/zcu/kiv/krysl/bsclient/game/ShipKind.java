package cz.zcu.kiv.krysl.bsclient.game;

public enum ShipKind {
    AIRCRAFT_CARRIER(5),
    BATTLESHIP(4),
    CRUISER(3),
    DESTROYER(2),
    PATROL_BOAT(1);

    private int cellCount;

    ShipKind(int i) {
        cellCount = i;
    }

    public int getCellCount() {
        return cellCount;
    }

    public void setCellCount(int cellCount) {
        this.cellCount = cellCount;
    }

    public Position[] getPositions(Placement from) {
        Position incrementer = from.getOrientation().getIncrementer();
        Position[] positions = new Position[cellCount];

        positions[0] = from.getPosition();

        for (int i = 1; i < cellCount; i++) {
            positions[i] = positions[i - 1].add(incrementer);
        }

        return positions;
    }
}
