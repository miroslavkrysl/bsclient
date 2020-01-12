package cz.zcu.kiv.krysl.bsclient;

import cz.zcu.kiv.krysl.bsclient.net.DisconnectedException;
import cz.zcu.kiv.krysl.bsclient.net.client.AlreadyOnlineException;
import cz.zcu.kiv.krysl.bsclient.net.client.Client;
import cz.zcu.kiv.krysl.bsclient.net.client.ConnectException;
import cz.zcu.kiv.krysl.bsclient.net.client.OfflineException;
import cz.zcu.kiv.krysl.bsclient.net.types.Nickname;
import cz.zcu.kiv.krysl.bsclient.net.types.RestoreState;
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

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    RestoreState state = client.restore();
                    System.out.println("restored: " + state.getClass().getName());
                    break;
                } catch (AlreadyOnlineException | OfflineException | ConnectException e) {
                    System.out.println(e.getMessage());
                } catch (DisconnectedException e) {
                    System.out.println("Disconnected");
                    break;
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (ConnectException e) {
            System.out.println(e.getMessage());
        }

        Platform.exit();
    }
}
