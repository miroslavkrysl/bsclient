package cz.zcu.kiv.krysl.bsclient.gui;

import cz.zcu.kiv.krysl.bsclient.App;
import cz.zcu.kiv.krysl.bsclient.net.client.IClientEventHandler;
import cz.zcu.kiv.krysl.bsclient.net.types.Layout;
import cz.zcu.kiv.krysl.bsclient.net.types.Nickname;
import cz.zcu.kiv.krysl.bsclient.net.types.Position;
import cz.zcu.kiv.krysl.bsclient.net.types.Who;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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

        this.board = new LayoutBoardPane(this.layout, 400);

        Button leaveGameButton = new Button("Leave game");
        Button chooseLayoutButton = new Button("Confirm the layout selection");

        HBox buttonHBox = new HBox(leaveGameButton, chooseLayoutButton);
        buttonHBox.setAlignment(Pos.CENTER);
        buttonHBox.setSpacing(20);

        setTop(heading);
        setCenter(this.board);
        setBottom(buttonHBox);
    }

    private void bindUi() {

    }

    @Override
    public void handleOpponentJoined(Nickname nickname) {
        // should not happen
    }

    @Override
    public void handleOpponentReady() {
        // TODO: implement opponent ready
    }

    @Override
    public void handleOpponentOffline() {
        // should not happen
    }

    @Override
    public void handleOpponentLeft() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Opponent left the game", ButtonType.OK);
            alert.showAndWait();
            app.goToLobbyScreen();
        });
    }

    @Override
    public void handleOpponentMissed(Position position) {
        // should not happen
    }

    @Override
    public void handleOpponentHit(Position position) {
        // should not happen
    }

    @Override
    public void handleGameOver(Who winner) {
        // should not happen
    }
}
