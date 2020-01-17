package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;

public enum ShipKind implements ISerializableItem {
    AIRCRAFT_CARRIER,
    BATTLESHIP,
    CRUISER,
    DESTROYER,
    PATROL_BOAT;

    public static ShipKind deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        switch (deserializer.getString()) {
            case "AC":
                return AIRCRAFT_CARRIER;
            case "B":
                return BATTLESHIP;
            case "C":
                return CRUISER;
            case "D":
                return DESTROYER;
            case "P":
                return PATROL_BOAT;
            default:
                throw new DeserializeException("Can't deserialize ShipKind from payload.");
        }
    }

    @Override
    public void serialize(PayloadSerializer serializer) {
        switch (this) {
            case AIRCRAFT_CARRIER:
                serializer.addString("A");
                break;
            case BATTLESHIP:
                serializer.addString("B");
                break;
            case CRUISER:
                serializer.addString("C");
                break;
            case DESTROYER:
                serializer.addString("D");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
