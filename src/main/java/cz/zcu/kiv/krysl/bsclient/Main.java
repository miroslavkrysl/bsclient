package cz.zcu.kiv.krysl.bsclient;

import cz.zcu.kiv.krysl.bsclient.gui.LoginScreenPane;
import cz.zcu.kiv.krysl.bsclient.net.client.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class of the application.
 */
public class Main extends Application {

    private Client client;

    public static void main(String[] args) {
        System.setProperty("log4j.configurationFile", "log4jconfig.xml");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Battleships Game");

        primaryStage.setWidth(1000);
        primaryStage.setHeight(600);

        LoginScreenPane loginPane = new LoginScreenPane(primaryStage);
        primaryStage.setScene(new Scene(loginPane));
        primaryStage.show();
    }
}
