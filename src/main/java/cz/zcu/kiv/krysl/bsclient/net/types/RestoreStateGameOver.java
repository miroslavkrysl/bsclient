package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;

public class RestoreStateGameOver extends RestoreState {

    private Who winner;

    public RestoreStateGameOver(Who winner) {
        this.winner = winner;
    }

    public Who getWinner() {
        return winner;
    }

    public static RestoreStateGameOver deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        Who winner = Who.deserialize(deserializer);
        return new RestoreStateGameOver(winner);
    }
}
