package cz.zcu.kiv.krysl.bsclient.game;

public enum Orientation {
    EAST,
    NORTH,
    WEST,
    SOUTH;

    public Orientation rotateLeft() {
        switch (this) {
            case EAST:
                return NORTH;
            case NORTH:
                return WEST;
            case WEST:
                return SOUTH;
            case SOUTH:
                return EAST;
        }

        throw new AssertionError("Unknown Orientation " + this);
    }

    public Position getIncrementer() {
        switch (this) {
            case EAST:
                return new Position(0, 1);
            case NORTH:
                return new Position(-1, 0);
            case WEST:
                return new Position(0, -1);
            case SOUTH:
                return new Position(1, 0);
        }

        throw new AssertionError("Unknown Orientation " + this);
    }
}
