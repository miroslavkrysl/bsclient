package cz.zcu.kiv.krysl.bsclient.net.message.items;

import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;

public class Placement implements ISerializableItem {
    private Position position;
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
}
