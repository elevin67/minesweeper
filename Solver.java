package minesweeper;

import java.util.*;
import java.util.ArrayList;

public class Solver {
  Board board1;
  int[][] gameBoard;
  // the board that is visible to the user
  int[][] cover;
  // tracks if a function changes a board
  boolean changes = false;
  // tracks if there were any changes on the cover that iteration
  boolean iterationChange = false;
  // tracks if a random selector was unable to find any changes
  boolean failed = false;

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

  // solves the board using a completely random way of guessing mines when stuck
  public boolean solveRandomBoard(int[][] board, int[][] cover) {
    cover = pickRandom(board,cover);
    if(cover==null) {
      return false;
    }
    cover = iterateBoard(board,cover);
    while(cover!=null) {
      // if iterateBoard doesn't make any changes
      if(!changes) {
        cover = iterateRandomBoard(board,cover);
      }
      // check the board for logical moves
      else {
        cover = iterateBoard(board,cover);
      }
      if(cover==null) {
        continue;
      }


      if(checkFinish(cover, board)) {
        return true;
      }
    }
    return false;
  }

  // solves the board using an optimized random approach that searches for
  // guesses with a higher percentage of success
  public boolean solveOptimizedBoard(int[][] board, int[][] cover) {
    cover = pickRandom(board,cover);
    if(cover==null) {
      return false;
    }
    cover = iterateBoard(board,cover);
    // if iterateBoard doesn't make any changes
    while(cover!=null) {
      if(!changes) {
        cover = iterateOptimizedBoard(board,cover);
      }
      // check the board for logical moves
      else {
        cover = iterateBoard(board,cover);
      }
      if(cover==null) {
        continue;
      }


      if(checkFinish(cover, board)) {
        return true;
      }

    }
    return false;
  }

  // iterates over the board, picking out spots where there are obviously mines
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
    return cover;
  }

  // iterates over the board to search where to make it's guess when stuck
  private int[][] iterateRandomBoard(int[][] board, int[][] cover) {
    iterationChange = false;
    failed = false;
    for(int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (cover[i][j] > 0) {
          cover = iterateRandomTile(i, j, board, cover);
          if (iterationChange) {
            changes = true;
            return cover;
          } else {
            changes = false;
          }
        } else if(cover[i][j] == -1) {
          return null;
        }
      }
    }
    // was unable to find any tiles to uncover
    failed = true;
    return cover;
  }

  // iterates over the board, looking for the highest percentage possible guess when stuck
  private int[][] iterateOptimizedBoard(int[][] board, int[][] cover) {
    iterationChange = false;
    failed = false;
    for(int k = 1; k <= 8; k++) {
      for(int i = 0; i < board.length; i++) {
        for(int j = 0; j < board[i].length; j++) {
          if(cover[i][j] > 0) {
            cover = iterateOptimizedTile(i,j,board,cover,k);
            if (iterationChange) {
              changes = true;
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
    // was unable to find any tiles to uncover
    failed = true;
    return cover;
  }



  // iterates over a single tile
  // if it needs to flag surrounding tiles, will do so
  // if it needs to uncover surrounding tiles, will do so
  public int[][] iterateTile(int r, int c, int[][] board, int[][] cover) {
    ArrayList<Point> points = getAdjacent(r,c,cover);
    ArrayList<Point> unknownPoints = getUnknown(points);
    ArrayList<Point> flaggedPoints = getFlagged(points);
    int unknown = unknownPoints.size();
    int flagged = flaggedPoints.size();
    int value = board[r][c];

    // if you need to flag all the unknown
    if((unknown==value&&flagged==0) || (flagged+unknown==value&&unknown!=0)) {
      iterationChange = true;
      for(int i = 0; i < unknown; i++) {
        Point unknownPoint = unknownPoints.get(i);
        cover[unknownPoint.getX()][unknownPoint.getY()] = -2;
      }
    }
    // if you need to uncover all the unknown
    else if(flagged==value&&unknown>0) {
      iterationChange = true;
      for(int i = 0; i < unknown; i++) {
        Point unknownPoint = unknownPoints.get(i);
        if(board[unknownPoint.getX()][unknownPoint.getY()]==0) {
          cover = uncoverBoardBFS(unknownPoint.getX(),unknownPoint.getY(),board,cover);
        } else {
          cover[unknownPoint.getX()][unknownPoint.getY()] = board[unknownPoint.getX()][unknownPoint.getY()];
        }
      }
    }
    return cover;
  }

  // will pick a random unknown around a tile to uncover
  public int[][] iterateRandomTile(int r, int c, int[][] board, int[][] cover) {
    ArrayList<Point> points = getAdjacent(r,c,cover);
    ArrayList<Point> unknownPoints = getUnknown(points);

    if(unknownPoints.size()!=0) {
      iterationChange = true;
      int randNum = (int)(Math.random()*unknownPoints.size());
      Point randomUnknown = unknownPoints.get(randNum);
      if(board[randomUnknown.getX()][randomUnknown.getY()]!=0) {
        cover[randomUnknown.getX()][randomUnknown.getY()] = board[randomUnknown.getX()][randomUnknown.getY()];
      } else {
        cover = uncoverBoardBFS(randomUnknown.getX(),randomUnknown.getY(),board,cover);
      }
    }
    return cover;
  }

  // if the number of unknown points is the right number specified by the value k,
  // will uncover random unknown around tile
  public int[][] iterateOptimizedTile(int r, int c, int[][] board, int[][] cover, int k) {
    ArrayList<Point> points = getAdjacent(r,c,cover);
    ArrayList<Point> unknownPoints = getUnknown(points);
    ArrayList<Point> flaggedPoints = getFlagged(points);
    int flagged = flaggedPoints.size();
    int value = board[r][c];

    if(unknownPoints.size()==k+(value-flagged)) {
      iterationChange = true;
      int randNum = (int)(Math.random()*unknownPoints.size());
      Point randomUnknown = unknownPoints.get(randNum);

      if(board[randomUnknown.getX()][randomUnknown.getY()]!=0) {
        cover[randomUnknown.getX()][randomUnknown.getY()] = board[randomUnknown.getX()][randomUnknown.getY()];
      } else {
        cover = uncoverBoardBFS(randomUnknown.getX(),randomUnknown.getY(),board,cover);
      }
    }
    return cover;
  }

  // Shilad says:
  // * Write this as a while loop
  // * Pick a location
  // * If the location is covered, do a BFS from that location to uncover nearby cells and stop
  // * Otherwise, keep looping

  // picks a random spot empty spot in the board. Selected spot is the first move.
  // first point must be a 0, so it can uncover a larger section of the board
  // picks a random point, then finds nearest 0
  public int[][] pickRandom(int[][] board, int[][] cover) {
    int randX = (int)(Math.random()*board.length);
    int randY = (int)(Math.random()*board[0].length);
    // searches one direction for a 0
    for(int i = randX; i<board.length; i++){
      for(int j = randY; j<board[0].length; j++){
        if(board[i][j]==0){
          cover = uncoverBoardBFS(i,j,board,cover);
          return cover;
        }
      }
    }
    // if it doesn't find a 0, searches the other direction
    for(int i = randX; i>=0; i--){
      for(int j  = randY; j>=0; j--){
        if(board[i][j]==0){
          cover = uncoverBoardBFS(i,j,board,cover);
          return cover;
        }
      }
    }
    cover[randX][randY] = board[randX][randY];
    return cover;
  }

  // Brute force solution, no longer in use, left in for old times sake
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
            uncoveredBoard = uncoverBoardBFS(x+i,y+j,board, uncoveredBoard);
          }
        } else if(board[x+i][y+j] != -1) {
          uncoveredBoard[x+i][y+j] = board[x+i][y+j];
        }
      }
    }
    return uncoveredBoard;
  }

  // BFS implementation of function that uncovers all necessary mines around a 0 tile
  private int[][] uncoverBoardBFS(int r, int c, int[][] board, int[][] cover) {
    Queue<Point> points = new LinkedList<>();

    ArrayList<Point> visited = new ArrayList<>();

    Point firstPoint = new Point(r,c,cover[r][c]);
    points.add(firstPoint);
    visited.add(firstPoint);
    cover[r][c] = board[r][c];

    while(!points.isEmpty()) {
      Point point = points.remove();
      ArrayList<Point> adjacent = getAdjacent(point.getX(),point.getY(),cover);
      for(int i = 0; i < adjacent.size(); i++) {
        Point adjacentPoint = adjacent.get(i);
        if(checkVisited(adjacentPoint,visited)) {
          continue;
        } else if(board[adjacentPoint.getX()][adjacentPoint.getY()] > 0) {
          cover[adjacentPoint.getX()][adjacentPoint.getY()] = board[adjacentPoint.getX()][adjacentPoint.getY()];
        } else if(board[adjacentPoint.getX()][adjacentPoint.getY()] == 0) {
          points.add(adjacentPoint);
          visited.add(adjacentPoint);
          cover[adjacentPoint.getX()][adjacentPoint.getY()] = board[adjacentPoint.getX()][adjacentPoint.getY()];
        }
      }
    }

    return cover;
  }

  // returns ArrayList of all adjacent points to a tile
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

  // returns all flagged points within an ArrayList
  private ArrayList<Point> getFlagged(ArrayList<Point> points) {
    ArrayList<Point> flagged = new ArrayList<>();
    for(int i = 0; i < points.size(); i++) {
      if(points.get(i).getValue()==-2) {
        flagged.add(points.get(i));
      }
    }

    return flagged;
  }

  // returns all unknown points within an ArrayList
  private ArrayList<Point> getUnknown(ArrayList<Point> points) {
    ArrayList<Point> unknown = new ArrayList<>();
    for(int i = 0; i < points.size(); i++) {
      if(points.get(i).getValue()==-3) {
        unknown.add(points.get(i));
      }
    }

    return unknown;
  }

  // checks if the board has been finished
  private boolean checkFinish(int[][] cover, int[][] board) {
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
    // if a random iteration was unable to find any tiles to uncover
    else if(failed) {
      if(checkForCoveredMine(cover)) {
        return true;
      }
      else if(checkForWall(cover)) {
        pickRandom(board, cover);
      }
    }
    return false;
  }

  // checks if there is a mine completely covered by other mines
  private boolean checkForCoveredMine(int[][] cover) {
    for(int i = 0; i < cover.length; i++) {
      for(int j = 0; j < cover[i].length; j++) {
        if(cover[i][j]==-3) {
          ArrayList<Point> adjacentPoints = getAdjacent(i,j,cover);
          if(getFlagged(adjacentPoints).size()+getUnknown(adjacentPoints).size()==adjacentPoints.size()) {
            cover[i][j] = -2;
            return true;
          }
        }
      }
    }
    return false;
  }

  // checks if all the unknown tiles are walled off by all mines
  // if that is the case unable to reach unknown tiles
  private boolean checkForWall(int[][] cover) {
    for(int i = 0; i < cover.length; i++) {
      for(int j = 0; j < cover[i].length; j++) {
        if(cover[i][j] > 0) {
          ArrayList<Point> adjacentPoints = getAdjacent(i,j,cover);
          if(getUnknown(adjacentPoints).size() > 0) {
            return false;
          }
        }
      }
    }

    return true;
  }

  // part of uncoverBoardBFS, checks if a point's x and y coordinate are in the ArrayList
  private boolean checkVisited(Point point, ArrayList<Point> points) {
    for(int i = 0; i < points.size(); i++) {
      Point j = points.get(i);
      if(point.getX()==j.getX()&&point.getY()==j.getY()) {
        return true;
      }
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