package cz.zcu.kiv.krysl.bsclient.net.messages.server;

public enum ServerMessageKind {
    ILLEGAL_STATE,

    ALIVE_OK,

    LOGIN_OK,
    LOGIN_FULL,
    LOGIN_TAKEN,
    LOGIN_RESTORED,

    JOIN_GAME_WAIT,
    JOIN_GAME_OK,

    LAYOUT_OK,
    LAYOUT_FAIL,

    SHOOT_HIT,
    SHOOT_SUNK,
    SHOOT_MISSED,

    LEAVE_GAME_OK,

    LOGOUT_OK,

    DISCONNECT,
    OPPONENT_JOINED,
    OPPONENT_READY,
    OPPONENT_OFFLINE,
    OPPONENT_LEFT,
    OPPONENT_MISSED,
    OPPONENT_HIT,
    GAME_OVER;

    public boolean isResponse() {
        switch (this) {
            case ILLEGAL_STATE:
            case ALIVE_OK:
            case LOGIN_OK:
            case LOGIN_FULL:
            case LOGIN_TAKEN:
            case LOGIN_RESTORED:
            case JOIN_GAME_WAIT:
            case JOIN_GAME_OK:
            case LAYOUT_OK:
            case LAYOUT_FAIL:
            case SHOOT_HIT:
            case SHOOT_SUNK:
            case SHOOT_MISSED:
            case LEAVE_GAME_OK:
            case LOGOUT_OK:
                return true;
            case DISCONNECT:
            case OPPONENT_JOINED:
            case OPPONENT_READY:
            case OPPONENT_OFFLINE:
            case OPPONENT_LEFT:
            case OPPONENT_MISSED:
            case OPPONENT_HIT:
            case GAME_OVER:
                return false;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
