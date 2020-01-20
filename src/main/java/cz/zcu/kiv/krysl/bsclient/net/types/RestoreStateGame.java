package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;

public class RestoreStateGame extends RestoreState {

    private final Nickname opponent;
    private final Who onTurn;
    private final Hits playerBoardHits;
    private final Hits playerBoardMisses;
    private final Layout layout;
    private final Hits opponentBoardHits;
    private final Hits opponentBoardMisses;
    private final ShipsPlacements sunkShips;

    public RestoreStateGame(
            Nickname opponent,
            Who onTurn,
            Hits playerBoardHits,
            Hits playerBoardMisses,
            Layout layout,
            Hits opponentBoardHits,
            Hits opponentBoardMisses,
            ShipsPlacements sunkenShips) {
        this.opponent = opponent;
        this.onTurn = onTurn;
        this.playerBoardHits = playerBoardHits;
        this.playerBoardMisses = playerBoardMisses;
        this.layout = layout;
        this.opponentBoardHits = opponentBoardHits;
        this.opponentBoardMisses = opponentBoardMisses;
        this.sunkShips = sunkenShips;
    }

    public Hits getPlayerBoardHits() {
        return playerBoardHits;
    }

    public Hits getPlayerBoardMisses() {
        return playerBoardMisses;
    }

    public Hits getOpponentBoardHits() {
        return opponentBoardHits;
    }

    public Hits getOpponentBoardMisses() {
        return opponentBoardMisses;
    }

    public Nickname getOpponent() {
        return opponent;
    }

    public Layout getLayout() {
        return layout;
    }

    public ShipsPlacements getSunkShips() {
        return sunkShips;
    }

    public Who getOnTurn() {
        return onTurn;
    }

    public static RestoreStateGame deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        Nickname opponent = Nickname.deserialize(deserializer);
        Who onTurn = Who.deserialize(deserializer);
        Hits playerBoardHits = Hits.deserialize(deserializer);
        Hits playerBoardMisses = Hits.deserialize(deserializer);
        Layout layout = Layout.deserialize(deserializer);
        Hits opponentBoardHits = Hits.deserialize(deserializer);
        Hits opponentBoardMisses = Hits.deserialize(deserializer);
        ShipsPlacements sunkenShips = ShipsPlacements.deserialize(deserializer);
        return new RestoreStateGame(
                opponent,
                onTurn,
                playerBoardHits,
                playerBoardMisses,
                layout,
                opponentBoardHits,
                opponentBoardMisses,
                sunkenShips);
    }
}
