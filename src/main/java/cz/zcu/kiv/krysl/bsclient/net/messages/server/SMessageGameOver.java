package cz.zcu.kiv.krysl.bsclient.net.messages.server;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.types.Who;

public class SMessageGameOver extends ServerMessage {

    private Who winner;

    public SMessageGameOver(Who winner) {
        super(ServerMessageKind.GAME_OVER);
        this.winner = winner;
    }

    public Who getWinner() {
        return winner;
    }

    public static SMessageGameOver deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        Who winner = Who.deserialize(deserializer);
        return new SMessageGameOver(winner);
    }
}
