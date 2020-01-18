package cz.zcu.kiv.krysl.bsclient.gui;

import cz.zcu.kiv.krysl.bsclient.App;
import cz.zcu.kiv.krysl.bsclient.net.types.*;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class GameScreenPane extends BorderPane {
    private BoardPane playerBoard;
    private BoardPane opponentBoard;
    private App app;
    private Nickname opponent;
    private Layout layout;

    public GameScreenPane(App app, Nickname opponent, Layout layout, boolean onTurn) {
        this.app = app;
        this.opponent = opponent;

        createUi();

        this.playerBoard.markShips(layout.getPlacements(), false);
    }

    public GameScreenPane(App app, RestoreStateGame restoreState) {
        this.app = app;
        this.opponent = restoreState.getOpponent();

        createUi();

        this.playerBoard.markShips(restoreState.getLayout().getPlacements(), false);

        for (Position position : restoreState.getPlayerBoard().getPositions()) {
            BoardCellRectangle cell = playerBoard.getCell(position);

            ShipKind kind = cell.getShipKind();
            if (kind == null) {
                cell.mark(null, true);
            }
            else {
                cell.mark(kind, true);
            }
        }

        for (Position position : restoreState.getOpponentBoard().getPositions()) {
            BoardCellRectangle cell = opponentBoard.getCell(position);
            cell.mark(null, true);
        }

        this.opponentBoard.markShips(restoreState.getSunkShips().getPlacements(), true);
    }

    private void createUi() {
        this.playerBoard = new BoardPane();
        this.opponentBoard = new BoardPane();

        HBox boardsHBox = new HBox(playerBoard, opponentBoard);
        boardsHBox.setSpacing(30);
        boardsHBox.setAlignment(Pos.CENTER);

        setCenter(boardsHBox);
    }


}
