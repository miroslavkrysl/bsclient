package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;

public abstract class RestoreState {

    public static RestoreState deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        String state = deserializer.getString();

        switch (state) {
            case "lobby":
                return new RestoreStateLobby();
            case "game":
                return RestoreStateGame.deserialize(deserializer);
            case "game_over":
                return RestoreStateGameOver.deserialize(deserializer);
            default:
                throw new DeserializeException("Can't deserialize ReconnectedState from payload.");
        }
    }

    public boolean isLobby() {
        return this instanceof RestoreStateLobby;
    }

    public boolean isGame() {
        return this instanceof RestoreStateGame;
    }

    public boolean isGameOver() {
        return this instanceof RestoreStateGameOver;
    }
}
