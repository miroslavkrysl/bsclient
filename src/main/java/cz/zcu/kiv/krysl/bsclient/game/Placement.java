package cz.zcu.kiv.krysl.bsclient.game;

public class Placement {
    private Position position;
    private Orientation orientation;

    public Placement(Position position, Orientation orientation) {
        this.position = position;
        this.orientation = orientation;
    }

    public Position getPosition() {
        return position;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void rotateLeft() {
        orientation = orientation.rotateLeft();
    }
}
