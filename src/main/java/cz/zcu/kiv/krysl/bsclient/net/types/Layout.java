package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;

import java.util.HashMap;
import java.util.Map;

public class Layout implements ISerializableItem {
    private final Map<ShipKind, Placement> placements;

    public Layout(Map<ShipKind, Placement> placements) {
        if (placements.size() != 5) {
            throw new IllegalArgumentException("Layout must have exactly 5 placements.");
        }

        this.placements = placements;
    }

    public static Layout createDefault() {
        HashMap<ShipKind, Placement> placements = new HashMap<>();
        placements.put(ShipKind.AIRCRAFT_CARRIER, new Placement(new Position(1, 1), Orientation.EAST));
        placements.put(ShipKind.BATTLESHIP, new Placement(new Position(2, 7), Orientation.SOUTH));
        placements.put(ShipKind.CRUISER, new Placement(new Position(7, 6), Orientation.WEST));
        placements.put(ShipKind.DESTROYER, new Placement(new Position(6, 2), Orientation.NORTH));
        placements.put(ShipKind.PATROL_BOAT, new Placement(new Position(3, 3), Orientation.EAST));

        return new Layout(placements);
    }

    public void setPlacement(ShipKind kind, Placement placement) {
        placements.put(kind, placement);
    }

    public Placement getPlacement(ShipKind kind) {
        return placements.get(kind);
    }

    public Map<ShipKind, Placement> getPlacements() {
        return placements;
    }

    @Override
    public void serialize(PayloadSerializer serializer) {
        ShipsPlacements shipsPlacements = new ShipsPlacements(placements);
        shipsPlacements.serialize(serializer);
    }

    public static Layout deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        ShipsPlacements placements = ShipsPlacements.deserialize(deserializer);
        try {
            return new Layout(placements.getPlacements());
        } catch (IllegalArgumentException e) {
            throw new DeserializeException("Can't deserialize Layout from payload: " + e);
        }
    }
}
