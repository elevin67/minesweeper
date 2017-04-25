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
      Board board = new Board(24,24,99);
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
//    int[][] k = {{-2,1,0,0,1,-2,2,-2},{2,2,1,0,1,2,3,2},{1,-2,1,1,1,2,-2,1},{2,2,1,1,-2,2,1,1},{-2,2,1,3,2,2,0,0},{1,2,-2,2,-2,1,0,0},{0,2,2,3,1,1,0,0},{0,1,-2,1,0,0,0,0}};
//    Board board1 = new Board(8,8,10);
//    Solver solver1 = new Solver(board1);
//    boolean finish = solver1.checkFinish(k);
//    board1.printBoard(k);
//    System.out.println("Finish? "+finish);
  }

}
