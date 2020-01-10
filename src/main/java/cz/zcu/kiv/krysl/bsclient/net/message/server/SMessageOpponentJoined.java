package cz.zcu.kiv.krysl.bsclient.net.message.server;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.message.items.Nickname;

public class SMessageOpponentJoined extends ServerMessage {
    private Nickname nickname;

    public SMessageOpponentJoined(Nickname opponent) {
        super(ServerMessageKind.OPPONENT_JOINED);
        this.nickname = opponent;
    }

    public static ServerMessage deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        Nickname nickname = Nickname.deserialize(deserializer);
        return new SMessageOpponentJoined(nickname);
    }

    public Nickname getNickname() {
        return nickname;
    }
}
