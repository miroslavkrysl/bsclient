package cz.zcu.kiv.krysl.bsclient.net;

public enum ServerMessageKind {
    ALIVE,

    HANDSHAKE_OK,
    HANDSHAKE_FAIL,

    RECONNECT_OK,
    RECONNECT_FAIL,

    LOGIN_OK,
    LOGIN_FAIL,

    JOIN_GAME_WAIT,
    JOIN_GAME_JOINED,
    JOIN_GAME_FAIL,

    LAYOUT_OK,
    LAYOUT_FAIL,

    SHOOT_OK,
    SHOOT_FAIL,

    LEAVE_GAME_OK,
    LEAVE_GAME_FAIL,

    DISCONNECT_OK,
    DISCONNECT_FAIL
}
