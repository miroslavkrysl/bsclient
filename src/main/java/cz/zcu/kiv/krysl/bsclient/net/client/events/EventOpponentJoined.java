package cz.zcu.kiv.krysl.bsclient.net.client.events;

import cz.zcu.kiv.krysl.bsclient.net.types.Nickname;

public class EventOpponentJoined extends Event {
    private Nickname opponent;

    public EventOpponentJoined(Nickname opponent) {
        super(EventKind.OPPONENT_JOINED);
        this.opponent = opponent;
    }

    public Nickname getOpponent() {
        return opponent;
    }
}
