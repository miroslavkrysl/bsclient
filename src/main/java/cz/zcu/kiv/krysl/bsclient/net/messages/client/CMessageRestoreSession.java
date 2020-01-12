package cz.zcu.kiv.krysl.bsclient.net.messages.client;

import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;
import cz.zcu.kiv.krysl.bsclient.net.types.SessionKey;

public class CMessageRestoreSession extends ClientMessage {
    private final SessionKey sessionKey;

    public CMessageRestoreSession(SessionKey sessionKey) {
        super(ClientMessageKind.RESTORE_SESSION);

        this.sessionKey = sessionKey;
    }

    public SessionKey getSessionKey() {
        return sessionKey;
    }

    @Override
    protected void serializePayload(PayloadSerializer serializer) {
        serializer.addString(sessionKey.getKey());
    }
}
