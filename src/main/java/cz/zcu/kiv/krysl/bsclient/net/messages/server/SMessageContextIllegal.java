package cz.zcu.kiv.krysl.bsclient.net.messages.server;

public class SMessageContextIllegal extends ServerMessage {

    SMessageContextIllegal() {
        super(ServerMessageKind.CONTEXT_ILLEGAL);
    }
}
