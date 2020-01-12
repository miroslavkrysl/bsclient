package cz.zcu.kiv.krysl.bsclient.net.client.events;

import cz.zcu.kiv.krysl.bsclient.net.types.Position;

public class EventOpponentMissed extends Event {
    private Position position;

    public EventOpponentMissed(Position position) {
        super(EventKind.OPPONENT_MISSED);
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}
