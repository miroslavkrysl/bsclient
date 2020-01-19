package cz.zcu.kiv.krysl.bsclient.gui;

import cz.zcu.kiv.krysl.bsclient.net.types.Placement;
import cz.zcu.kiv.krysl.bsclient.net.types.Position;
import cz.zcu.kiv.krysl.bsclient.net.types.ShipKind;
import javafx.scene.layout.Pane;

import java.util.Map;

public class BoardPane extends Pane {
    protected BoardCellRectangle[][] cells;
    protected static final double SIZE = 400;
    protected final double size;

    public BoardPane() {
        this.size = SIZE;
        createUi();
    }

    private void createUi() {
        this.cells = new BoardCellRectangle[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                cells[i][j] = new BoardCellRectangle(this, new Position(i, j));
                getChildren().add(cells[i][j]);
            }
        }

        setMinHeight(size);
        setMaxHeight(size);
        setMinWidth(size);
        setMaxWidth(size);
    }

    protected void clear() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                cells[i][j].mark(null);
            }
        }
    }

    protected BoardCellRectangle getCell(Position position) {
        return cells[position.getRow()][position.getCol()];
    }

    protected void markShips(Map<ShipKind, Placement> placements, boolean sunk) {
        for (Map.Entry<ShipKind, Placement> entry : placements.entrySet()){
            markShip(entry.getKey(), entry.getValue(), sunk);
        }
    }

    protected void markShip(ShipKind shipKind, Placement placement, boolean sunk) {
        int cells = shipKind.getNumberOfCells();
        int row = placement.getPosition().getRow();
        int col = placement.getPosition().getCol();

        int incR;
        int incC;

        switch (placement.getOrientation()) {
            case EAST:
                incR = 0;
                incC = 1;
                break;
            case NORTH:
                incR = -1;
                incC = 0;
                break;
            case WEST:
                incR = 0;
                incC = -1;
                break;
            case SOUTH:
                incR = 1;
                incC = 0;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + placement.getOrientation());
        }

        for (int i = 0; i < cells; i++) {
            if (col < 0 || col > 9 || row < 0 || row > 9) {
                continue;
            }

            if (sunk) {
                this.cells[row][col].markShoot(shipKind, true);
            }
            else {
                this.cells[row][col].mark(shipKind);
            }

            row += incR;
            col += incC;
        }
    }
}
