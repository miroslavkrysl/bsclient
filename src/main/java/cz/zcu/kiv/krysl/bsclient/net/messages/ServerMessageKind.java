package cz.zcu.kiv.krysl.bsclient.net.messages;

public enum ServerMessageKind {
    ILLEGAL_STATE,

    HANDSHAKE_OK,
    HANDSHAKE_FAIL,

    RECONNECT_OK,
    RECONNECT_FAIL,

    LOGIN_OK,
    LOGIN_FAIL,

    JOIN_GAME_WAIT,
    JOIN_GAME_JOINED,

    LAYOUT_OK,
    LAYOUT_FAIL,

    SHOOT_OK,
    SHOOT_FAIL,

    LEAVE_GAME_OK,

    DISCONNECT_OK,

    DISCONNECT,
    OPPONENT_READY,
    OPPONENT_LEFT,
    OPPONENT_MISSED,
    OPPONENT_HIT,
    GAME_OVER
}
