package cz.zcu.kiv.krysl;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button("BS client");

        btn.setOnAction(event -> System.out.println("This is BS client"));

        Scene scene = new Scene(btn);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Battleships client");
        primaryStage.show();
    }
}
