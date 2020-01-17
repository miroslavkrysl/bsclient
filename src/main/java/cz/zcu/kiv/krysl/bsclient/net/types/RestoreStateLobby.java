package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;

public class RestoreStateLobby extends RestoreState {
    private final Nickname nickname;

    public RestoreStateLobby(Nickname nickname) {
        this.nickname = nickname;
    }

    public Nickname getNickname() {
        return nickname;
    }

    public static RestoreStateLobby deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        Nickname nickname = Nickname.deserialize(deserializer);
        return new RestoreStateLobby(nickname);
    }
}
