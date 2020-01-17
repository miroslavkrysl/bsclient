package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;

public class SessionKey implements ISerializableItem{
    private final String key;

    public SessionKey(String key) {
        if (key.length() != 16) {
            throw new IllegalArgumentException("Session key must have exactly 16 chars");
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