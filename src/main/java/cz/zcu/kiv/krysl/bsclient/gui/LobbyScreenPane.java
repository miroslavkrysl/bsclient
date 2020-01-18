package cz.zcu.kiv.krysl.bsclient.gui;

import cz.zcu.kiv.krysl.bsclient.App;
import cz.zcu.kiv.krysl.bsclient.net.client.IClientEventHandler;
import cz.zcu.kiv.krysl.bsclient.net.types.Nickname;
import cz.zcu.kiv.krysl.bsclient.net.types.Position;
import cz.zcu.kiv.krysl.bsclient.net.types.Who;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LobbyScreenPane extends BorderPane implements IClientEventHandler {

    private App app;
    private boolean waitingForGame;
    private Button joinGameButton;
    private Button logoutButton;
    private ProgressIndicator progressIndicator;
    private Label addressValueLabel;
    private Label nicknameValueLabel;
    private Button stopWaitingButton;

    public LobbyScreenPane(App app) {
        this.app = app;
        this.waitingForGame = false;

        app.getClient().setEventHandler(this);

        createUi();
        bindUi();
    }

    private void createUi() {
        setPadding(new Insets(10));

        // --- Heading ---
        HeadingLabel heading = new HeadingLabel();

        // --- centerVBox ---
        VBox centerVBox = new VBox();
        centerVBox.setFillWidth(false);
        centerVBox.setAlignment(Pos.CENTER);
        centerVBox.setSpacing(10);

        // heading
        Label connectedLabel = new Label("Connected to the server");
        connectedLabel.setFont(Font.font(null, FontWeight.BOLD, 16));

        // address
        HBox addressHBox = new HBox();
        addressHBox.setAlignment(Pos.CENTER);
        addressHBox.setSpacing(10);
        Label addressLabel = new Label("Server address:");
        Label addressValueLabel = new Label();
        addressHBox.getChildren().add(addressLabel);
        addressHBox.getChildren().add(addressValueLabel);

        // nickname
        HBox nicknameHBox = new HBox();
        nicknameHBox.setAlignment(Pos.CENTER);
        nicknameHBox.setSpacing(10);
        Label nicknameLabel = new Label("Nickname:");
        Label nicknameValueLabel = new Label();
        nicknameHBox.getChildren().add(nicknameLabel);
        nicknameHBox.getChildren().add(nicknameValueLabel);

        // join game button
        Button joinGameButton = new Button("Join a game");

        // leave game button
        Button logoutButton = new Button("Logout");

        // stop waiting button
        Button stopWaitingButton = new Button("Stop waiting");
        stopWaitingButton.setVisible(false);

        // progress indicator
        ProgressIndicator progressIndicator = new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS);
        progressIndicator.setMaxSize(30, 30);
        progressIndicator.setVisible(false);

        // add all to connectVBox
        centerVBox.getChildren().add(connectedLabel);
        centerVBox.getChildren().add(addressHBox);
        centerVBox.getChildren().add(nicknameHBox);
        centerVBox.getChildren().add(joinGameButton);
        centerVBox.getChildren().add(logoutButton);
        centerVBox.getChildren().add(progressIndicator);
        centerVBox.getChildren().add(stopWaitingButton);

        setTop(heading);
        setCenter(centerVBox);


        this.addressValueLabel = addressValueLabel;
        this.nicknameValueLabel = nicknameValueLabel;
        this.joinGameButton = joinGameButton;
        this.logoutButton = logoutButton;
        this.stopWaitingButton = stopWaitingButton;
        this.progressIndicator = progressIndicator;
    }

    private void bindUi() {
        addressValueLabel.setText(app.getClient().getServerAddress().toString());
        nicknameValueLabel.setText(app.getClient().getNickname().getValue());

        // --- Logout button ---
        logoutButton.setOnAction(e -> {

            Task<Void> logoutTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    app.getClient().disconnect();
                    return null;
                }
            };

            progressIndicator.setVisible(true);
            joinGameButton.setDisable(true);
            logoutButton.setDisable(true);

            logoutTask.setOnSucceeded(event -> app.goToLoginScreen());

            new Thread(logoutTask).start();
        });

        // --- Join game button ---
        joinGameButton.setOnAction(e -> {

            Task<Nickname> joinGameTask = new Task<>() {
                @Override
                protected Nickname call() throws Exception {
                    return app.getClient().joinGame();
                }
            };

            progressIndicator.setVisible(true);
            joinGameButton.setDisable(true);
            logoutButton.setDisable(true);

            joinGameTask.setOnSucceeded(event -> {
                Nickname opponent = (Nickname) event.getSource().getValue();

                if (opponent == null) {
                    waitingForGame = true;
                    stopWaitingButton.setDisable(false);
                    stopWaitingButton.setVisible(true);
                    return;
                }

                app.goToLayoutScreen(opponent, false);
            });

            new Thread(joinGameTask).start();
        });

        // --- Stop waiting button ---
        stopWaitingButton.setOnAction(e -> {

            Task<Void> stopWaitingTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    app.getClient().leaveGame();
                    return null;
                }
            };

            stopWaitingButton.setDisable(true);

            stopWaitingTask.setOnSucceeded(event -> {
                waitingForGame = false;
                progressIndicator.setVisible(false);
                joinGameButton.setDisable(false);
                logoutButton.setDisable(false);
                stopWaitingButton.setDisable(false);
                stopWaitingButton.setVisible(false);
            });

            new Thread(stopWaitingTask).start();
        });
    }

    @Override
    public void handleOpponentJoined(Nickname opponent) {
        if (waitingForGame) {
            waitingForGame = false;

            Platform.runLater(() -> app.goToLayoutScreen(opponent, true));
        }
    }

    @Override
    public void handleOpponentReady() {
        // should not happen
    }

    @Override
    public void handleOpponentOffline() {
        // should not happen
    }

    @Override
    public void handleOpponentLeft() {
        // should not happen
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
