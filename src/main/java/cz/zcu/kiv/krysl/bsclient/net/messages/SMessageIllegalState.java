package cz.zcu.kiv.krysl.bsclient.net.messages;

public class SMessageIllegalState extends ServerMessage {
    public SMessageIllegalState() {
        super(ServerMessageKind.ILLEGAL_STATE);
    }
}