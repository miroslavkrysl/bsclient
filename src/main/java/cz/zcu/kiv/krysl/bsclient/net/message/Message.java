package cz.zcu.kiv.krysl.bsclient.net.message;

public abstract class Message {

    private final MessageKind kind;

    protected Message(MessageKind kind) {
        this.kind = kind;
    }

    public MessageKind getKind() {
        return kind;
    }
}
