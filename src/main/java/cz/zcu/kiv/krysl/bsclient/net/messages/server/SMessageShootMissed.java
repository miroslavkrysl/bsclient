package cz.zcu.kiv.krysl.bsclient.net.messages.server;

public class SMessageShootMissed extends ServerMessage {

    public SMessageShootMissed() {
        super(ServerMessageKind.SHOOT_MISSED);
    }
}
