package cz.zcu.kiv.krysl.bsclient.game;

public class Game {

    public enum State {
        LAYOUTING,
        PLAYING,
        ENDED
    }

    public static final int BOARD_SIZE = 10;

    private Player player;
    private Player opponent;
    private State state;

    public Game(String playerName, String opponentName) {
        this.player = new Player(playerName, new Board(BOARD_SIZE));
        this.opponent = new Player(opponentName, new Board(BOARD_SIZE));
        this.state = State.LAYOUTING;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getOpponent() {
        return opponent;
    }

    public State getState() {
        return state;
    }

    /**
     * Shoot at the given position.
     * @param position Target position
     * @return True if the target is a part of any ship
     * which wasn't already hit before shooting, false otherwise.
     */
    public boolean shoot(Position position) {
        Cell target = opponent.getBoard().getCell(position);

        if (target.isMarked()) {
            return false;
        }

        target.setMarked(true);

        return false;
    }
}
