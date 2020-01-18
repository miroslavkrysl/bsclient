package cz.zcu.kiv.krysl.bsclient.net.client;

import cz.zcu.kiv.krysl.bsclient.net.types.Nickname;
import cz.zcu.kiv.krysl.bsclient.net.types.Position;
import cz.zcu.kiv.krysl.bsclient.net.types.Who;

public interface IClientEventHandler {
    void handleOpponentJoined(Nickname opponent);

    void handleOpponentReady();

    void handleOpponentLeft();

    void handleOpponentMissed(Position position);

    void handleOpponentHit(Position position);

    void handleGameOver(Who winner);
}
