package cz.zcu.kiv.krysl.bsclient.gui;

import cz.zcu.kiv.krysl.bsclient.game.Board;
import cz.zcu.kiv.krysl.bsclient.game.Cell;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class BoardPane extends Pane {
    private Board board;
    private Rectangle[][] rectangles;

    private final static double BOARD_HEIGHT = 500;

    public BoardPane(Board board) {
        this.board = board;
        this.rectangles = new Rectangle[board.getHeight()][board.getWidth()];

        Cell[][] map = board.getMap();
        double rectSize = BOARD_HEIGHT / map.length;

        setWidth(rectSize * board.getWidth());
        setHeight(rectSize * board.getHeight());

        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                Rectangle rect = new Rectangle(rectSize, rectSize);
                Cell field = map[i][j];
                rect.setY(rectSize * i);
                rect.setX(rectSize * j);

                getChildren().add(rect);
                rectangles[i][j] = rect;
            }
        }
    }
}
