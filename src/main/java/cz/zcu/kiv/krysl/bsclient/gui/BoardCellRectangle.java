package cz.zcu.kiv.krysl.bsclient.gui;

import cz.zcu.kiv.krysl.bsclient.net.types.Position;
import cz.zcu.kiv.krysl.bsclient.net.types.ShipKind;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class BoardCellRectangle extends Rectangle {
    private ShipKind shipKind;

//    public enum State {
//        EMPTY,
//        HIT,
//        HIT_SHIP,
//        SHIP
//    }

    private Position position;
    private boolean hit;

    public BoardCellRectangle(BoardPane board, Position position) {
        this.position = position;

        setStrokeWidth(1);
        setStrokeType(StrokeType.INSIDE);
        mark(null, false);

        widthProperty().bind(board.widthProperty().divide(10));
        heightProperty().bind(board.heightProperty().divide(10));
        layoutXProperty().bind(board.widthProperty().divide(10).multiply(position.getCol()));
        layoutYProperty().bind(board.heightProperty().divide(10).multiply(position.getRow()));
    }

    public void mark(ShipKind shipKind, boolean hit) {
        this.shipKind = shipKind;
        this.hit = hit;

        if (shipKind == null) {
            if (hit) {
                setFill(Color.LIGHTCORAL);
                setStroke(Color.LIGHTGRAY);
            }
            else {
                setFill(Color.WHITE);
                setStroke(Color.LIGHTGRAY);
            }
        }
        else {
            if (hit) {
                setFill(Color.LIGHTCORAL);
                setStroke(Color.LIGHTCORAL);
            }
            else {
                setFill(Color.GREEN);
                setStroke(Color.GREEN);
            }
        }
    }

    public ShipKind getShipKind() {
        return this.shipKind;
    }

    public boolean isHit() {
        return this.hit;
    }

    public Position getPosition() {
        return position;
    }
}
