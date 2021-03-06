package cz.zcu.kiv.krysl.bsclient.net.messages.server;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.MessageDeserializer;

public abstract class ServerMessage {
    private ServerMessageKind kind;

    public ServerMessage(ServerMessageKind kind) {
        this.kind = kind;
    }

    public final ServerMessageKind getKind() {
        return kind;
    }

    public static ServerMessage deserialize(String serialized) throws DeserializeException {
        MessageDeserializer deserializer = new MessageDeserializer(serialized);
        ServerMessageKind kind = deserializer.getKind();

        ServerMessage message;

        switch (kind) {
            case ILLEGAL_STATE:
                message = new SMessageIllegalState();
                break;
            case ALIVE_OK:
                message = new SMessageAliveOk();
                break;
            case LOGIN_OK:
                message = new SMessageLoginOk();
                break;
            case LOGIN_FULL:
                message = new SMessageLoginFull();
                break;
            case LOGIN_TAKEN:
                message = new SMessageLoginTaken();
                break;
            case LOGIN_RESTORED:
                message = SMessageLoginRestored.deserialize(deserializer.getPayloadDeserializer());
                break;
            case JOIN_GAME_WAIT:
                message = new SMessageJoinGameWait();
                break;
            case JOIN_GAME_OK:
                message = SMessageJoinGameOk.deserialize(deserializer.getPayloadDeserializer());
                break;
            case LAYOUT_OK:
                message = new SMessageLayoutOk();
                break;
            case LAYOUT_FAIL:
                message = new SMessageLayoutFail();
                break;
            case SHOOT_HIT:
                message = new SMessageShootHit();
                break;
            case SHOOT_SUNK:
                message = SMessageShootSunk.deserialize(deserializer.getPayloadDeserializer());
                break;
            case SHOOT_MISSED:
                message = new SMessageShootMissed();
                break;
            case LEAVE_GAME_OK:
                message = new SMessageLeaveGameOk();
                break;
            case LOGOUT_OK:
                message = new SMessageLogoutOk();
                break;
            case DISCONNECT:
                message = new SMessageDisconnect();
                break;
            case OPPONENT_JOINED:
                message = SMessageOpponentJoined.deserialize(deserializer.getPayloadDeserializer());
                break;
            case OPPONENT_READY:
                message = new SMessageOpponentReady();
                break;
            case OPPONENT_OFFLINE:
                message = new SMessageOpponentOffline();
                break;
            case OPPONENT_LEFT:
                message = new SMessageOpponentLeft();
                break;
            case OPPONENT_MISSED:
                message = SMessageOpponentMissed.deserialize(deserializer.getPayloadDeserializer());
                break;
            case OPPONENT_HIT:
                message = SMessageOpponentHit.deserialize(deserializer.getPayloadDeserializer());
                break;
            case GAME_OVER:
                message = SMessageGameOver.deserialize(deserializer.getPayloadDeserializer());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + kind);
        }

        return message;
    }

    public boolean isResponse() {
        return kind.isResponse();
    }
}