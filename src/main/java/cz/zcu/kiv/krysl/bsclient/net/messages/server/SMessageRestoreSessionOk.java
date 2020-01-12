package cz.zcu.kiv.krysl.bsclient.net.messages.server;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.types.RestoreState;

public class SMessageRestoreSessionOk extends ServerMessage {

    private RestoreState state;

    public SMessageRestoreSessionOk(RestoreState state) {
        super(ServerMessageKind.RESTORE_SESSION_OK);
        this.state = state;
    }

    public RestoreState getState() {
        return state;
    }

    public static SMessageRestoreSessionOk deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        RestoreState state = RestoreState.deserialize(deserializer);
        return new SMessageRestoreSessionOk(state);
    }

}
