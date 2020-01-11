package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;

public class Hits {
    private final Position[] positions;

    public Hits(Position[] positions) {
        this.positions = positions;
    }

    public Position[] getPositions() {
        return positions;
    }

    public static Hits deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        int length = deserializer.getInt();

        if (length < 0 || length > 100) {
            throw new DeserializeException("Can't deserialize Hits from payload, illegal array length.");
        }

        Position[] positions = new Position[length];

        for (int i = 0; i < length; i++) {
            positions[i] = Position.deserialize(deserializer);
        }

        return new Hits(positions);
    }
}
