package cz.zcu.kiv.krysl.bsclient;

import cz.zcu.kiv.krysl.bsclient.net.client.*;
import javafx.stage.Stage;

public class App {


    private Stage stage;
    private BattleshipsClient client;

    public App(Stage stage) {
        this.stage = stage;
    }

    public BattleshipsClient getClient() {
        return this.client;
    }

    public void setClient(BattleshipsClient client) {
        this.client = client;
    }
}
