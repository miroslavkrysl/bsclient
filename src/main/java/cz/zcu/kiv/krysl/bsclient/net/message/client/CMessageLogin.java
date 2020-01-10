package cz.zcu.kiv.krysl.bsclient.net.message.client;

import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;
import cz.zcu.kiv.krysl.bsclient.net.message.items.Nickname;

public class CMessageLogin extends ClientMessage {
    private final Nickname nickname;

    public CMessageLogin(Nickname nickname) {
        super(ClientMessageKind.LOGIN);
        this.nickname = nickname;
    }

    public Nickname getNickname() {
        return nickname;
    }

    @Override
    protected void serializePayload(PayloadSerializer serializer) {
        serializer.addString(nickname.getValue());
    }
}
