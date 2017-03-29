package minesweeper;

import java.util.*;

public class TestSolver {
  public static void main(String[] args) {
    Board board = new Board(16,16,40);
    int[][] b = board.getBoard();
    board.printBoard(b);
    Solver solver = new Solver();
    int[][] cover = solver.getCover();
    int[][] randomBoard = solver.pickRandom(b, cover);
    if(randomBoard==null) {
      System.out.println("You lose!");
    } else {
      board.printBoard(randomBoard);
    }
  }
}
