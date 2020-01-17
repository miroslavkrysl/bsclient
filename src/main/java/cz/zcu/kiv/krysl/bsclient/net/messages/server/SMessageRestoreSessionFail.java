package cz.zcu.kiv.krysl.bsclient.net.messages.server;

public class SMessageRestoreSessionFail extends ServerMessage {

    SMessageRestoreSessionFail() {
        super(ServerMessageKind.RESTORE_SESSION_FAIL);
    }
}
