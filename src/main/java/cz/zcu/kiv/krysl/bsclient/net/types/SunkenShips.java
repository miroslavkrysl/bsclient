package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;

import java.util.HashMap;
import java.util.Map;

public class SunkenShips {
    private final Map<ShipId, Placement> ships;

    public SunkenShips(Map<ShipId, Placement> ships) {
        this.ships = ships;
    }

    public Map<ShipId, Placement> getShips() {
        return ships;
    }

    public static SunkenShips deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        int length = deserializer.getInt();

        if (length < 0 || length > 5) {
            throw new DeserializeException("Can't deserialize SunkenShips from payload, illegal array length.");
        }

        Map<ShipId, Placement> ships = new HashMap<>();

        for (int i = 0; i < length; i++) {
            ShipId id = ShipId.deserialize(deserializer);
            Placement placement = Placement.deserialize(deserializer);

            if (ships.containsKey(id)) {
                throw new DeserializeException("Can't deserialize SunkenShips from payload, ship ids are repeating.");
            }

            ships.put(id, placement);
        }

        return new SunkenShips(ships);
    }
}
