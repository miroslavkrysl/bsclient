package cz.zcu.kiv.krysl.bsclient.game;

import java.util.List;

public class Board {
    private Cell[][] map;
    private int size;
    private List<Ship> ships;

    public Board(int size) {
        this.size = size;
        this.map = new Cell[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.map[i][j] = new Cell();
            }
        }
    }

    public Cell getCell(Position position) {
        return map[position.getCol()][position.getRow()];
    }

    public int getSize() {
        return size;
    }

    public void placeShip(Ship ship, Placement placement) throws InvalidPlacementException {
        if (isInBounds(ship, placement)) {
            throw new InvalidPlacementException("Ship is out of bounds.");
        }

        if (isTooClose(ship, placement)) {
            throw new InvalidPlacementException("Ship is too close to another ship.");
        }
    }

    private boolean isTooClose(Ship ship, Placement placement) {
        for (Ship s :
                this.ships) {
            Position[] positions = ship.getPositions();
        }
    }

    private boolean isInBounds(Ship ship, Placement placement) {
        return false;
    }
}
