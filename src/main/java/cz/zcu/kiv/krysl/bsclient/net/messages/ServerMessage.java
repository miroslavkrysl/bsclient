package cz.zcu.kiv.krysl.bsclient.net.messages;

public abstract class ServerMessage {
    private ServerMessageKind kind;

    public ServerMessage(ServerMessageKind kind) {
        this.kind = kind;
    }

    public boolean isResponse() {
        switch (kind) {
            case ILLEGAL_STATE:
            case HANDSHAKE_OK:
            case HANDSHAKE_FAIL:
            case RECONNECT_OK:
            case RECONNECT_FAIL:
            case LOGIN_OK:
            case LOGIN_FAIL:
            case JOIN_GAME_WAIT:
            case JOIN_GAME_JOINED:
            case LAYOUT_OK:
            case LAYOUT_FAIL:
            case SHOOT_OK:
            case SHOOT_FAIL:
            case LEAVE_GAME_OK:
            case DISCONNECT_OK:
                return true;
            case DISCONNECT:
            case OPPONENT_READY:
            case OPPONENT_LEFT:
            case OPPONENT_MISSED:
            case OPPONENT_HIT:
            case GAME_OVER:
                return false;
            default:
                throw new IllegalStateException("Unexpected value: " + kind);
        }
    }
}
