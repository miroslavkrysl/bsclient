package cz.zcu.kiv.krysl.bsclient.gui;

import cz.zcu.kiv.krysl.bsclient.App;
import cz.zcu.kiv.krysl.bsclient.net.types.Layout;
import cz.zcu.kiv.krysl.bsclient.net.types.Nickname;
import cz.zcu.kiv.krysl.bsclient.net.types.RestoreStateGame;
import javafx.scene.layout.BorderPane;

public class GameScreenPane extends BorderPane {
    private App app;
    private Nickname opponent;
    private Layout layout;

    public GameScreenPane(App app, Nickname opponent, Layout layout) {
        this.app = app;
        this.opponent = opponent;
        this.layout = layout;
    }

    public GameScreenPane(App app, RestoreStateGame restoreState) {

    }
}
