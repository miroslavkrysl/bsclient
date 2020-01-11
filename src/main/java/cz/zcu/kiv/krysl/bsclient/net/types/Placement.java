package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;

public class Placement implements ISerializableItem {
    private final Position position;
    private Orientation orientation;

    public Placement(Position position, Orientation orientation) {
        this.position = position;
        this.orientation = orientation;
    }

    public Position getPosition() {
        return position;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public void serialize(PayloadSerializer serializer) {
        position.serialize(serializer);
        orientation.serialize(serializer);
    }

    public static Placement deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        Position position = Position.deserialize(deserializer);
        Orientation orientation = Orientation.deserialize(deserializer);
        return new Placement(position, orientation);
    }
}
