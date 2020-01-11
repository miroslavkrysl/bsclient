package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;

public class ShipId {
    private final int id;

    public ShipId(int id) {
        if (id < 0 || id >= 5) {
            throw new IllegalArgumentException("ShipId must be between 0 and 4 inclusive");
        }

        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ShipId deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        try {
            return new ShipId(deserializer.getInt());
        } catch (IllegalArgumentException e) {
            throw new DeserializeException("Can't deserialize ShipId from payload: " + e);
        }
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ShipId) && this.id == ((ShipId) obj).id;
    }
}
