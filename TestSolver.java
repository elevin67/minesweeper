package minesweeper;

import java.util.*;

public class TestSolver {

  public TestSolver() {

  }

  public boolean runOneRandom(Board board) {
    Solver solver = new Solver(board);

    Boolean result = solver.solveBoard(solver.getGameBoard(),solver.getCover());
    if(result) {
//      System.out.println("you win!");
      return true;
    } else {
//      System.out.println("you lost");
      return false;
    }
  }

  public static void main(String[] args) {
    TestSolver testSolver = new TestSolver();
    int won = 0;
    int lost = 0;
    for(int i = 0; i < 1000; i++) {
      Board board = new Board(16,16,40);
      boolean run = testSolver.runOneRandom(board);
      if(run){won++;}
      else{lost++;}
    }
    System.out.println("Won: "+won+" Lost: "+lost);

  }

}
