package minesweeper;

import java.util.*;

public class TestSolver {

  public TestSolver() {

  }

  public void runOneRandom() {
    Board board = new Board(16,16,40);
    Solver solver = new Solver(board);
    Boolean result = solver.solveBoard(solver.getGameBoard(),solver.getCover());
    if(result) {
      System.out.println("you win!");
    } else {
      System.out.println("you lost");
    }
  }

  public static void main(String[] args) {

  //   int[][] i = {{1,1,-1},{-1,2,1},{2,1,1}};
  //   int[][] j = {{-3,-3,-2},{-3,2,-3},{-3,-3,-3}};
  //   int[][] b = board.getBoard();
  //   board.printBoard(b);
  //   Solver solver = new Solver();
  //   int[][] cover = solver.getCover();
  //   int[][] randomBoard = solver.pickRandom(b, cover);
  //   if(randomBoard==null) {
  //     System.out.println("You lose!");
  //   } else {
  //     board.printBoard(randomBoard);
  //   }
  //   board.printBoard(i);
  //   board.printBoard(j);
  //   int[][] k = solver.iterateRandomTile(1,1,i,j);
  //   board.printBoard(k);
    TestSolver testSolver = new TestSolver();
    testSolver.runOneRandom();
  }

}
