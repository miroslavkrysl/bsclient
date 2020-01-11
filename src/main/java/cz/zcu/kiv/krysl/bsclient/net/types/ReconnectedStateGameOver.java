package cz.zcu.kiv.krysl.bsclient.net.types;

public class ReconnectedStateGameOver {

    private Who winner;

    public ReconnectedStateGameOver(Who winner) {
        this.winner = winner;
    }

    public Who getWinner() {
        return winner;
    }
}
