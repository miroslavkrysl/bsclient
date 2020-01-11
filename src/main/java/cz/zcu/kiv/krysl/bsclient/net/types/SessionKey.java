package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;

public class SessionKey implements ISerializableItem{
    private final String key;

    public SessionKey(String key) {
        if (key.length() < 3) {
            throw new IllegalArgumentException("Session key must have at least 3 chars");
        }

        if (key.length() > 32) {
            throw new IllegalArgumentException("SessionKey must have at most 32 chars");
        }

        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public void serialize(PayloadSerializer serializer) {
        serializer.addString(key);
    }

    public static SessionKey deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        try {
            return new SessionKey(deserializer.getString());
        } catch (IllegalArgumentException e) {
            throw new DeserializeException("Can't deserialize SessionKey from payload: " + e);
        }
    }
}