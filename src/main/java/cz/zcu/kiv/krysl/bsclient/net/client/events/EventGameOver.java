package cz.zcu.kiv.krysl.bsclient.net.client.events;

import cz.zcu.kiv.krysl.bsclient.net.types.Who;

public class EventGameOver extends Event {
    private Who winner;

    public EventGameOver(Who winner) {
        super(EventKind.GAME_OVER);
        this.winner = winner;
    }

    public Who getWinner() {
        return winner;
    }
}
