package cz.zcu.kiv.krysl.bsclient.gui;

import cz.zcu.kiv.krysl.bsclient.App;
import cz.zcu.kiv.krysl.bsclient.net.client.IClientEventHandler;
import cz.zcu.kiv.krysl.bsclient.net.client.results.ShootResult;
import cz.zcu.kiv.krysl.bsclient.net.client.results.ShootResultSunk;
import cz.zcu.kiv.krysl.bsclient.net.types.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class GameScreenPane extends BorderPane implements IClientEventHandler {
    private BoardPane playerBoard;
    private BoardPane opponentBoard;
    private App app;
    private Nickname opponent;
    private BooleanProperty onTurn;
    private Layout layout;

    public GameScreenPane(App app, Nickname opponent, Layout layout, boolean onTurn) {
        this.app = app;
        this.opponent = opponent;
        this.onTurn = new SimpleBooleanProperty(onTurn);
        app.getClient().setEventHandler(this);

        createUi();
        bindUi();

        this.playerBoard.markShips(layout.getPlacements(), false);
    }

    public GameScreenPane(App app, RestoreStateGame restoreState) {
        this.app = app;
        this.opponent = restoreState.getOpponent();
        this.onTurn = new SimpleBooleanProperty(restoreState.getOnTurn() == Who.YOU);

        createUi();
        bindUi();

        this.playerBoard.markShips(restoreState.getLayout().getPlacements(), false);

        for (Position position : restoreState.getPlayerBoard().getPositions()) {
            BoardCellRectangle cell = playerBoard.getCell(position);

            ShipKind kind = cell.getShipKind();
            cell.markShoot(kind, true);
        }

        for (Position position : restoreState.getOpponentBoard().getPositions()) {
            BoardCellRectangle cell = opponentBoard.getCell(position);
            cell.markShoot(null, true);
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

    private void bindUi() {
        this.opponentBoard.disableProperty().bind(onTurn.not());

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                BoardCellRectangle cell = opponentBoard.getCell(new Position(i, j));

                cell.setOnMouseClicked(e -> {
                    BoardCellRectangle clickedCell = ((BoardCellRectangle) e.getSource());

                    if (clickedCell.isMarked()) {
                        return;
                    }

                    Position position = clickedCell.getPosition();

                    Task<ShootResult> shootTask = new Task<>() {
                        @Override
                        protected ShootResult call() throws Exception {
                            return app.getClient().shoot(position);
                        }
                    };

                    shootTask.setOnSucceeded(event -> {
                        ShootResult result = (ShootResult) event.getSource().getValue();

                        if (result.isHit()) {
                            clickedCell.markShoot(null, true);
                        }
                        else if (result.isSunk()) {
                            ShootResultSunk resultSunk = (ShootResultSunk) result;
                            opponentBoard.markShip(resultSunk.getShipKind(), resultSunk.getPlacement(), true);
                        }
                        else {
                            onTurn.set(false);
                            clickedCell.markShoot(null, false);
                        }
                    });

                    new Thread(shootTask).start();
                });
            }
        }
    }

    @Override
    public void handleOpponentJoined(Nickname opponent) {
        // should not happen
    }

    @Override
    public void handleOpponentReady() {
        // TODO:
    }

    @Override
    public void handleOpponentOffline() {
        // TODO:
    }

    @Override
    public void handleOpponentLeft() {
        // TODO:
    }

    @Override
    public void handleOpponentMissed(Position position) {
        System.out.println("Opp missed");
        Platform.runLater(() -> {
            playerBoard.getCell(position).markShoot(null, false);
            onTurn.set(true);
        });
    }

    @Override
    public void handleOpponentHit(Position position) {
        System.out.println("Opp hit");
        Platform.runLater(() -> {
            BoardCellRectangle cell = playerBoard.getCell(position);
            cell.markShoot(cell.getShipKind(), true);
        });
    }

    @Override
    public void handleGameOver(Who winner) {

    }
}
