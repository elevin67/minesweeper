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
  public void testBoard(int rand, int height, int width, int numMines) {
    int won = 0;
    int lost = 0;
    long start = System.currentTimeMillis();
    for(int i = 0; i < 1000; i++) {
      Board board = new Board(height,width,numMines);
      boolean run = runOne(board,rand);
      if(run){won++;}
      else{lost++;}
    }
    long end = System.currentTimeMillis();
    long time = (end-start);
    float avgTime = (float)(time/1000.0);
    System.out.println("Time: "+time);
    System.out.println("Average time to complete: "+avgTime+" ms");
    System.out.println("Won: "+won+" Lost: "+lost);
  }

  public void runTest() {
    printOutLine();
    System.out.println("10% of board is mines");
    System.out.println("");
    System.out.println("Beginner: 10x10 with 10 bombs");
    System.out.println("Random selection when stuck.");
    testBoard(0,10,10,10);
    System.out.println("");
    System.out.println("Optimized random selection when stuck.");
    testBoard(1,10,10,10);
    System.out.println("");
    printOutLine();
    System.out.println("Intermediate: 16x16 with 26 bombs");
    System.out.println("Random selection when stuck.");
    testBoard(0,16,16,26);
    System.out.println("");
    System.out.println("Optimized random selection when stuck.");
    testBoard(1,16,16,26);
    System.out.println("");

    printOutLine();
    System.out.println("15% of board is mines");
    System.out.println("");
    System.out.println("Beginner: 8x8 with 10 bombs");
    System.out.println("Random selection when stuck.");
    testBoard(0,8,8,10);
    System.out.println("");
    System.out.println("Optimized random selection when stuck.");
    testBoard(1,8,8,10);
    System.out.println("");
    printOutLine();
    System.out.println("Intermediate: 16x16 with 40 bombs");
    System.out.println("Random selection when stuck.");
    testBoard(0,16,16,40);
    System.out.println("");
    System.out.println("Optimized random selection when stuck.");
    testBoard(1,16,16,40);
    System.out.println("");
    printOutLine();
  }

  public void printOutLine() {
    for(int i = 0; i < 50; i++) {
      System.out.print("-");
    }
    System.out.println("");
  }

  public static void main(String[] args) {
    TestSolver testSolver = new TestSolver();
    testSolver.runTest();
  }

}
