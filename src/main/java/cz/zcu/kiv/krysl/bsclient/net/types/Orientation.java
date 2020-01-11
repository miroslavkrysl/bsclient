package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
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

    public static Orientation deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        switch (deserializer.getString()) {
            case "east":
                return EAST;
            case "north":
                return NORTH;
            case "west":
                return WEST;
            case "south":
                return SOUTH;
            default:
                throw new DeserializeException("Can't deserialize Orientation from payload.");
        }
    }
}
