package cz.zcu.kiv.krysl.bsclient.gui;

import cz.zcu.kiv.krysl.bsclient.App;
import cz.zcu.kiv.krysl.bsclient.net.client.IClientEventHandler;
import cz.zcu.kiv.krysl.bsclient.net.types.Layout;
import cz.zcu.kiv.krysl.bsclient.net.types.Nickname;
import cz.zcu.kiv.krysl.bsclient.net.types.Position;
import cz.zcu.kiv.krysl.bsclient.net.types.Who;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;


public class LayoutScreenPane extends BorderPane implements IClientEventHandler {

    private App app;
    private final Layout layout;
    private BoardPane board;
    private Nickname opponent;
    private boolean onTurn;
    private Button leaveGameButton;
    private Button chooseLayoutButton;
    private boolean ready;
    private Label heading;

    public LayoutScreenPane(App app, Nickname opponent, boolean onTurn) {
        this.app = app;
        this.opponent = opponent;
        this.onTurn = onTurn;
        this.layout = Layout.createDefault();

        this.ready = false;

        app.getClient().setEventHandler(this);

        createUi();
        bindUi();
    }

    private void createUi() {
        setPadding(new Insets(10));

        Label heading = new Label("Choose layout");
        heading.setFont(Font.font(null, FontWeight.BOLD, 30));
        heading.setAlignment(Pos.CENTER);
        heading.setTextAlignment(TextAlignment.CENTER);
        heading.setMaxWidth(Double.MAX_VALUE);
        heading.setPadding(new Insets(0, 0, 10, 0));

        this.board = new LayoutBoardPane(this.layout);

        Button leaveGameButton = new Button("Leave game");
        Button chooseLayoutButton = new Button("Confirm the layout selection");

        HBox buttonHBox = new HBox(leaveGameButton, chooseLayoutButton);
        buttonHBox.setAlignment(Pos.CENTER);
        buttonHBox.setSpacing(20);

        setTop(heading);
        setCenter(this.board);
        setBottom(buttonHBox);

        this.leaveGameButton = leaveGameButton;
        this.chooseLayoutButton = chooseLayoutButton;
        this.heading = heading;
    }

    private void bindUi() {
        this.leaveGameButton.setOnAction(e -> {
            Task<Void> leaveGameTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    app.getClient().leaveGame();
                    return null;
                }
            };

            leaveGameButton.setDisable(true);
            chooseLayoutButton.setDisable(true);
            board.setDisable(true);

            leaveGameTask.setOnSucceeded(event -> {
                app.goToLobbyScreen();
            });

            new Thread(leaveGameTask).start();
        });

        this.chooseLayoutButton.setOnAction(e -> {
            Task<Boolean> chooseLayoutTask = new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    return app.getClient().chooseLayout(layout);
                }
            };

            leaveGameButton.setDisable(true);
            chooseLayoutButton.setDisable(true);
            board.setDisable(true);

            chooseLayoutTask.setOnSucceeded(event -> {
                Boolean isLayoutValid = (Boolean) event.getSource().getValue();

                if (isLayoutValid) {
                    if (!ready) {
                        leaveGameButton.setDisable(false);
                        ready = true;
                        heading.setText("Waiting for opponent to be ready ");
                    } else {
                        app.goToGameScreen(opponent, layout, onTurn);
                    }
                } else {
                    leaveGameButton.setDisable(false);
                    chooseLayoutButton.setDisable(false);
                    board.setDisable(false);

                    Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                    alert.setHeaderText("Layout is invalid!");
                    alert.showAndWait();
                }
            });

            new Thread(chooseLayoutTask).start();
        });
    }

    @Override
    public void handleOpponentJoined(Nickname nickname) {
        // should not happen
    }

    @Override
    public void handleOpponentReady() {
        Platform.runLater(() -> {
            if (!ready) {
                ready = true;
            } else {
                app.goToGameScreen(opponent, layout, onTurn);
            }
        });
    }

    @Override
    public void handleOpponentOffline() {
        // should not happen
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
        // should not happen
    }

    @Override
    public void handleOpponentHit(Position position) {
        // should not happen
    }

    @Override
    public void handleGameOver(Who winner) {
        // should not happen
    }
}
