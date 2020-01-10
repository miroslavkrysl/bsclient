package cz.zcu.kiv.krysl.bsclient.net.message.server;

public enum ServerMessageKind {
    CONTEXT_ILLEGAL,

    RECONNECT_OK,
    RECONNECT_FAIL,

    LOGIN_OK,
    LOGIN_FAIL,

    JOIN_GAME_WAIT,
    JOIN_GAME_OK,

    LAYOUT_OK,
    LAYOUT_FAIL,

    SHOOT_HIT,
    SHOOT_SUNK,
    SHOOT_MISS,

    LEAVE_GAME_OK,

    DISCONNECT_OK,

    DISCONNECT,
    OPPONENT_JOINED,
    OPPONENT_READY,
    OPPONENT_LEFT,
    OPPONENT_MISSED,
    OPPONENT_HIT,
    GAME_OVER
}
