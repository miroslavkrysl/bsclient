package cz.zcu.kiv.krysl.bsclient.gui;

import cz.zcu.kiv.krysl.bsclient.net.types.Position;
import cz.zcu.kiv.krysl.bsclient.net.types.ShipKind;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class BoardCellRectangle extends Rectangle {
    private ShipKind shipKind;

    private Position position;
    private boolean marked;

    public BoardCellRectangle(BoardPane board, Position position) {
        this.position = position;

        setStrokeWidth(1);
        setStrokeType(StrokeType.INSIDE);
        unMark();

        widthProperty().bind(board.widthProperty().divide(10));
        heightProperty().bind(board.heightProperty().divide(10));
        layoutXProperty().bind(board.widthProperty().divide(10).multiply(position.getCol()));
        layoutYProperty().bind(board.heightProperty().divide(10).multiply(position.getRow()));
    }

    public void unMark() {
        this.marked = false;
        this.shipKind = null;

        setFill(Color.WHITE);
        setStroke(Color.LIGHTGRAY);
    }

    public void markShoot(boolean hit) {
        this.marked = true;
        this.shipKind = null;

        if (hit) {
            setFill(Color.INDIANRED);
            setStroke(Color.LIGHTGRAY);
        } else {
            setFill(Color.CORNFLOWERBLUE);
            setStroke(Color.LIGHTGRAY);
        }
    }

    public void markShip(ShipKind shipKind, boolean sunk) {
        this.shipKind = shipKind;
        this.marked = true;

        if (sunk) {
            setFill(Color.INDIANRED);
            setStroke(Color.INDIANRED);
        }
        else {
            setFill(Color.GREEN);
            setStroke(Color.GREEN);
        }
    }

    public ShipKind getShipKind() {
        return this.shipKind;
    }

    public boolean isMarked() {
        return this.marked;
    }

    public Position getPosition() {
        return position;
    }
}
