package cz.zcu.kiv.krysl.bsclient;

import cz.zcu.kiv.krysl.bsclient.game.Position;
import cz.zcu.kiv.krysl.bsclient.net.*;
import cz.zcu.kiv.krysl.bsclient.net.connection.Connection;
import cz.zcu.kiv.krysl.bsclient.net.message.Message;
import cz.zcu.kiv.krysl.bsclient.net.results.*;
import cz.zcu.kiv.krysl.bsclient.net.types.Layout;

import java.net.InetSocketAddress;

public class BattleshipsClient extends Client {
    private final String nickname;

    public BattleshipsClient(InetSocketAddress serverAddress, String nickname, ClientEventHandler eventHandler) {
        super(serverAddress, eventHandler);
        this.nickname = nickname;
    }

    public JoinGameResult joinGame() {
        // TODO
        return null;
    }

    public LeaveGameResult leaveGame() {
        // TODO
        return null;
    }

    public ChooseLayoutResult chooseLayout(Layout layout) {
        // TODO
        return null;
    }

    public ShootResult shoot(Position position) {
        // TODO
        return null;
    }
}
