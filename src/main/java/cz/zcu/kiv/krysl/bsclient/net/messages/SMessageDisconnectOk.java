package cz.zcu.kiv.krysl.bsclient.net.messages;

public class SMessageDisconnectOk extends ServerMessage {
    public SMessageDisconnectOk() {
        super(ServerMessageKind.DISCONNECT_OK);
    }
}