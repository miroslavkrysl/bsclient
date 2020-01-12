package cz.zcu.kiv.krysl.bsclient.net.client.events;

import cz.zcu.kiv.krysl.bsclient.net.types.Position;

public class EventOpponentHit extends Event {
    private Position position;

    public EventOpponentHit(Position position) {
        super(EventKind.OPPONENT_HIT);
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}
