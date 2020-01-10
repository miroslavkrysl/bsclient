package cz.zcu.kiv.krysl.bsclient.net.message.items;

import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;

public enum Orientation implements ISerializableItem {
    EAST,
    NORTH,
    WEST,
    SOUTH;

    @Override
    public void serialize(PayloadSerializer serializer) {
        switch (this) {
            case EAST:
                serializer.addString("east");
                break;
            case NORTH:
                serializer.addString("north");
                break;
            case WEST:
                serializer.addString("west");
                break;
            case SOUTH:
                serializer.addString("south");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
