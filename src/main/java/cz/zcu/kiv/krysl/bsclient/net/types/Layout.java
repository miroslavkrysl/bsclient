package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;

public class Layout implements ISerializableItem {
    private final ShipsPlacements placements;

    public Layout(ShipsPlacements placements) {
        if (placements.getPlacements().size() != 5) {
            throw new IllegalArgumentException("Layout must have exactly 5 placements.");
        }

        this.placements = placements;
    }

    public ShipsPlacements getPlacements() {
        return placements;
    }

    @Override
    public void serialize(PayloadSerializer serializer) {
        placements.serialize(serializer);
    }
}
