package cz.zcu.kiv.krysl.bsclient.gui;

import cz.zcu.kiv.krysl.bsclient.net.types.Layout;
import cz.zcu.kiv.krysl.bsclient.net.types.Orientation;
import cz.zcu.kiv.krysl.bsclient.net.types.Position;
import cz.zcu.kiv.krysl.bsclient.net.types.ShipKind;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;

public class LayoutBoardPane extends BoardPane {
    private Layout layout;
    private ShipKind dragging;

    public LayoutBoardPane(Layout layout) {
        super();
        this.layout = layout;

        markShips(layout.getPlacements(), false);
        bindUi();
    }

    private void bindUi() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                cells[i][j].setOnMouseEntered(event -> {
                    BoardCellRectangle cell = (BoardCellRectangle) event.getSource();

                    if (cell.getShipKind() != null) {
                        setCursor(Cursor.OPEN_HAND);
                    }
                });
                cells[i][j].setOnMouseExited(event -> {
                    BoardCellRectangle cell = (BoardCellRectangle) event.getSource();
                    setCursor(Cursor.DEFAULT);
                });
                cells[i][j].setOnMousePressed(event -> {
                    if (event.getButton().equals(MouseButton.PRIMARY)) {
                        BoardCellRectangle cell = (BoardCellRectangle) event.getSource();
                        dragging = cell.getShipKind();
                    }
                });
                cells[i][j].setOnMouseReleased(event -> {
                    if (event.getButton().equals(MouseButton.PRIMARY)) {
                        dragging = null;
                    }
                });
                cells[i][j].setOnMouseDragged(event -> {
                    if (dragging == null) {
                        return;
                    }

                    setCursor(Cursor.CLOSED_HAND);

                    BoardCellRectangle cell = (BoardCellRectangle) event.getSource();

                    int col = (int)Math.floor(((cell.getLayoutX() + event.getX()) / size) * 10);
                    int row = (int)Math.floor(((cell.getLayoutY() + event.getY()) / size) * 10);

                    if (col < 0) {
                        col = 0;
                    }
                    if (col > 9) {
                        col = 9;
                    }
                    if (row < 0) {
                        row = 0;
                    }
                    if (row > 9) {
                        row = 9;
                    }

                    this.layout.getPlacement(dragging).setPosition(new Position(row, col));
                    clear();
                    markShips(layout.getPlacements(), false);
                });
                cells[i][j].setOnDragExited(event -> {
                    BoardCellRectangle cell = (BoardCellRectangle) event.getSource();

                    setCursor(Cursor.DEFAULT);
                });
                cells[i][j].setOnMouseClicked(event -> {
                    if (event.getButton().equals(MouseButton.SECONDARY)) {
                        BoardCellRectangle cell = (BoardCellRectangle) event.getSource();

                        ShipKind kind = cell.getShipKind();

                        if (kind == null) {
                            return;
                        }

                        Orientation orientation = this.layout.getPlacement(kind).getOrientation();
                        this.layout.getPlacement(kind).setOrientation(orientation.rotateRight());
                        clear();
                        markShips(layout.getPlacements(), false);
                    }
                });
            }
        }
    }
}
