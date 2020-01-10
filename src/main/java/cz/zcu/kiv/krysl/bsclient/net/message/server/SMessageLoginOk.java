package cz.zcu.kiv.krysl.bsclient.net.message.server;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.message.items.SessionKey;

public class SMessageLoginOk extends ServerMessage {
    private final SessionKey sessionKey;

    public SMessageLoginOk(SessionKey sessionKey) {
        super(ServerMessageKind.LOGIN_OK);
        this.sessionKey = sessionKey;
    }

    public static SMessageLoginOk deserializeFromPayload(PayloadDeserializer deserializer) throws DeserializeException {
        SessionKey sessionKey = SessionKey.deserialize(deserializer);
        return new SMessageLoginOk(sessionKey);
    }

    public SessionKey getSessionKey() {
        return sessionKey;
    }
}
