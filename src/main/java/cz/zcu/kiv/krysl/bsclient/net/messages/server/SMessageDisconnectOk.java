package cz.zcu.kiv.krysl.bsclient.net.messages.server;

public class SMessageDisconnectOk extends ServerMessage {

    public SMessageDisconnectOk() {
        super(ServerMessageKind.DISCONNECT_OK);
    }
}
