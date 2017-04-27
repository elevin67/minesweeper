package minesweeper;

import java.util.Timer;


public class TestSolver {

  public TestSolver() {

  }

  public boolean runOne(Board board, int rand) {
    Solver solver = new Solver(board);
    Boolean result = false;
    if(rand==0) {
      result = solver.solveRandomBoard(solver.getGameBoard(), solver.getCover());
    } else {
      result = solver.solveOptimizedBoard(solver.getGameBoard(), solver.getCover());
    }
    if(result) {
      return true;
    } else {
      return false;
    }
  }

  // Beginner: 8x8 10 mines
  // Intermediate: 16x16 40 mines
  // Expert: 24x24 99 mines
  public void test(int rand) {
    int won = 0;
    int lost = 0;
    long start = System.currentTimeMillis();
    for(int i = 0; i < 10000; i++) {
      Board board = new Board(8,8,10);
      boolean run = runOne(board,rand);
      if(run){won++;}
      else{lost++;}
    }
    long end = System.currentTimeMillis();
    long time = (end-start);
    float avgTime = (float)(time/10000.0);
    System.out.println("Time: "+time);
    System.out.println("Average time to complete: "+avgTime+" ms");
    System.out.println("Won: "+won+" Lost: "+lost);
  }

  public static void main(String[] args) {
    TestSolver testSolver = new TestSolver();
    testSolver.test(0);
    testSolver.test(1);
  }

}
