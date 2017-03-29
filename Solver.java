package minesweeper;

import java.util.*;

public class Solver {
  Board board;
  int[][] gameBoard;
  int[][] cover;
  int[][] solvedBoard;

  public Solver() {
    board = new Board(16,16,40);
    gameBoard = board.getBoard();
    cover = fillCover(new int[gameBoard.length][gameBoard[0].length]);
    solvedBoard = solveBoard(gameBoard,cover);
  }

  // puts flags (-2) on all the mines
  private int[][] solveBoard(int[][] board, int[][] cover) {
    cover = pickRandom(board,cover);
    cover = iterateBoard(board,cover);
    return null;
  }

  // iterates over the board, picking out mines
  private int[][] iterateBoard(int[][] board, int[][] cover) {
    for(int i = 0; i < board.length; i++) {
      for(int j = 0; j < board[i].length; j++) {
        if(cover[i][j]==-3) {
          continue;
        }
      }
    }
    return null;
  }

  // picks a random spot on the board to start, calls uncoverBoard to uncover location
  public static int[][] pickRandom(int[][] board, int[][] cover) {
    int randX = (int)(Math.random()*board.length);
    int randY = (int)(Math.random()*board[0].length);
    int randSpot = board[randX][randY];
    System.out.println(randX + " " + randY);
    if(randSpot==0) {
      cover = uncoverBoard(randX,randY, board, cover);
    } else if(randSpot==-1) {
      return null;
    } else {
      cover[randX][randY] = randSpot;
    }
    return cover;
  }

  // uncovers the board at the random spot (r,c)
  // works, but very inefficient
  public static int[][] uncoverBoard(int r,int c, int[][] board, int[][] cover) {
    int x = r - 1;
    int y = c - 1;
    int[][] uncoveredBoard = cover;
    for(int i = 0; i < 3; i++) {
      for(int j = 0; j < 3; j++) {
        if(i == 1 && j == 1) {
          continue;
        }
        if(x+i < 0 || x+i > board.length - 1) {
          continue;
        } else if (y+j < 0 || y+j > board[i].length - 1) {
          continue;
        } else if(board[x+i][y+j] == 0){
          if(cover[x+i][y+j] == 0) {
            continue;
          } else {
            cover[x+i][y+j] = 0;
            uncoveredBoard = uncoverBoard(x+i,y+j,board, uncoveredBoard);
          }
        } else if(board[x+i][y+j] != -3 && board[x+i][y+j] != -2 && board[x+i][y+j] != -1) {
          uncoveredBoard[x+i][y+j] = board[x+i][y+j];
        }
      }
    }
    return uncoveredBoard;
  }

  // fills the cover board with -3
  public static int[][] fillCover(int[][] board) {
    int height = board.length;
    int width = board[0].length;
    int[][] cover = new int[height][width];

    for(int i = 0; i < height; i++) {
      for(int j = 0; j < width; j++) {
        cover[i][j] = -3;
      }
    }

    return cover;
  }

  public static void main(String[] args) {
    Board board = new Board(16,16,40);
    int[][] b = board.getBoard();
    Board.printBoard(b);
    int[][] cover = fillCover(b);
    int[][] randomBoard = pickRandom(b,cover);
    if(randomBoard==null) {
      System.out.println("You lose!");
    } else {
      Board.printBoard(randomBoard);
    }
  }
}
