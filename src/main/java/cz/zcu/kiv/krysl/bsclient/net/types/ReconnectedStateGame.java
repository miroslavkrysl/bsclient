package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadDeserializer;

public class ReconnectedStateGame extends ReconnectedState {

    private Who onTurn;
    private Hits playerBoard;
    private Hits opponentBoard;
    private SunkShips sunkenShips;

    public ReconnectedStateGame(Who onTurn, Hits playerBoard, Hits opponentBoard, SunkShips sunkenShips) {
        this.onTurn = onTurn;
        this.playerBoard = playerBoard;
        this.opponentBoard = opponentBoard;
        this.sunkenShips = sunkenShips;
    }

    public Hits getPlayerBoard() {
        return playerBoard;
    }

    public Hits getOpponentBoard() {
        return opponentBoard;
    }

    public SunkShips getSunkenShips() {
        return sunkenShips;
    }

    public Who getOnTurn() {
        return onTurn;
    }

    public static ReconnectedState deserialize(PayloadDeserializer deserializer) throws DeserializeException {
        Who onTurn = Who.deserialize(deserializer);
        Hits playerBoard = Hits.deserialize(deserializer);
        Hits opponentBoard = Hits.deserialize(deserializer);
        SunkShips sunkenShips = SunkShips.deserialize(deserializer);
        return new ReconnectedStateGame(onTurn, playerBoard, opponentBoard, sunkenShips);
    }
}
