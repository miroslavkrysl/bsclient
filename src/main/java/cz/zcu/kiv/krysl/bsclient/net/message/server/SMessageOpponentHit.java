package cz.zcu.kiv.krysl.bsclient.net.message.server;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.message.items.Position;

public class SMessageOpponentHit extends ServerMessage {
    private final Position position;

    public SMessageOpponentHit(Position position) {
        super(ServerMessageKind.OPPONENT_HIT);
        this.position = position;
    }

    public static SMessageOpponentHit deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        Position position = Position.deserialize(deserializer);
        return new SMessageOpponentHit(position);
    }

    public Position getPosition() {
        return position;
    }
}
