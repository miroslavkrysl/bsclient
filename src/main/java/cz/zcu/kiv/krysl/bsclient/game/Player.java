package cz.zcu.kiv.krysl.bsclient.game;

public class Player {
    private String nick;
    private Board board;

    public Player(String nick, Board board) {
        this.nick = nick;
        this.board = board;
    }

    public String getNick() {
        return nick;
    }

    public Board getBoard() {
        return board;
    }
}
