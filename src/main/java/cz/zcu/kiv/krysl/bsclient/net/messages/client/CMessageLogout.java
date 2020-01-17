package cz.zcu.kiv.krysl.bsclient.net.messages.client;

public class CMessageLogout extends ClientMessage {
    public CMessageLogout() {
        super(ClientMessageKind.LOGOUT);
    }
}