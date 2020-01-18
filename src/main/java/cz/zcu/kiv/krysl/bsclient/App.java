package cz.zcu.kiv.krysl.bsclient;

import cz.zcu.kiv.krysl.bsclient.gui.GameScreenPane;
import cz.zcu.kiv.krysl.bsclient.gui.LayoutScreenPane;
import cz.zcu.kiv.krysl.bsclient.gui.LobbyScreenPane;
import cz.zcu.kiv.krysl.bsclient.gui.LoginScreenPane;
import cz.zcu.kiv.krysl.bsclient.net.DisconnectedException;
import cz.zcu.kiv.krysl.bsclient.net.client.*;
import cz.zcu.kiv.krysl.bsclient.net.types.Layout;
import cz.zcu.kiv.krysl.bsclient.net.types.Nickname;
import cz.zcu.kiv.krysl.bsclient.net.types.RestoreState;
import cz.zcu.kiv.krysl.bsclient.net.types.RestoreStateGame;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

public class App implements IClientDisconnectionHandler{

    private Stage stage;
    private final LoginScreenPane loginScreen;
    private Client client;

    public App(Stage stage, LoginScreenPane loginScreen, Client client) {
        this.stage = stage;
        this.loginScreen = loginScreen;
        setUp(client);

        stage.setOnCloseRequest(event -> {
            try {
                client.disconnect();
            } catch (DisconnectedException e) {
                // doesn't matter
            }

            Platform.exit();
        });
    }

    public Client getClient() {
        return this.client;
    }

    public void goToLoginScreen() {
        stage.getScene().setRoot(loginScreen);
    }

    public void goToLobbyScreen() {
        stage.getScene().setRoot(new LobbyScreenPane(this));
    }

    public void goToLayoutScreen(Nickname opponent) {
        stage.getScene().setRoot(new LayoutScreenPane(this, opponent));
    }

    public void goToGameScreen(Nickname opponent, Layout layout) {
        stage.getScene().setRoot(new LobbyScreenPane(this));
    }

    public void goToGameScreen(RestoreStateGame restoreState) {
        stage.getScene().setRoot(new GameScreenPane(this, restoreState));
    }


    private void setUp(Client client) {
        this.client = client;
        RestoreState restoreState = client.getRestoreState();

        if (restoreState == null || restoreState.isLobby()) {
            goToLobbyScreen();
        }
        else {
            goToGameScreen((RestoreStateGame) restoreState);
        }
    }

    @Override
    public void handleDisconnected(ConnectionLossCause cause) {

        InetSocketAddress address = client.getServerAddress();
        Nickname nickname = client.getNickname();

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING, "", ButtonType.CANCEL);
            alert.setHeaderText("Connection lost - trying to reconnect");
            alert.setGraphic(new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS));
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

            String text = "Cause: ";

            switch (cause) {
                case CLOSED:
                    text += "Server socket connection was closed.";
                    break;
                case SERVER_DISCONNECTED:
                    text += "Server has been shut down.";
                    break;
                case CORRUPTED:
                    text += "Message stream is corrupted.";
                    break;
                case INVALID_MESSAGE:
                    text += "Invalid message received.";
                    break;
                case UNAVAILABLE:
                    text += "Server is not responding.";
                    break;
            }

            alert.setContentText(text);
            AtomicBoolean cancelReconnecting = new AtomicBoolean(false);

            Task<Client> reconnectionTask = new Task<>() {
                @Override
                protected Client call() throws Exception {
                    for (int i = 0; i < 30 && !cancelReconnecting.get(); i++) {
                        System.out.println(i);
                        try {
                            return new Client(address, nickname);
                        } catch (ConnectException e) {
                            Thread.sleep(1000);
                        }
                    }

                    return null;
                }
            };

            reconnectionTask.setOnSucceeded(event -> {
                Client client = (Client) event.getSource().getValue();

                if (client == null) {
                    alert.setHeaderText("Reconnection failed");
                    alert.setGraphic(null);
                    return;
                }

                setUp(client);

                Platform.runLater(alert::close);
            });

            new Thread(reconnectionTask).start();

            alert.showAndWait().ifPresent(response -> {
                cancelReconnecting.set(true);
                if (response == ButtonType.CANCEL) {
                    goToLoginScreen();
                }
            });
        });
    }
}
