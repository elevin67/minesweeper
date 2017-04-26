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
  public boolean solveRandomBoard(int[][] board, int[][] cover) {
    cover = pickRandom(board,cover);
//    board1.printBoard(board);
//    board1.printBoard(cover);
    if(cover==null) {
      return false;
    }
    cover = iterateBoard(board,cover);
    while(cover!=null) {
      if(!changes) {
        cover = iterateRandomBoard(board,cover);
      } else {
        cover = iterateBoard(board,cover);
      }
      if(cover==null) {
        continue;
      }


      if(checkFinish(cover)) {
        return true;
      }
    }
    return false;
  }

  public boolean solveOptimizedBoard(int[][] board, int[][] cover) {
    cover = pickRandom(board,cover);
//    board1.printBoard(cover);
    if(cover==null) {
      return false;
    }
    cover = iterateBoard(board,cover);
    while(cover!=null) {
      if(!changes) {
        cover = iterateOptimizedBoard(board,cover);
      } else {
        cover = iterateBoard(board,cover);
      }
      if(cover==null) {
        continue;
      }


      if(checkFinish(cover)) {
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
//            System.out.println("iterateRandomBoard");
//            board1.printBoard(cover);
            return cover;
          } else {
            changes = false;
          }
        } else if(cover[i][j] == -1) {
          return null;
        }
      }
    }
    return cover;
  }

  private int[][] iterateOptimizedBoard(int[][] board, int[][] cover) {
    iterationChange = false;
    for(int k = 1; k <= 8; k++) {
      for(int i = 0; i < board.length; i++) {
        for(int j = 0; j < board[i].length; j++) {
          if(cover[i][j] > 0) {
            cover = iterateOptimizedTile(i,j,board,cover,k);
            if (iterationChange) {
              changes = true;
//              System.out.println("iterateOptimizedBoard");
//              board1.printBoard(cover);
              return cover;
            } else {
              changes = false;
            }
          } else if(cover[i][j] == -1) {
            return null;
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
        if(cover[i][j] > 0) {
          cover = iterateTile(i, j, board, cover);
        }
        if(cover[i][j] == -1) {
          return null;
        }
      }
    }
    if(iterationChange) {
      changes = true;
    } else {
      changes = false;
    }
//    System.out.println("iterateBoard");
//    board1.printBoard(cover);
    return cover;
  }

  // just for one tile. Will need a function that runs over all tiles until no obvious moves are left available
  // works!
  public int[][] iterateTile(int r, int c, int[][] board, int[][] cover) {
    ArrayList<Point> points = getAdjacent(r,c,cover);
    ArrayList<Point> unknownPoints = getUnknown(points);
    ArrayList<Point> flaggedPoints = getFlagged(points);
    int unknown = unknownPoints.size();
    int flagged = flaggedPoints.size();
    int value = board[r][c];

    if((unknown==value&&flagged==0) || (flagged+unknown==value&&unknown!=0)) {
      iterationChange = true;
      for(int i = 0; i < unknown; i++) {
        Point unknownPoint = unknownPoints.get(i);
        cover[unknownPoint.getX()][unknownPoint.getY()] = -2;
      }
    } else if(flagged==value&&unknown>0) {
      iterationChange = true;
      for(int i = 0; i < unknown; i++) {
        Point unknownPoint = unknownPoints.get(i);
        if(board[unknownPoint.getX()][unknownPoint.getY()]==0) {
          cover = uncoverBoard(unknownPoint.getX(),unknownPoint.getY(),board,cover);
        } else {
          cover[unknownPoint.getX()][unknownPoint.getY()] = board[unknownPoint.getX()][unknownPoint.getY()];
        }
      }
    }
//    System.out.println("iterateTile");
//    board1.printBoard(cover);
    return cover;
  }

  // works!
  public int[][] iterateRandomTile(int r, int c, int[][] board, int[][] cover) {
    ArrayList<Point> points = getAdjacent(r,c,cover);
    ArrayList<Point> unknownPoints = getUnknown(points);
    ArrayList<Point> flaggedPoints = getFlagged(points);
    int unknown = unknownPoints.size();
    int flagged = flaggedPoints.size();
    int value = board[r][c];

    if(unknownPoints.size()!=0) {
      iterationChange = true;
      int randNum = (int)(Math.random()*unknownPoints.size());
      Point randomUnknown = unknownPoints.get(randNum);
      if(board[randomUnknown.getX()][randomUnknown.getY()]!=0) {
        cover[randomUnknown.getX()][randomUnknown.getY()] = board[randomUnknown.getX()][randomUnknown.getY()];
      } else {
        cover = uncoverBoard(randomUnknown.getX(),randomUnknown.getY(),board,cover);
      }
    }
//    System.out.println("iterateRandomTile");
//    board1.printBoard(cover);
    return cover;
  }

  public int[][] iterateOptimizedTile(int r, int c, int[][] board, int[][] cover, int k) {
    ArrayList<Point> points = getAdjacent(r,c,cover);
    ArrayList<Point> unknownPoints = getUnknown(points);
    ArrayList<Point> flaggedPoints = getFlagged(points);
    int unknown = unknownPoints.size();
    int flagged = flaggedPoints.size();
    int value = board[r][c];

    if(unknownPoints.size()==k+(value-flagged)) {
      iterationChange = true;
      int randNum = (int)(Math.random()*unknownPoints.size());
      Point randomUnknown = unknownPoints.get(randNum);

      if(board[randomUnknown.getX()][randomUnknown.getY()]!=0) {
        cover[randomUnknown.getX()][randomUnknown.getY()] = board[randomUnknown.getX()][randomUnknown.getY()];
      } else {
        cover = uncoverBoard(randomUnknown.getX(),randomUnknown.getY(),board,cover);
      }
    }
//    System.out.println("iterateOptimizedBoard");
//    board1.printBoard(cover);
    return cover;
  }

  // Shilad says:
  // * Write this as a while loop
  // * Pick a location
  // * If the location is covered, do a BFS from that location to uncover nearby cells and stop
  // * Otherwise, keep looping

  // picks a random spot empty spot in the board. Selected spot is the first move.
  // works!
  public int[][] pickRandom(int[][] board, int[][] cover) {
    int randX = (int)(Math.random()*board.length);
    int randY = (int)(Math.random()*board[0].length);
    for(int i = randX; i<board.length; i++){
      for(int j = randY; j<board[0].length; j++){
        if(board[i][j]==0){
          cover = uncoverBoard(i,j,board,cover);
          return cover;
        }
      }
    }
    for(int i = randX; i>=0; i--){
      for(int j  = randY; j>=0; j--){
        if(board[i][j]==0){
          cover = uncoverBoard(i,j,board,cover);
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

  // This should be replaced with a BFS
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
        if(x+i < 0 || x+i >= board.length) {
          continue;
        } else if (y+j < 0 || y+j >= board[i].length) {
          continue;
        } else if(board[x+i][y+j] == 0){
          if(cover[x+i][y+j] != 0) {
            cover[x+i][y+j] = 0;
            uncoveredBoard = uncoverBoard(x+i,y+j,board, uncoveredBoard);
          }
        } else if(board[x+i][y+j] != -1) {
          uncoveredBoard[x+i][y+j] = board[x+i][y+j];
        }
      }
    }
    return uncoveredBoard;
  }

  private int[][] uncoverBoardBFS(int r, int c, int[][] board, int[][] cover) {
    Queue<Point> points = new LinkedList<>();
    points.add(new Point(r,c,cover[r][c]));
    while(!points.isEmpty()) {
      Point point = points.remove();
      ArrayList<Point> adjacent = getAdjacent(point.getX(),point.getY(),cover);
      for(int i = 0; i < adjacent.size(); i++) {
        Point adjacentPoint = adjacent.get(i);
        if(points.contains(adjacentPoint)) {
          continue;
        } else if(board[adjacentPoint.getX()][adjacentPoint.getY()] > 0) {
          cover[adjacentPoint.getX()][adjacentPoint.getY()] = board[adjacentPoint.getX()][adjacentPoint.getY()];
        } else if(board[adjacentPoint.getX()][adjacentPoint.getY()] == 0) {
          points.add(adjacentPoint);
          cover[adjacentPoint.getX()][adjacentPoint.getY()] = board[adjacentPoint.getX()][adjacentPoint.getY()];
        }
      }
    }

    return cover;
  }

  // works
  private ArrayList<Point> getAdjacent(int r, int c, int[][] cover) {
    int x = r - 1;
    int y = c - 1;
    ArrayList<Point> points = new ArrayList<Point>();
    for(int i=0; i<3; i++) {
      for(int j=0; j<3; j++) {
        if(i == 1 && j == 1) {
          continue;
        }
        if(x+i < 0 || x+i >= cover.length) {
          continue;
        } else if (y+j < 0 || y+j >= cover[i].length) {
          continue;
        }
        points.add(new Point(x+i,y+j,cover[x+i][y+j]));
      }
    }
    return points;
  }

  // works
  private ArrayList<Point> getFlagged(ArrayList<Point> points) {
    ArrayList<Point> flagged = new ArrayList<>();
    for(int i = 0; i < points.size(); i++) {
      if(points.get(i).getValue()==-2) {
        flagged.add(points.get(i));
      }
    }

    return flagged;
  }

  // works
  private ArrayList<Point> getUnknown(ArrayList<Point> points) {
    ArrayList<Point> unknown = new ArrayList<>();
    for(int i = 0; i < points.size(); i++) {
      if(points.get(i).getValue()==-3) {
        unknown.add(points.get(i));
      }
    }

    return unknown;
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