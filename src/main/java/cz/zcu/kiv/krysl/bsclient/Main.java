package cz.zcu.kiv.krysl.bsclient;

import cz.zcu.kiv.krysl.bsclient.game.Board;
import cz.zcu.kiv.krysl.bsclient.gui.BoardPane;
import cz.zcu.kiv.krysl.bsclient.gui.ConnectPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    private Scene scene;
    private ConnectPane connectPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
//        connectPane = new ConnectPane(this);
        Board board = new Board(10, 10);
        scene = new Scene(new BoardPane(board));

        primaryStage.setScene(scene);
        primaryStage.setTitle("Battleships client");
        primaryStage.show();
    }

    public void goToConnect() {
        scene.setRoot(connectPane);
    }
}
