package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;

public abstract class ReconnectedState {

    public static ReconnectedState deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        String state = deserializer.getString();

        switch (state) {
            case "lobby":
                return new ReconnectedStateLobby();
            case "game":
                return ReconnectedStateGame.deserialize(deserializer);
            default:
                throw new DeserializeException("Can't deserialize ReconnectedState from payload.");
        }
    }
}
