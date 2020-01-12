package cz.zcu.kiv.krysl.bsclient.net.messages.server;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.types.Position;

public class SMessageOpponentMissed extends ServerMessage {
    private final Position position;

    public SMessageOpponentMissed(Position position) {
        super(ServerMessageKind.OPPONENT_MISSED);
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public static SMessageOpponentMissed deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        Position position = Position.deserialize(deserializer);
        return new SMessageOpponentMissed(position);
    }
}