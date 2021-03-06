package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;

public class Nickname implements ISerializableItem{
    private final String nickname;

    public Nickname(String nickname) {
        if (nickname.length() < 3) {
            throw new IllegalArgumentException("Nickname must have at least 3 chars");
        }

        if (nickname.length() > 16) {
            throw new IllegalArgumentException("Nickname must have at most 16 chars");
        }

        if (!nickname.chars().allMatch(c -> Character.isAlphabetic(c) || Character.isDigit(c))) {
            throw new IllegalArgumentException("Nickname must have only alphanumeric characters.");
        }

        this.nickname = nickname;
    }

    public String getValue() {
        return nickname;
    }

    @Override
    public void serialize(PayloadSerializer serializer) {
        serializer.addString(nickname);
    }

    public static Nickname deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        try {
            return new Nickname(deserializer.getString());
        } catch (IllegalArgumentException e) {
            throw new DeserializeException("Can't deserialize nickname from payload: " + e);
        }
    }
}
