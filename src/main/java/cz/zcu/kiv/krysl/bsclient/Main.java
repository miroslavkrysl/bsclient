package cz.zcu.kiv.krysl.bsclient;

import cz.zcu.kiv.krysl.bsclient.net.client.Client;
import cz.zcu.kiv.krysl.bsclient.net.client.ConnectException;
import cz.zcu.kiv.krysl.bsclient.net.types.Nickname;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.net.InetSocketAddress;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        try {
            Client client = new Client(new InetSocketAddress("localhost", 20000), new Nickname("freddy"));
            System.out.println("logged");
        } catch (ConnectException e) {
            System.out.println(e.getMessage());
        }

        Platform.exit();
    }
}
