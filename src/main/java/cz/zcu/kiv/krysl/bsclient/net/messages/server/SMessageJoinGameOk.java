package cz.zcu.kiv.krysl.bsclient.net.messages.server;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.types.Nickname;

public class SMessageJoinGameOk extends ServerMessage {
    private Nickname opponent;

    public SMessageJoinGameOk(Nickname opponent) {
        super(ServerMessageKind.JOIN_GAME_OK);
        this.opponent = opponent;
    }

    public Nickname getOpponent() {
        return opponent;
    }

    public static ServerMessage deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        Nickname opponent = Nickname.deserialize(deserializer);
        return new SMessageJoinGameOk(opponent);
    }
}
