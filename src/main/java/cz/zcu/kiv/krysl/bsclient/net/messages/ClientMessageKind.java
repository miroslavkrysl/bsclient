package cz.zcu.kiv.krysl.bsclient.net.messages;

public enum ClientMessageKind {
    ALIVE,
    HANDSHAKE,
    RECONNECT,
    LOGIN,
    JOIN_GAME,
    LAYOUT,
    SHOOT,
    LEAVE_GAME,
    DISCONNECT
}
