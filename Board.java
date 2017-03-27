package minesweeper;

import java.util.*;

public class Board {
  int[][] board;
  int height;
  int width;
  int numMines;

  public Board(int h, int w, int nM) {
    height = h;
    width = w;
    numMines = nM;
    board = new int[height][width];
    board = fillBoardBruteForce(board,numMines);
  }

  public int[][] getBoard() {
    return board;
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public int getNumMines() {
    return numMines;
  }

  /**
   * fills a Minesweeper board with mines and corresponding numbers, returns as a 2d array
   *
   * @param  int[][] 2d array with predetermined height and width
   * @return int[][] Minesweeper board with everything filled in
   */
  private int[][] fillBoardBruteForce(int[][] board, int numMines) {
    board = addMines(board,numMines);
    board = addNumbersBruteForce(board);
    return board;
  }


  /**
   * randomly places a passed in number of mines, represented by -1, into a 2d array
   *
   * @param  int[][] board 2d array that represents the board with predetermined height and width
   * @param  int numMines  number of mines to randomly distribute on board
   * @return int[][] returns an array with distributed mines
   */
  private int[][] addMines(int[][] board, int numMines) {
    int width = board[0].length;
    int height = board.length;
    int[][] boardWithMines = new int[width][height];
    for(int i = 0; i < numMines; i++) {
      int x = (int)((Math.random()*width));
      int y = (int)((Math.random()*height));
      if(boardWithMines[x][y]==-1) {
        i--;
        continue;
      } else {
        boardWithMines[x][y] = -1;
      }
    }
    return boardWithMines;
  }


  /**
   * fills a board that already has randomly dispersed mines with their corresponding numbers
   *
   * @param  int[][] board passed in board with mines already randomly dispersed
   * @return int[][] board with corresponding numbers filled in
   */
   // should be O(n^2)
  private int[][] addNumbersBruteForce(int[][] board) {
    int[][] boardWithNumbers = new int[board.length][board[0].length];
    //brute force solution
    for(int i = 0; i < board.length; i++) {
      for(int j = 0; j < board[i].length; j++) {
        boardWithNumbers[i][j] = checkMine(i,j,board);
      }
    }
    return boardWithNumbers;
  }

  // something dynamic like fibonacci example??
  // look for bombs first, duh!
  // this should be O(nlogn)
  // in progress
  private int[][] addNumbersDivideConquer(int[][] board) {
    int[][] boardWithNumbers = new int[board.length][board[0].length];
    for(int i = 0; i < board.length; i++) {
      for(int j = 0; j < board[i].length; i++) {

      }
    }
    return null;
  }

  // in progress
  private void heightRecurse(int[][] board) {
  }

  // checks adjacent tiles for mines, returns number surrounding specific tile
  private int checkMine(int r, int c, int[][] board) {
    if(board[r][c]==-1) {
      return -1;
    }
    int x = r - 1;
    int y = c - 1;
    int count = 0;
    for(int i = 0; i < 3; i++) {
      for(int j = 0; j < 3; j++) {
        if(i == 1 && j == 1) {
          continue;
        }
        if(x+i < 0 || x+i > board.length - 1) {
          continue;
        } else if (y+j < 0 || y+j > board[i].length - 1) {
          continue;
        }
        else if(board[x+i][y+j]==-1) {
          count++;
        }
      }
    }

    return count;
  }

  // prints out a minesweeper board
  public static void printBoard(int[][] board) {
    for(int i = 0; i < board.length; i++) {
      for(int j = 0; j < board[i].length; j++) {
        if(board[i][j] < 0) {
          System.out.print(" "+board[i][j]+"  ");
        } else {
          System.out.print("  "+board[i][j]+"  ");
        }
      }
      System.out.println("");
    }
    System.out.println("");
  }

  public static void main(String[] args) {
    Board boardGenerator = new Board(5,5,5);
    printBoard(boardGenerator.getBoard());
  }
}
