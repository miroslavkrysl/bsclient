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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GameScreenPane extends BorderPane implements IClientEventHandler {
    private BoardPane playerBoard;
    private BoardPane opponentBoard;
    private App app;
    private Nickname opponent;
    private BooleanProperty onTurn;
    private Label onTurnOpponentLabel;
    private Label onTurnPlayerLabel;
    private Label opponentOfflineLabel;
    private Button leaveGameButton;

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
        app.getClient().setEventHandler(this);

        createUi();
        bindUi();

        this.playerBoard.markShips(restoreState.getLayout().getPlacements(), false);

        // mark misses on player board
        for (Position position : restoreState.getPlayerBoardHits().getPositions()) {
            BoardCellRectangle cell = playerBoard.getCell(position);
            cell.markShoot(true);
        }

        // mark hits on player board
        for (Position position : restoreState.getPlayerBoardMisses().getPositions()) {
            BoardCellRectangle cell = playerBoard.getCell(position);
            cell.markShoot(false);
        }

        // mark opponent board hits
        for (Position position : restoreState.getOpponentBoardHits().getPositions()) {
            BoardCellRectangle cell = opponentBoard.getCell(position);
            cell.markShoot(true);
        }

        // mark opponent board misses
        for (Position position : restoreState.getOpponentBoardMisses().getPositions()) {
            BoardCellRectangle cell = opponentBoard.getCell(position);
            cell.markShoot(false);
        }

        this.opponentBoard.markShips(restoreState.getSunkShips().getPlacements(), true);
    }

    private void createUi() {
        BoardPane playerBoard = new BoardPane();
        BoardPane opponentBoard = new BoardPane();

        Label opponentLabel = new Label(opponent.getValue());
        opponentLabel.setFont(Font.font(20));
        Label playerLabel = new Label(app.getClient().getNickname().getValue());
        playerLabel.setFont(Font.font(20));

        Label onTurnPlayerLabel = new Label("ON TURN");
        Label onTurnOpponentLabel = new Label("ON TURN");
        Label opponentOfflineLabel = new Label("OFFLINE");
        opponentOfflineLabel.setTextFill(Color.FIREBRICK);
        opponentOfflineLabel.setManaged(false);
        opponentOfflineLabel.setVisible(false);

        VBox playerBox = new VBox(playerLabel, onTurnPlayerLabel, playerBoard);
        playerBox.setSpacing(10);
        playerBox.setAlignment(Pos.CENTER);

        HBox opponentStateBox = new HBox(onTurnOpponentLabel, opponentOfflineLabel);
        opponentStateBox.setAlignment(Pos.CENTER);
        opponentStateBox.setSpacing(10);
        VBox opponentBox = new VBox(opponentLabel, opponentStateBox, opponentBoard);
        opponentBox.setSpacing(10);
        opponentBox.setAlignment(Pos.CENTER);

        Button leaveGameButton = new Button("Leave game");
        HBox buttonBox = new HBox(leaveGameButton);
        buttonBox.setAlignment(Pos.CENTER);

        HBox boardsBox = new HBox(playerBox, opponentBox);
        boardsBox.setSpacing(30);
        boardsBox.setAlignment(Pos.CENTER);

        VBox centerBox = new VBox(boardsBox, buttonBox);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);

        setCenter(centerBox);

        this.playerBoard = playerBoard;
        this.opponentBoard = opponentBoard;
        this.onTurnPlayerLabel = onTurnPlayerLabel;
        this.onTurnOpponentLabel = onTurnOpponentLabel;
        this.opponentOfflineLabel = opponentOfflineLabel;
        this.leaveGameButton = leaveGameButton;
    }

    private void bindUi() {
        this.opponentBoard.disableProperty().bind(onTurn.not());
        this.onTurnPlayerLabel.visibleProperty().bind(onTurn);
        this.onTurnOpponentLabel.visibleProperty().bind(onTurn.not());

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
                            clickedCell.markShoot(true);
                        }
                        else if (result.isSunk()) {
                            ShootResultSunk resultSunk = (ShootResultSunk) result;
                            opponentBoard.markShip(resultSunk.getShipKind(), resultSunk.getPlacement(), true);
                        }
                        else {
                            onTurn.set(false);
                            clickedCell.markShoot(false);
                        }
                    });

                    new Thread(shootTask).start();
                });
            }
        }

        this.leaveGameButton.setOnAction(e -> {
            Task<Void> leaveGameTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    app.getClient().leaveGame();
                    return null;
                }
            };

            leaveGameButton.setDisable(true);

            leaveGameTask.setOnSucceeded(event -> {
                app.goToLobbyScreen();
            });

            leaveGameTask.setOnFailed(event -> {
                leaveGameButton.setDisable(false);
            });

            new Thread(leaveGameTask).start();
        });
    }

    @Override
    public void handleOpponentJoined(Nickname opponent) {
        // should not happen
    }

    @Override
    public void handleOpponentReady() {
        Platform.runLater(() -> {
            opponentOfflineLabel.setManaged(false);
            opponentOfflineLabel.setVisible(false);
        });
    }

    @Override
    public void handleOpponentOffline() {
        Platform.runLater(() -> {
            opponentOfflineLabel.setManaged(true);
            opponentOfflineLabel.setVisible(true);
        });
    }

    @Override
    public void handleOpponentLeft() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Opponent left the game", ButtonType.OK);
            alert.setHeaderText("Game ended");
            alert.showAndWait();
            app.goToLobbyScreen();
        });
    }

    @Override
    public void handleOpponentMissed(Position position) {
        Platform.runLater(() -> {
            playerBoard.getCell(position).markShoot(false);
            onTurn.set(true);
        });
    }

    @Override
    public void handleOpponentHit(Position position) {
        Platform.runLater(() -> {
            playerBoard.getCell(position).markShoot(true);
        });
    }

    @Override
    public void handleGameOver(Who winner) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Game is over", ButtonType.OK);
            alert.setHeaderText("WINNER: " + (winner == Who.YOU ? app.getClient().getNickname().getValue() : opponent.getValue()));
            alert.showAndWait();
            app.goToLobbyScreen();
        });
    }
}
