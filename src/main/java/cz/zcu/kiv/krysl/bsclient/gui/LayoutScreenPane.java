package cz.zcu.kiv.krysl.bsclient.gui;

import cz.zcu.kiv.krysl.bsclient.App;
import cz.zcu.kiv.krysl.bsclient.net.client.IClientEventHandler;
import cz.zcu.kiv.krysl.bsclient.net.types.Layout;
import cz.zcu.kiv.krysl.bsclient.net.types.Nickname;
import cz.zcu.kiv.krysl.bsclient.net.types.Position;
import cz.zcu.kiv.krysl.bsclient.net.types.Who;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;


public class LayoutScreenPane extends BorderPane implements IClientEventHandler {

    private App app;
    private final Layout layout;
    private BoardPane board;
    private Nickname opponent;

    public LayoutScreenPane(App app, Nickname opponent) {
        this.app = app;
        this.opponent = opponent;
        this.layout = Layout.createDefault();

        app.getClient().setEventHandler(this);

        createUi();
        bindUi();
    }

    private void createUi() {
        setPadding(new Insets(10));

        Label heading = new Label("Choose layout");
        heading.setFont(Font.font(null, FontWeight.BOLD, 30));
        heading.setAlignment(Pos.CENTER);
        heading.setTextAlignment(TextAlignment.CENTER);
        heading.setMaxWidth(Double.MAX_VALUE);
        heading.setPadding(new Insets(0, 0, 10, 0));

        // --- Board
        this.board = new LayoutBoardPane(this.layout, 400);

        setTop(heading);
        setCenter(this.board);
    }

    private void bindUi() {

    }

    @Override
    public void handleOpponentJoined(Nickname nickname) {
        // not interested
    }

    @Override
    public void handleOpponentReady() {
        // TODO: implement opponent ready
    }

    @Override
    public void handleOpponentLeft() {
        // not interested
    }

    @Override
    public void handleOpponentMissed(Position position) {
        // not interested
    }

    @Override
    public void handleOpponentHit(Position position) {
        // not interested
    }

    @Override
    public void handleGameOver(Who winner) {
        // not interested
    }
}
