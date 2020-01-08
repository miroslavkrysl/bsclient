package cz.zcu.kiv.krysl.bsclient.net.messages;

public class ClientMessage {
    private ClientMessageKind kind;

    public ClientMessage(ClientMessageKind kind) {
        this.kind = kind;
    }
}
