package cz.zcu.kiv.krysl.bsclient.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class HeadingLabel extends Label {
    public HeadingLabel() {
        super("Battleships Game");

        setPadding(new Insets(0, 0, 10, 0));
        setTextAlignment(TextAlignment.CENTER);
        setAlignment(Pos.CENTER);
        setMaxWidth(Double.MAX_VALUE);
        setFont(Font.font(null, FontWeight.BOLD, FontPosture.ITALIC, 28));
    }
}
