package cz.zcu.kiv.krysl.bsclient.net.message.server;

public class SMessageDisconnect extends ServerMessage {

    public SMessageDisconnect() {
        super(ServerMessageKind.DISCONNECT);
    }
}
