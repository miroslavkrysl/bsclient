package cz.zcu.kiv.krysl.bsclient.net.messages;

import cz.zcu.kiv.krysl.bsclient.net.types.Nickname;

public class CMessageLogin extends ClientMessage {
    private final Nickname nickname;

    public CMessageLogin(Nickname nickname) {
        super(ClientMessageKind.LOGIN);
        this.nickname = nickname;
    }
}
