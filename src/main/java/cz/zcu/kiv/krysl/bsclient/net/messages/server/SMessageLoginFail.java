package cz.zcu.kiv.krysl.bsclient.net.messages.server;

public class SMessageLoginFail extends ServerMessage {

    public SMessageLoginFail() {
        super(ServerMessageKind.LOGIN_FAIL);
    }
}