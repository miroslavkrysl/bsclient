package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;

public abstract class RestoreState {

    public static RestoreState deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        String state = deserializer.getString();

        switch (state) {
            case "lobby":
                return RestoreStateLobby.deserialize(deserializer);
            case "game":
                return RestoreStateGame.deserialize(deserializer);
            default:
                throw new DeserializeException("Can't deserialize RestoreState from payload.");
        }
    }

    public boolean isLobby() {
        return this instanceof RestoreStateLobby;
    }

    public boolean isGame() {
        return this instanceof RestoreStateGame;
    }
}
