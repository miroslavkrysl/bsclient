package cz.zcu.kiv.krysl.bsclient;

import cz.zcu.kiv.krysl.bsclient.gui.ConnectPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private Scene scene;
    private ConnectPane connectPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        connectPane = new ConnectPane(this);
        scene = new Scene(connectPane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Battleships client");
        primaryStage.show();
    }

    public void goToConnect() {
        scene.setRoot(connectPane);
    }
}
