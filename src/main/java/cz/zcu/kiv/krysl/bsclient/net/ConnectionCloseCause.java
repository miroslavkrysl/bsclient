package cz.zcu.kiv.krysl.bsclient.net;

public enum ConnectionCloseCause {
    SERVER_CLOSED,
    SERVER_UNAVAILABLE,
    STREAM_CORRUPTED,
    UNKNOWN
}
