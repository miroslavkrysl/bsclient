package cz.zcu.kiv.krysl.bsclient;

import cz.zcu.kiv.krysl.bsclient.net.DisconnectedException;
import cz.zcu.kiv.krysl.bsclient.net.codec.Deserializer;
import cz.zcu.kiv.krysl.bsclient.net.codec.Serializer;
import cz.zcu.kiv.krysl.bsclient.net.connection.Connection;
import cz.zcu.kiv.krysl.bsclient.net.connection.ConnectionLossCause;
import cz.zcu.kiv.krysl.bsclient.net.connection.IConnectionManager;
import cz.zcu.kiv.krysl.bsclient.net.message.client.CMessageLogin;
import cz.zcu.kiv.krysl.bsclient.net.message.client.ClientMessage;
import cz.zcu.kiv.krysl.bsclient.net.message.items.Nickname;
import cz.zcu.kiv.krysl.bsclient.net.message.server.ServerMessage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Main extends Application {

    static class ConnectionManager implements IConnectionManager<ServerMessage> {

        @Override
        public void handleMessageReceived(ServerMessage clientMessage) {
            System.out.println("man: handling");
            System.out.println(clientMessage.getClass().getName());
        }

        @Override
        public void handleConnectionLost(ConnectionLossCause cause) {
            System.out.println("connection lost");
            Platform.exit();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Connection<ServerMessage, ClientMessage> connection = new Connection<>(
                    new InetSocketAddress("localhost", 20000),
                    new Deserializer(),
                    new Serializer(),
                    new ConnectionManager());

            while (true) {
                try {
                    System.out.println("sending");
                    connection.send(new CMessageLogin(new Nickname("freddy")));
                    Thread.sleep(5000);
                } catch (DisconnectedException | InterruptedException e) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            Platform.exit();
        }

    }
}
