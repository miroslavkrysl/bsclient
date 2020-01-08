package cz.zcu.kiv.krysl.bsclient.net.messages;

public class CMessageReconnect extends ClientMessage {
    private final String sessionKey;

    public CMessageReconnect(String sessionKey) {
        super(ClientMessageKind.RECONNECT);

        if (sessionKey.length() != 32) {
            throw new IllegalArgumentException("Session key must have exactly 32 chars.");
        }

        this.sessionKey = sessionKey;
    }
}
