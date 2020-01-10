package cz.zcu.kiv.krysl.bsclient.net.message.items;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;

public class Position implements ISerializableItem {
    private int row;
    private int col;

    public Position(int row, int col) {
        if (row < 0 || row >= 10) {
            throw new IllegalArgumentException("Position row must be between 0 and 9 inclusive.");
        }

        if (col < 0 || col >= 10) {
            throw new IllegalArgumentException("Position col must be between 0 and 9 inclusive.");
        }

        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public void serialize(PayloadSerializer serializer) {
        serializer.addInt(row);
        serializer.addInt(col);
    }

    public static Position deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        try {
            return new Position(deserializer.getInt(), deserializer.getInt());
        } catch (IllegalArgumentException e) {
            throw new DeserializeException("Can't deserialize position from payload: " + e);
        }
    }
}
