package cz.zcu.kiv.krysl.bsclient;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.DisconnectedException;
import cz.zcu.kiv.krysl.bsclient.net.client.AlreadyConnectedException;
import cz.zcu.kiv.krysl.bsclient.net.client.Client;
import cz.zcu.kiv.krysl.bsclient.net.client.ClientConnectException;
import cz.zcu.kiv.krysl.bsclient.net.client.ConnectionLossCause;
import cz.zcu.kiv.krysl.bsclient.net.codec.Deserializer;
import cz.zcu.kiv.krysl.bsclient.net.codec.Serializer;
import cz.zcu.kiv.krysl.bsclient.net.connection.Connection;
import cz.zcu.kiv.krysl.bsclient.net.messages.client.CMessageLogin;
import cz.zcu.kiv.krysl.bsclient.net.messages.client.ClientMessage;
import cz.zcu.kiv.krysl.bsclient.net.results.ConnectResult;
import cz.zcu.kiv.krysl.bsclient.net.types.Nickname;
import cz.zcu.kiv.krysl.bsclient.net.messages.server.ServerMessage;
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
            ConnectResult result = client.connect();
            System.out.println("Result: " + result.getClass().getName());
        } catch (InterruptedException e) {
            //
        } catch (ClientConnectException e) {
            System.out.println(e.getMessage());
        }

        Platform.exit();
    }
}
