package minesweeper;

import java.util.*;
import java.util.ArrayList;

public class Solver {
  Board board1;
  int[][] gameBoard;
  int[][] cover;
  boolean changes = false;
  boolean iterationChange = false;
  // boolean finished = false;

  public Solver(Board b) {
    board1 = b;
    gameBoard = board1.getBoard();
    cover = fillCover(new int[gameBoard.length][gameBoard[0].length]);

  }

  public int[][] getGameBoard() {
    return gameBoard;
  }

  public int[][] getCover() {
    return cover;
  }

  // puts flags (-2) on all the mines
  public boolean solveBoard(int[][] board, int[][] cover) {
//    board1.printBoard(board);
    cover = pickRandom(board,cover);
    if(cover==null) {
      return false;
    }
    board1.printBoard(board);
    board1.printBoard(cover);
    cover = iterateBoard(board,cover);
    while(cover!=null) {
      if(!changes) {
//        int randX = (int)(Math.random()*board.length);
//        int randY = (int)(Math.random()*board[0].length);
        cover = iterateRandomBoard(board,cover);
      } else {
        cover = iterateBoard(board,cover);
      }
      if(cover==null) {
        continue;
      }

      if(checkFinish(cover)==true) {
        return true;
      }
    }
    return false;
  }

  private int[][] iterateRandomBoard(int[][] board, int[][] cover) {
    iterationChange = false;
    for(int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (cover[i][j] > 0) {
          cover = iterateRandomTile(i, j, board, cover);
          if (iterationChange) {
            changes = true;
//            board1.printBoard(cover);
            return cover;
          } else {
            changes = false;
          }
        }
      }
    }
    return cover;
  }

  // iterates over the board, picking out mines
  // works!
  private int[][] iterateBoard(int[][] board, int[][] cover) {
    iterationChange = false;
    for(int i = 0; i < board.length; i++) {
      for(int j = 0; j < board[i].length; j++) {
        if(cover[i][j]>0) {
          cover = iterateTile(i, j, board, cover);
        }
        else if(cover[i][j]==-1) {
          return null;
        }
      }
    }
    if(iterationChange) {
      changes = true;
    } else {
      changes = false;
    }
//    board1.printBoard(cover);
    return cover;
  }
  public int[][] oneToOnePattern(int r, int c, int[][] board, int[][] cover){
    return cover;
  }
  public int[][] oneToTwoPattern(int r, int c, int[][] board, int[][] cover){
    return cover;
  }
  public int[][] oneToTwoToOnePattern(int r, int c, int[][] board, int[][] cover){
    return cover;
  }
  public int[][] oneToTwoToTwoToOnePattern(int r, int c, int[][] board, int[][] cover){
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
          unknown++;
        } else if (cover[x+i][y+j]==-2) {
          flagged++;
        }
        points.add(new Point(x+i,y+j,cover[x+i][y+j]));
      }
    }
    if((unknown==value&&flagged==0) || (flagged+unknown==value&&unknown!=0) || (flagged==value&&unknown>0)) {
      iterationChange = true;
      for(int i = 0; i < points.size(); i++) {
        Point point = points.get(i);
        if(unknown==value&&flagged==0 || (flagged+unknown==value&&unknown!=0)) {
          if(point.getValue()==-3) {
            cover[point.getX()][point.getY()] = -2;
          }
        } else if(flagged==value&&unknown>0) {
          if(point.getValue()==-3) {
            if(board[point.getX()][point.getY()]==0) {
              cover = uncoverBoard(point.getX(),point.getY(),board,cover);
//              cover[point.getX()][point.getY()] = board[point.getX()][point.getY()];
            } else {
              cover[point.getX()][point.getY()] = board[point.getX()][point.getY()];
            }
          }
        }
      }
    }
//    board1.printBoard(cover);
    return cover;
  }

  // works!
  public int[][] iterateRandomTile(int r, int c, int[][] board, int[][] cover) {
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
          unknown++;
        } else if (cover[x+i][y+j]==-2) {
          flagged++;
        }
        points.add(new Point(x+i,y+j,cover[x+i][y+j]));
      }
    }
    ArrayList<Point> unknownPoints = new ArrayList<Point>();
    for(int i=0; i<points.size(); i++) {
      Point point = points.get(i);
      if(point.getValue()==-3) {
        unknownPoints.add(new Point(point.getX(),point.getY(),-3));
      }
    }
    if(unknownPoints.size()!=0) {
      iterationChange = true;
      int randNum = (int)(Math.random()*unknownPoints.size());
      Point randomUnknown = unknownPoints.get(randNum);
      // find value on board, to uncover or nah
      if(board[randomUnknown.getX()][randomUnknown.getY()]!=0) {
        cover[randomUnknown.getX()][randomUnknown.getY()] = board[randomUnknown.getX()][randomUnknown.getY()];
      } else {
        cover = uncoverBoard(randomUnknown.getX(),randomUnknown.getY(),board,cover);
      }
    }
//    board1.printBoard(cover);
    return cover;
  }

  // picks a random spot empty spot in the board. Selected spot is the first move.
  // works!
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
    cover[randX][randY] = board[randX][randY];
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
    cover[r][c] = board[r][c];
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

  // works!
  public boolean checkFinish(int[][] cover) {
    int numMines = board1.getNumMines();
    int count = 0;
    for(int i = 0; i < cover.length; i++) {
      for(int j = 0; j < cover[i].length; j++) {
        if(cover[i][j]==-2) {
          count++;
        }
      }
    }
    if(count==numMines) {
      return true;
    }
    return false;
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