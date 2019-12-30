package cz.zcu.kiv.krysl.bsclient.game;

public class Position {
    private int row;
    private int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Position add(Position other) {
        return new Position(this.row + other.row, this.col + other.col);
    }
}
