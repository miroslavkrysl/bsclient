package cz.zcu.kiv.krysl.bsclient.net.messages;

import cz.zcu.kiv.krysl.bsclient.net.types.Position;

public class CMessageShoot extends ClientMessage {
    private final Position position;

    public CMessageShoot(Position position) {
        super(ClientMessageKind.SHOOT);
        this.position = position;
    }
}