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
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A class that handles window switching and reconnecting logic.
 */
public class App implements IClientDisconnectionHandler{

    private static final Duration RECONNECT_TIMEOUT = Duration.ofSeconds(20);
    private Stage stage;
    private final LoginScreenPane loginScreen;
    private Client client;

    /**
     * Create a new App.
     *
     * @param stage The main application stage.
     * @param loginScreen The initial login screen.
     * @param client The initial client.
     */
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

    /**
     * Get the current client instance.
     *
     * @return The client.
     */
    public Client getClient() {
        return this.client;
    }

    /**
     * Switch main stage to the login screen.
     */
    public void goToLoginScreen() {
        stage.getScene().setRoot(loginScreen);
    }

    /**
     * Switch main stage to the lobby screen.
     */
    public void goToLobbyScreen() {
        stage.getScene().setRoot(new LobbyScreenPane(this));
    }

    /**
     * Switch main stage to the layout screen.
     *
     * @param opponent The opponent nickname.
     * @param onTurn Information about who is on turn.
     */
    public void goToLayoutScreen(Nickname opponent, boolean onTurn) {
        stage.getScene().setRoot(new LayoutScreenPane(this, opponent, onTurn));
    }

    /**
     * Switch main stage to the game screen after selecting layout.
     *
     * @param opponent The opponent nickname.
     * @param layout Chosen layout.
     * @param onTurn Information about who is on turn.
     */
    public void goToGameScreen(Nickname opponent, Layout layout, boolean onTurn) {
        stage.getScene().setRoot(new GameScreenPane(this, opponent, layout, onTurn));
    }

    /**
     * Switch main stage to the game screen after session restoration.
     *
     * @param restoreState The state of the game after restoration.
     */
    public void goToGameScreen(RestoreStateGame restoreState) {
        stage.getScene().setRoot(new GameScreenPane(this, restoreState));
    }

    /**
     * Set the client and set self as a clients disconnection handler.
     *
     * @param client The client to configure with.
     */
    private void setUp(Client client) {
        this.client = client;
        client.setDisconnectionHandlerHandler(this);
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
        System.out.println("Disconnected: " + cause.toString());
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
                    Instant start = Instant.now();

                    while (!cancelReconnecting.get()) {
                        if (Duration.between(start, Instant.now()).compareTo(RECONNECT_TIMEOUT) >= 0) {
                            break;
                        }

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

                Platform.runLater(() -> {
                    cancelReconnecting.set(true);
                    alert.close();
                });
            });

            new Thread(reconnectionTask).start();

            alert.showAndWait().ifPresent(response -> {
                if (!cancelReconnecting.get()) {
                    cancelReconnecting.set(true);
                    goToLoginScreen();
                }
            });
        });
    }
}
