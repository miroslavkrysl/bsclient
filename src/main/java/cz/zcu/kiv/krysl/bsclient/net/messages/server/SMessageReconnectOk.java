package cz.zcu.kiv.krysl.bsclient.net.messages.server;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;
import cz.zcu.kiv.krysl.bsclient.net.types.Placement;
import cz.zcu.kiv.krysl.bsclient.net.types.ReconnectedState;
import cz.zcu.kiv.krysl.bsclient.net.types.ShipId;

public class SMessageReconnectOk extends ServerMessage {

    private ReconnectedState state;

    public SMessageReconnectOk(ReconnectedState state) {
        super(ServerMessageKind.RECONNECT_OK);
        this.state = state;
    }

    public ReconnectedState getState() {
        return state;
    }

    public static SMessageReconnectOk deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        ReconnectedState state = ReconnectedState.deserialize(deserializer);
        return new SMessageReconnectOk(state);
    }

}
