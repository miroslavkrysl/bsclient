package cz.zcu.kiv.krysl.bsclient.net.message;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;

public enum MessageKind {
    // ---ClientMessage---

    C_ALIVE("alive"),
    C_HANDSHAKE("handshake"),
    C_RECONNECT("reconnect"),
    C_LOGIN("login"),
    C_JOIN_GAME("join_game"),
    C_LAYOUT("layout"),
    C_SHOOT("shoot"),
    C_LEAVE_GAME("leave"),
    C_DISCONNECT("disconnect"),

    // ---ServerMessage---

    S_ILLEGAL_MESSAGE("illegal"),

    S_HANDSHAKE_OK("handshake_ok"),
    S_HANDSHAKE_FAIL("handshake_fail"),

    S_RECONNECT_OK("reconnect_ok"),
    S_RECONNECT_FAIL("reconnect_fail"),

    S_LOGIN_OK("login_ok"),
    S_LOGIN_FAIL("login_fail"),

    S_JOIN_GAME_WAIT("join_game_wait"),
    S_JOIN_GAME_JOINED("join_game_joined"),

    S_LAYOUT_OK("layout_ok"),
    S_LAYOUT_FAIL("layout_fail"),

    S_SHOOT_HIT("shoot_hit"),
    S_SHOOT_MISSED("shoot_missed"),
    S_SHOOT_FAIL("shoot_fail"),

    S_LEAVE_GAME_OK("leave_game_ok"),

    S_DISCONNECT_OK("disconnect_ok"),

    S_DISCONNECT("disconnect"),
    S_OPPONENT_JOINED("opponent_joined"),
    S_OPPONENT_READY("opponent_ready"),
    S_OPPONENT_LEFT("opponent_left"),
    S_OPPONENT_MISSED("opponent_missed"),
    S_OPPONENT_HIT("opponent_hit"),
    S_GAME_OVER("game_over");

    /**
     * Message header used for serialization.
     */
    private final String messageHeader;

    /**
     * Construct an enum variant using the given message header.
     * 
     * @param messageHeader Message header used for serialization.
     */
    MessageKind(String messageHeader) {
        this.messageHeader = messageHeader;
    }
    
    public static MessageKind fromHeader(String messageCode) throws DeserializeException {
        for (MessageKind kind : values()) {
            if (kind.messageHeader.equals(messageCode)) {
                return kind;
            }
        }

        throw new DeserializeException("Can't deserialize message kind from given string");
    }

    /**
     * Get message header to use for serialization.
     *
     * @return The message header.
     */
    public String toHeader() {
        return messageHeader;
    }

    /**
     * Get message kind group based on message source and character.
     * 
     * @return The message kind group.
     */
    public MessageGroup getGroup() {
        switch (this) {
            case C_ALIVE:
            case C_HANDSHAKE:
            case C_RECONNECT:
            case C_LOGIN:
            case C_JOIN_GAME:
            case C_LAYOUT:
            case C_SHOOT:
            case C_LEAVE_GAME:
            case C_DISCONNECT:
                return MessageGroup.CLIENT_REQUEST;
            case S_ILLEGAL_MESSAGE:
            case S_HANDSHAKE_OK:
            case S_HANDSHAKE_FAIL:
            case S_RECONNECT_OK:
            case S_RECONNECT_FAIL:
            case S_LOGIN_OK:
            case S_LOGIN_FAIL:
            case S_JOIN_GAME_WAIT:
            case S_JOIN_GAME_JOINED:
            case S_LAYOUT_OK:
            case S_LAYOUT_FAIL:
            case S_SHOOT_HIT:
            case S_SHOOT_MISSED:
            case S_SHOOT_FAIL:
            case S_LEAVE_GAME_OK:
            case S_DISCONNECT_OK:
                return MessageGroup.SERVER_RESPONSE;
            case S_DISCONNECT:
            case S_OPPONENT_JOINED:
            case S_OPPONENT_READY:
            case S_OPPONENT_LEFT:
            case S_OPPONENT_MISSED:
            case S_OPPONENT_HIT:
            case S_GAME_OVER:
                return MessageGroup.SERVER_NOTIFICATION;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
