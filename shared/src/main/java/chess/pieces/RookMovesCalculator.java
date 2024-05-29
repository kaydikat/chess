package chess.pieces;

public class RookMovesCalculator extends MovesCalculator {
  @Override
  protected int[][] getDirections() {
    return new int[][]{
            {-1, 0}, // Upwards
            {1, 0},  // Downwards
            {0, -1}, // Leftwards
            {0, 1}   // Rightwards
    };
  }
}
