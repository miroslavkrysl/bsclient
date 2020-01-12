package cz.zcu.kiv.krysl.bsclient.net.messages.server;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.types.RestoreState;

public class SMessageReconnectOk extends ServerMessage {

    private RestoreState state;

    public SMessageReconnectOk(RestoreState state) {
        super(ServerMessageKind.RECONNECT_OK);
        this.state = state;
    }

    public RestoreState getState() {
        return state;
    }

    public static SMessageReconnectOk deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        RestoreState state = RestoreState.deserialize(deserializer);
        return new SMessageReconnectOk(state);
    }

}
