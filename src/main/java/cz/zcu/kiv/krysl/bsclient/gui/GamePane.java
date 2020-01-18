package cz.zcu.kiv.krysl.bsclient.gui;

import cz.zcu.kiv.krysl.bsclient.net.types.Layout;
import cz.zcu.kiv.krysl.bsclient.net.types.Nickname;
import javafx.scene.layout.BorderPane;

public class GamePane extends BorderPane {
    private Nickname opponent;
    private Layout layout;

    public GamePane(Nickname opponent, Layout layout) {
        this.opponent = opponent;
        this.layout = layout;
    }
}
