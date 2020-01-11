package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;

public enum Who {
    YOU,
    OPPONENT;

    public static Who deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        switch (deserializer.getString()) {
            case "you":
                return YOU;
            case "opponent":
                return OPPONENT;
            default:
                throw new DeserializeException("Can't deserialize Who from payload.");
        }
    }
}
