package cz.zcu.kiv.krysl.bsclient.net.messages.server;

public class SMessageLoginTaken extends ServerMessage {

    public SMessageLoginTaken() {
        super(ServerMessageKind.LOGIN_TAKEN);
    }
}
