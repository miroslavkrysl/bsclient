package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;

import java.util.HashMap;
import java.util.Map;

public class ShipsPlacements implements ISerializableItem {
    private final Map<ShipKind, Placement> placements;

    public ShipsPlacements(Map<ShipKind, Placement> placements) {
        this.placements = placements;
    }

    public Map<ShipKind, Placement> getPlacements() {
        return placements;
    }

    public static ShipsPlacements deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        int length = deserializer.getInt();

        if (length < 0 || length > 5) {
            throw new DeserializeException("Can't deserialize ShipsPlacements from payload, illegal array length.");
        }

        Map<ShipKind, Placement> ships = new HashMap<>();

        for (int i = 0; i < length; i++) {
            ShipKind kind = ShipKind.deserialize(deserializer);
            Placement placement = Placement.deserialize(deserializer);

            if (ships.containsKey(kind)) {
                throw new DeserializeException("Can't deserialize ShipsPlacements from payload, ship kinds are repeating.");
            }

            ships.put(kind, placement);
        }

        return new ShipsPlacements(ships);
    }

    @Override
    public void serialize(PayloadSerializer serializer) {
        serializer.addInt(placements.size());

        for (Map.Entry<ShipKind, Placement> entry : placements.entrySet()) {
            entry.getKey().serialize(serializer);
            entry.getValue().serialize(serializer);
        }
    }
}
