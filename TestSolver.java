package minesweeper;

import java.util.*;

public class TestSolver {
  public static void main(String[] args) {
    Board board = new Board(30,16,40);
    int[][] i = {{-3,-3,-3},{-2,1,-3},{-3,-3,-3}};
    int[][] j = {{0,0,0},{-1,1,0},{0,0,0}};
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
    int[][] k = solver.iterateTile(1,1,j,i);
    board.printBoard(k);
  }
}
