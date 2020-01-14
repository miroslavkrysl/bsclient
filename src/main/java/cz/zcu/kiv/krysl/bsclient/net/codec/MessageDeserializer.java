package cz.zcu.kiv.krysl.bsclient.net.codec;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.messages.server.ServerMessageKind;

public class MessageDeserializer {

    private final ServerMessageKind kind;
    private final PayloadDeserializer payloadDeserializer;

    public MessageDeserializer(String serialized) throws DeserializeException {
        // deserialize header
        int payloadStart = serialized.indexOf(Constants.PAYLOAD_START);

        String header;

        if (payloadStart < 0) {
            // no payload
            header = serialized;
            this.payloadDeserializer = new PayloadDeserializer();
        } else {
            // some payload
            header = serialized.substring(0, payloadStart);
            this.payloadDeserializer = new PayloadDeserializer(serialized.substring(payloadStart + 1));
        }

        this.kind = deserializeHeader(header);
    }

    private ServerMessageKind deserializeHeader(String header) throws DeserializeException {
        switch (header) {
            case "illegal_state":
                return ServerMessageKind.ILLEGAL_STATE;
            case "alive_ok":
                return ServerMessageKind.ALIVE_OK;
            case "restore_session_ok":
                return ServerMessageKind.RESTORE_SESSION_OK;
            case "restore_session_fail":
                return ServerMessageKind.RESTORE_SESSION_FAIL;
            case "login_ok":
                return ServerMessageKind.LOGIN_OK;
            case "login_fail":
                return ServerMessageKind.LOGIN_FAIL;
            case "join_game_wait":
                return ServerMessageKind.JOIN_GAME_WAIT;
            case "join_game_ok":
                return ServerMessageKind.JOIN_GAME_OK;
            case "layout_ok":
                return ServerMessageKind.LAYOUT_OK;
            case "layout_fail":
                return ServerMessageKind.LAYOUT_FAIL;
            case "shoot_hit":
                return ServerMessageKind.SHOOT_HIT;
            case "shoot_sunk":
                return ServerMessageKind.SHOOT_SUNK;
            case "shoot_missed":
                return ServerMessageKind.SHOOT_MISSED;
            case "leave_game_ok":
                return ServerMessageKind.LEAVE_GAME_OK;
            case "disconnect_ok":
                return ServerMessageKind.DISCONNECT_OK;
            case "disconnect":
                return ServerMessageKind.DISCONNECT;
            case "opponent_joined":
                return ServerMessageKind.OPPONENT_JOINED;
            case "opponent_ready":
                return ServerMessageKind.OPPONENT_READY;
            case "opponent_left":
                return ServerMessageKind.OPPONENT_LEFT;
            case "opponent_missed":
                return ServerMessageKind.OPPONENT_MISSED;
            case "opponent_hit":
                return ServerMessageKind.OPPONENT_HIT;
            case "game_over":
                return ServerMessageKind.GAME_OVER;
            default:
                throw new DeserializeException("Can't deserialize ServerMessageKind from message header.");
        }
    }


    public ServerMessageKind getKind() {
        return kind;
    }

    public PayloadDeserializer getPayloadDeserializer() {
        return payloadDeserializer;
    }
}
