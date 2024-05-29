package chess.pieces;

public class BishopMovesCalculator extends MovesCalculator {
    @Override
    protected int[][] getDirections() {
        return new int[][]{
                {-1, -1}, // Diagonally upwards and to the left
                {-1, 1},  // Diagonally upwards and to the right
                {1, -1},  // Diagonally downwards and to the left
                {1, 1}    // Diagonally downwards and to the right
        };
    }
}