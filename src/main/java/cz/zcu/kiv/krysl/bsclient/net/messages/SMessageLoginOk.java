package cz.zcu.kiv.krysl.bsclient.net.messages;

public class SMessageLoginOk extends ServerMessage {
    public SMessageLoginOk() {
        super(ServerMessageKind.LOGIN_OK);
    }
}