package cz.zcu.kiv.krysl.bsclient.net.messages.server;

public class SMessageIllegalState extends ServerMessage {

    SMessageIllegalState() {
        super(ServerMessageKind.ILLEGAL_STATE);
    }
}
