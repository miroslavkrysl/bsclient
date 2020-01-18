package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;

public class RestoreStateGame extends RestoreState {

    private final Nickname opponent;
    private final Who onTurn;
    private final Hits playerBoard;
    private final Layout layout;
    private final Hits opponentBoard;
    private final ShipsPlacements sunkShips;

    public RestoreStateGame(Nickname opponent, Who onTurn, Hits playerBoard, Layout layout, Hits opponentBoard, ShipsPlacements sunkenShips) {
        this.opponent = opponent;
        this.onTurn = onTurn;
        this.playerBoard = playerBoard;
        this.layout = layout;
        this.opponentBoard = opponentBoard;
        this.sunkShips = sunkenShips;
    }

    public Nickname getOpponent() {
        return opponent;
    }

    public Hits getPlayerBoard() {
        return playerBoard;
    }

    public Hits getOpponentBoard() {
        return opponentBoard;
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
        Hits playerBoard = Hits.deserialize(deserializer);
        Layout layout = Layout.deserialize(deserializer);
        Hits opponentBoard = Hits.deserialize(deserializer);
        ShipsPlacements sunkenShips = ShipsPlacements.deserialize(deserializer);
        return new RestoreStateGame(opponent, onTurn, playerBoard, layout, opponentBoard, sunkenShips);
    }
}
