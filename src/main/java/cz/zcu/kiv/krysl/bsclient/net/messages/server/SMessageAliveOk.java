package cz.zcu.kiv.krysl.bsclient.net.messages.server;

public class SMessageAliveOk extends ServerMessage {

    public SMessageAliveOk() {
        super(ServerMessageKind.ALIVE_OK);
    }
}
