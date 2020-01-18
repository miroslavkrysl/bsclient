package cz.zcu.kiv.krysl.bsclient;

import cz.zcu.kiv.krysl.bsclient.gui.LayoutScenePane;
import cz.zcu.kiv.krysl.bsclient.net.client.*;
import cz.zcu.kiv.krysl.bsclient.net.types.Nickname;
import cz.zcu.kiv.krysl.bsclient.net.types.Position;
import cz.zcu.kiv.krysl.bsclient.net.types.Who;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class Main extends Application implements IClientEventHandler {

    private Client client;

    public static void main(String[] args) {
        System.setProperty("log4j.configurationFile", "log4jconfig.xml");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Battleships Game");

//        LoginPane loginPane = new LoginPane();
//        primaryStage.setScene(new Scene(loginPane));
        primaryStage.setScene(new Scene(new LayoutScenePane()));
        primaryStage.show();
    }

    @Override
    public void handleOpponentJoined(Nickname nickname) {
        System.out.println("handleOpponentJoined");
    }

    @Override
    public void handleOpponentReady() {
        System.out.println("handleOpponentReady");
    }

    @Override
    public void handleOpponentLeft() {
        System.out.println("handleOpponentLeft");
    }

    @Override
    public void handleOpponentMissed(Position position) {
        System.out.println("handleOpponentMissed");
    }

    @Override
    public void handleOpponentHit(Position position) {
        System.out.println("handleOpponentHit");
    }

    @Override
    public void handleGameOver(Who winner) {
        System.out.println("handleGameOver");
    }

    @Override
    public void handleDisconnected(ConnectionLossCause cause) {
        System.out.println("handleDisconnected");
    }
}
