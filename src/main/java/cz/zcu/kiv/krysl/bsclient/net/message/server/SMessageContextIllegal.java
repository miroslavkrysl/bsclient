package cz.zcu.kiv.krysl.bsclient.net.message.server;

public class SMessageContextIllegal extends ServerMessage {

    SMessageContextIllegal() {
        super(ServerMessageKind.CONTEXT_ILLEGAL);
    }
}
