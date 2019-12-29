package cz.zcu.kiv.krysl.bsclient.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import cz.zcu.kiv.krysl.bsclient.App;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class ConnectPane extends BorderPane {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField addressTF;

    @FXML
    private TextField portTF;

    @FXML
    private TextField nicknameTF;

    @FXML
    private Button connectBtn;

    @FXML
    private Label errorLabel;

    @FXML
    private ProgressIndicator connectProgress;

    private final App app;

    public ConnectPane(App app) {
        this.app = app;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource("templates/connect.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading ConnectPane template: " + e.getMessage());
            Platform.exit();
        }
    }

    @FXML
    void initialize() {
        assert addressTF != null : "fx:id=\"addressTF\" was not injected: check your FXML file 'connect.fxml'.";
        assert portTF != null : "fx:id=\"portTF\" was not injected: check your FXML file 'connect.fxml'.";
        assert nicknameTF != null : "fx:id=\"nicknameTF\" was not injected: check your FXML file 'connect.fxml'.";
        assert connectBtn != null : "fx:id=\"connectBtn\" was not injected: check your FXML file 'connect.fxml'.";
        assert errorLabel != null : "fx:id=\"errorLabel\" was not injected: check your FXML file 'connect.fxml'.";
        assert connectProgress != null : "fx:id=\"connectProgress\" was not injected: check your FXML file 'connect.fxml'.";

        // make port field only numeric
        portTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                portTF.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    void connect(ActionEvent event) {
        // TODO: implement connection logic
    }

    public void setErrorMessage(String message) {
        Platform.runLater(() -> {
            if (message == null || message.isEmpty()) {
                errorLabel.setText("");
                errorLabel.setVisible(false);
            }
            else {
                errorLabel.setText(message);
                errorLabel.setVisible(true);
            }
        });
    }
}
