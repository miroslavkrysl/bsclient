package cz.zcu.kiv.krysl.bsclient.net.message.items;

import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;

public class Layout implements ISerializableItem {
    private Placement[] placements;

    public Layout(Placement[] placements) {
        if (placements.length != 5) {
            throw new IllegalArgumentException("Layout must have exactly 5 placements.");
        }

        this.placements = placements;
    }

    public Placement[] getPlacements() {
        return placements;
    }

    @Override
    public void serialize(PayloadSerializer serializer) {
        for (Placement placement : placements) {
            placement.serialize(serializer);
        }
    }
}
