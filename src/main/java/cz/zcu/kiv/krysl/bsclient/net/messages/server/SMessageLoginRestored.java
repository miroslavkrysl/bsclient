package cz.zcu.kiv.krysl.bsclient.net.messages.server;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.types.RestoreState;

public class SMessageLoginRestored extends ServerMessage {

    private RestoreState state;

    public SMessageLoginRestored(RestoreState state) {
        super(ServerMessageKind.LOGIN_RESTORED);
        this.state = state;
    }

    public RestoreState getState() {
        return state;
    }

    public static SMessageLoginRestored deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        RestoreState state = RestoreState.deserialize(deserializer);
        return new SMessageLoginRestored(state);
    }

}
