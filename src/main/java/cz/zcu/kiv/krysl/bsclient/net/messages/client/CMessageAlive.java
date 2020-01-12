package cz.zcu.kiv.krysl.bsclient.net.messages.client;

public class CMessageAlive extends ClientMessage {
    public CMessageAlive() {
        super(ClientMessageKind.ALIVE);
    }
}
