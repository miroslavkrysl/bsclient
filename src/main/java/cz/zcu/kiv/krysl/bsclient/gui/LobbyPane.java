package cz.zcu.kiv.krysl.bsclient.gui;

import cz.zcu.kiv.krysl.bsclient.net.client.Client;
import cz.zcu.kiv.krysl.bsclient.net.client.ConnectionLossCause;
import cz.zcu.kiv.krysl.bsclient.net.client.IClientEventHandler;
import cz.zcu.kiv.krysl.bsclient.net.types.Nickname;
import cz.zcu.kiv.krysl.bsclient.net.types.Position;
import cz.zcu.kiv.krysl.bsclient.net.types.Who;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LobbyPane extends BorderPane implements IClientEventHandler {

    private Client client;
    private Button joinGameButton;
    private Button logoutButton;
    private ProgressIndicator progressIndicator;
    private LoginPane loginPane;
    private Alert disconnectedAlert;
    private Label addressValueLabel;
    private Label nicknameValueLabel;

    public LobbyPane(LoginPane loginPane, Client client) {
        this.loginPane = loginPane;
        this.client = client;
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

        setTop(heading);
        setCenter(centerVBox);

        this.disconnectedAlert = new Alert(Alert.AlertType.ERROR, "", new ButtonType("Go back", ButtonBar.ButtonData.OK_DONE));
        this.disconnectedAlert.setHeaderText("Network error");
        this.disconnectedAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);


        this.addressValueLabel = addressValueLabel;
        this.nicknameValueLabel = nicknameValueLabel;
        this.joinGameButton = joinGameButton;
        this.logoutButton = logoutButton;
        this.progressIndicator = progressIndicator;
    }

    private void bindUi() {
        addressValueLabel.setText(client.getServerAddress().toString());
        nicknameValueLabel.setText(client.getNickname().getValue());

        // --- Connect button
        logoutButton.setOnAction(e -> {

            Task<Void> logoutTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    client.disconnect();
                    return null;
                }
            };

            progressIndicator.setVisible(true);
            joinGameButton.setDisable(true);
            logoutButton.setDisable(true);

            logoutTask.setOnSucceeded(event -> {
                progressIndicator.setVisible(false);
                joinGameButton.setDisable(false);
                logoutButton.setDisable(false);
                getScene().setRoot(loginPane);
            });

            logoutTask.setOnFailed(event -> {
                disconnectedAlert.setContentText(event.getSource().getException().getMessage());
                disconnectedAlert.showAndWait();

                progressIndicator.setVisible(true);
                joinGameButton.setDisable(true);
                logoutButton.setDisable(true);
            });

            new Thread(logoutTask).start();
        });
    }

    @Override
    public void handleOpponentJoined(Nickname nickname) {

    }

    @Override
    public void handleOpponentReady() {

    }

    @Override
    public void handleOpponentLeft() {

    }

    @Override
    public void handleOpponentMissed(Position position) {

    }

    @Override
    public void handleOpponentHit(Position position) {

    }

    @Override
    public void handleGameOver(Who winner) {

    }

    @Override
    public void handleDisconnected(ConnectionLossCause cause) {
        System.out.println("disconnected: " + cause.toString());
    }
}
