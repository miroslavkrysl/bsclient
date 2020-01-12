package cz.zcu.kiv.krysl.bsclient.net.codec;

import cz.zcu.kiv.krysl.bsclient.net.messages.client.ClientMessageKind;

public class MessageSerializer {
    private ClientMessageKind kind;
    private PayloadSerializer payloadSerializer;

    public MessageSerializer(ClientMessageKind kind) {
        this.kind = kind;
        this.payloadSerializer = new PayloadSerializer();
    }

    public PayloadSerializer getPayloadSerializer() {
        return payloadSerializer;
    }

    public String serialize() {
        StringBuilder serialized = new StringBuilder();

        serializeHeader(serialized);
        String payload = payloadSerializer.serialize();

        if (payload != null) {
            serialized.append(Constants.PAYLOAD_START);
            serialized.append(payload);
        }

        return serialized.toString();
    }

    private void serializeHeader(StringBuilder serialized) {
        String header;

        switch (kind) {
            case ALIVE:
                header = "alive";
                break;
            case RECONNECT:
                header = "reconnect";
                break;
            case LOGIN:
                header = "login";
                break;
            case JOIN_GAME:
                header = "join_game";
                break;
            case LAYOUT:
                header = "layout";
                break;
            case SHOOT:
                header = "shoot";
                break;
            case LEAVE_GAME:
                header = "leave_game";
                break;
            case DISCONNECT:
                header = "disconnect";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + kind);
        }

        serialized.append(header);
    }
}
