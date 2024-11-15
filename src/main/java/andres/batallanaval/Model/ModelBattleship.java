package andres.batallanaval.Model;

import java.util.ArrayList;
import java.util.List;

public class ModelBattleship {
    private final int BOARD_SIZE = 10;
    private CellStatus[][] board;
    private List<Ship> ships;

    public ModelBattleship() {
        board = new CellStatus[BOARD_SIZE][BOARD_SIZE];
        ships = new ArrayList<>();
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = CellStatus.EMPTY;
            }
        }
    }

    // Método para colocar un barco en el tablero
    public boolean placeShip(int startX, int startY, int length, boolean horizontal) {
        if (!isValidPlacement(startX, startY, length, horizontal)) {
            return false;
        }
        Ship ship = new Ship(startX, startY, length, horizontal);
        ships.add(ship);

        for (int i = 0; i < length; i++) {
            if (horizontal) {
                board[startX][startY + i] = CellStatus.SHIP;
            } else {
                board[startX + i][startY] = CellStatus.SHIP;
            }
        }
        return true;
    }

    private boolean isValidPlacement(int startX, int startY, int length, boolean horizontal) {
        if (horizontal && (startY + length > BOARD_SIZE)) return false;
        if (!horizontal && (startX + length > BOARD_SIZE)) return false;

        for (int i = 0; i < length; i++) {
            int x = horizontal ? startX : startX + i;
            int y = horizontal ? startY + i : startY;
            if (board[x][y] != CellStatus.EMPTY) {
                return false;
            }
        }
        return true;
    }

    public CellStatus getCellStatus(int x, int y) {
        return board[x][y];
    }

    // Define los estados posibles de las celdas en el tablero
    public enum CellStatus {
        EMPTY, SHIP, HIT, MISS, SUNK
    }

    // Clase interna Ship para almacenar la información de cada barco
    private static class Ship {
        private int startX, startY, length;
        private boolean horizontal;
        private int hits;

        public Ship(int startX, int startY, int length, boolean horizontal) {
            this.startX = startX;
            this.startY = startY;
            this.length = length;
            this.horizontal = horizontal;
            this.hits = 0;
        }

        public boolean isSunk() {
            return hits >= length;
        }

        public void hit() {
            hits++;
        }
    }
}