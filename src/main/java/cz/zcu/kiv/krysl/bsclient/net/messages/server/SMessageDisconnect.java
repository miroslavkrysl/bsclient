package cz.zcu.kiv.krysl.bsclient.net.messages.server;

public class SMessageDisconnect extends ServerMessage {

    public SMessageDisconnect() {
        super(ServerMessageKind.DISCONNECT);
    }
}
