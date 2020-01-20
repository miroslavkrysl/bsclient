package cz.zcu.kiv.krysl.bsclient.gui;

import cz.zcu.kiv.krysl.bsclient.App;
import cz.zcu.kiv.krysl.bsclient.net.client.Client;
import cz.zcu.kiv.krysl.bsclient.net.types.Nickname;
import cz.zcu.kiv.krysl.bsclient.net.types.RestoreState;
import cz.zcu.kiv.krysl.bsclient.net.types.RestoreStateGame;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.net.InetSocketAddress;

public class LoginScreenPane extends BorderPane {

    private Stage stage;
    private App app;

    private TextField addressTextField;
    private PortTextField portTextField;
    private TextField nicknameTextField;
    private Button connectButton;
    private ProgressIndicator connectProgressIndicator;
    private Alert alert;

    public LoginScreenPane(Stage stage) {
        this.stage = stage;

        createUi();
        bindUi();
    }

    private void createUi() {
        setPadding(new Insets(10));

        // --- Heading ---
        HeadingLabel heading = new HeadingLabel();

        // --- Connect form ---
        VBox connectVBox = new VBox();
        connectVBox.setFillWidth(false);
        connectVBox.setAlignment(Pos.CENTER);
        connectVBox.setSpacing(10);

        // connect heading
        Label connectLabel = new Label("Connect to a server");
        connectLabel.setFont(Font.font(null, FontWeight.BOLD, 16));

        // address
        VBox addressVBox = new VBox();
        addressVBox.setAlignment(Pos.CENTER);
        Label addressLabel = new Label("Server address");
        TextField addressTextField = new TextField("localhost");
        addressTextField.setAlignment(Pos.CENTER);
        addressVBox.getChildren().add(addressLabel);
        addressVBox.getChildren().add(addressTextField);

        // port
        VBox portVBox = new VBox();
        portVBox.setAlignment(Pos.CENTER);
        Label portLabel = new Label("Port");
        PortTextField portTextField = new PortTextField(10000);
        portTextField.setAlignment(Pos.CENTER);
        portVBox.getChildren().add(portLabel);
        portVBox.getChildren().add(portTextField);

        // nickname
        VBox nicknameVBox = new VBox();
        nicknameVBox.setAlignment(Pos.CENTER);
        Label nicknameLabel = new Label("Nickname");
        TextField nicknameTextField = new TextField("nickname");
        nicknameTextField.setAlignment(Pos.CENTER);
        nicknameVBox.getChildren().add(nicknameLabel);
        nicknameVBox.getChildren().add(nicknameTextField);

        // connect button
        Button connectButton = new Button("Connect");
        ProgressIndicator connectProgressIndicator = new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS);
        connectProgressIndicator.setMaxSize(30, 30);
        connectProgressIndicator.setVisible(false);

        // add all to connectVBox
        connectVBox.getChildren().add(connectLabel);
        connectVBox.getChildren().add(addressVBox);
        connectVBox.getChildren().add(portVBox);
        connectVBox.getChildren().add(nicknameVBox);
        connectVBox.getChildren().add(connectButton);
        connectVBox.getChildren().add(connectProgressIndicator);

        setTop(heading);
        setCenter(connectVBox);

        this.alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
        this.alert.setHeaderText("Can't connect");
        this.alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        this.addressTextField = addressTextField;
        this.portTextField = portTextField;
        this.nicknameTextField = nicknameTextField;
        this.connectButton = connectButton;
        this.connectProgressIndicator = connectProgressIndicator;
    }

    private void bindUi() {
        // --- Connect button
        connectButton.setOnAction(e -> {
            Integer port = portTextField.getPort();
            String address = addressTextField.getText();

            if (port == null) {
                alert.setContentText("No port given.");
                alert.showAndWait();
                return;
            }

            Nickname nickname;
            try {
                nickname = new Nickname(nicknameTextField.getText());
            } catch (IllegalArgumentException exception) {
                alert.setContentText(exception.getMessage());
                alert.showAndWait();
                return;
            }

            InetSocketAddress socketAddress = new InetSocketAddress(address, port);

            Task<Client> connectTask = new Task<>() {
                @Override
                protected Client call() throws Exception {
                    return new Client(socketAddress, nickname);
                }
            };

            connectProgressIndicator.setVisible(true);
            connectButton.setDisable(true);

            LoginScreenPane loginPane = this;
            connectTask.setOnSucceeded(event -> {
                Client client = (Client) event.getSource().getValue();

                this.app = new App(stage, this, client);
                client.setDisconnectionHandlerHandler(app);

                RestoreState restoreState = client.getRestoreState();

                if (restoreState == null || restoreState.isLobby()) {
                    this.app.goToLobbyScreen();
                }
                else {
                    this.app.goToGameScreen((RestoreStateGame) restoreState);
                }

                connectProgressIndicator.setVisible(false);
                connectButton.setDisable(false);
            });

            connectTask.setOnFailed(event -> {
                alert.setContentText(event.getSource().getException().getMessage());
                alert.showAndWait();

                connectProgressIndicator.setVisible(false);
                connectButton.setDisable(false);
            });

            new Thread(connectTask).start();
        });
    }
}
