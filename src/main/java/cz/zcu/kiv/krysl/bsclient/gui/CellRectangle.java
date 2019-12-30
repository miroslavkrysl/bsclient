package cz.zcu.kiv.krysl.bsclient.gui;

import cz.zcu.kiv.krysl.bsclient.game.Cell;
import javafx.beans.binding.Bindings;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class CellRectangle extends Rectangle {

    private Cell cell;

    public CellRectangle(Cell cell) {
        this.cell = cell;
        initAppearance();
    }

    private void initAppearance() {
        fillProperty().bind(Bindings.createObjectBinding(
                () -> getColorByState(cell.getState()),
                cell.stateProperty())
        );
        rect.strokeProperty().bind(Bindings.createObjectBinding(
                () -> {
                    switch (field.getState()) {
                        case EMPTY:
                            return Color.LIGHTGRAY;
                        case MARKED_EMPTY:
                            return Color.DEEPSKYBLUE;
                        case SHIP:
                            return Color.DIMGRAY;
                        case MARKED_SHIP:
                            return Color.ORANGE;
                        case SUNK_SHIP:
                            return Color.CRIMSON;
                        case MORE_SHIPS:
                            return Color.BLACK;
                        default:
                            return Color.WHITE;
                    }
                },
                field.stateProperty())
        );

        rect.setStrokeWidth(1);
        rect.setStrokeType(StrokeType.INSIDE);
    }

    private Object getColorByType(Ce) {
        switch (state) {
            case EMPTY:
                return Color.WHITE;
            case MARKED_EMPTY:
                return Color.LIGHTBLUE;
            case SHIP:
                return Color.GRAY;
            case MARKED_SHIP:
                return Color.GOLD;
            case SUNK_SHIP:
                return Color.LIGHTCORAL;
            case MORE_SHIPS:
                return Color.BLACK;
        }
    }
}
