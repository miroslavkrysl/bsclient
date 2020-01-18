package cz.zcu.kiv.krysl.bsclient.gui;

import cz.zcu.kiv.krysl.bsclient.net.types.Layout;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;


public class LayoutScenePane extends BorderPane {

    private final Layout layout;
    private BoardPane board;

    public LayoutScenePane() {
        this.layout = Layout.createDefault();

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
}
