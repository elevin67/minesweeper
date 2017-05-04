package minesweeper;

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
    board = fillBoard(board,numMines);
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

 // fills a Minesweeper board with mines and corresponding numbers, returns as a 2d array
  private int[][] fillBoard(int[][] board, int numMines) {
    board = addMinesRandom(board,numMines);
    board = addMinesOptRandom(board,numMines);
    board = addNumbers(board);
    return board;
  }

  // randomly places a passed in number of mines, represented by -1, into a 2d array
  private int[][] addMinesRandom(int[][] board, int numMines) {
    int width = board[0].length;
    int height = board.length;
    int[][] boardWithMines = new int[width][height];
    for(int i = 0; i < numMines/2; i++) {
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
   * randomly places a passed in number of mines, but makes sure that there are no neighboring mines.
   * This eliminates the cases where the game gets too impossible or too easy.
   * mines are represented by -1, into a 2d array
   */
  private int[][] addMinesOptRandom(int[][] board, int numMines) {

    for(int i = 0; i < numMines/2; i++) {
      int x = (int)((Math.random()*width));
      int y = (int)((Math.random()*height));
      if((board[x][y]==-1)||(checkMine(x,y,board)>1)) {
        i--;
        continue;
      } else {
        board[x][y] = -1;
      }
    }
    return board;
  }

  // fills a board that already has randomly dispersed mines with their corresponding numbers
  private int[][] addNumbers(int[][] board) {
    int[][] boardWithNumbers = new int[board.length][board[0].length];
    for(int i = 0; i < board.length; i++) {
      for(int j = 0; j < board[i].length; j++) {
        boardWithNumbers[i][j] = checkMine(i,j,board);
      }
    }
    return boardWithNumbers;
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
  public void printBoard(int[][] board) {
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
}
