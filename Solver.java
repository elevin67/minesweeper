package minesweeper;

import java.util.*;
import java.util.ArrayList;

public class Solver {
  Board board;
  int[][] gameBoard;
  int[][] cover;
  int[][] solvedBoard;

  public Solver() {
    board = new Board(30,16,40);
    gameBoard = board.getBoard();
    cover = fillCover(new int[gameBoard.length][gameBoard[0].length]);
    //solvedBoard = solveBoard(gameBoard,cover);
  }

  public int[][] getGameBoard() {
    return gameBoard;
  }

  public int[][] getCover() {
    return cover;
  }

  public int[][] getSolvedBoard() {
    return solvedBoard;
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
        if(cover[i][j]>0) {
          cover = iterateTile(i,j,board,cover);
        }
      }
    }
    return cover;
  }

  // just for one tile. Will need a function that runs over all tiles until no obvious moves are left available
  // works!
  public int[][] iterateTile(int r, int c, int[][] board, int[][] cover) {
    int x = r - 1;
    int y = c - 1;
    int value = board[r][c];
    int unknown = 0;
    int flagged = 0;
    ArrayList<Point> points = new ArrayList<Point>();
    for(int i=0; i<3; i++) {
      for(int j=0; j<3; j++) {
        if(i == 1 && j == 1) {
          continue;
        }
        if(x+i < 0 || x+i > board.length - 1) {
          continue;
        } else if (y+j < 0 || y+j > board[i].length - 1) {
          continue;
        } else if (cover[x+i][y+j]==-3) {
          points.add(new Point(x+i,y+j,-3));
          unknown++;
        } else if (cover[x+i][y+j]==-2) {
          points.add(new Point(x+i,y+j,-2));
          flagged++;
        }
      }
    }
    System.out.println("Flagged: "+flagged);
    System.out.println("Unknown: "+unknown);
    if(flagged==value || unknown==value&&flagged==0 || flagged+unknown==value) {
      System.out.println("Hit!");
      for(int i = 0; i < points.size(); i++) {
        Point point = points.get(i);
        System.out.println("X:"+point.getX());
        System.out.println("Y:"+point.getY());
        System.out.println("Value:"+point.getValue());
        if(point.getValue()==-3) {
          System.out.println(board[point.getX()][point.getY()]);
          cover[point.getX()][point.getY()] = board[point.getX()][point.getY()];
        }
      }
    } else {
      System.out.println("nope");
    }
    return cover;
  }

  // picks a random spot on the board to start, calls uncoverBoard to uncover location
  public int[][] pickRandom(int[][] board, int[][] cover) {
    int randX = (int)(Math.random()*board.length);
    int randY = (int)(Math.random()*board[0].length);
    for(int i = randX; i<board.length; i++){
      for(int j = randY; j<board[0].length; j++){
        if(board[i][j]==0){
          cover=uncoverBoard(i,j,board,cover);
          return cover;
        }
      }
    }
    for(int i = randX; i>=0; i--){
      for(int j  = randY; j>=0; j--){
        if(board[i][j]==0){
          cover=uncoverBoard(i,j,board,cover);
          return cover;
        }
      }
    }
    return cover;
    /**
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
    return cover;**/
  }

  // uncovers the board at the random spot (r,c)
  // works, but very inefficient
  private int[][] uncoverBoard(int r,int c, int[][] board, int[][] cover) {
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
  public int[][] fillCover(int[][] board) {
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
}
