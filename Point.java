package minesweeper;

import java.util.*;


// this class is used to keep track of a points coordinates and value
public class Point {
  int x;
  int y;
  int value;

  public Point(int x, int y, int value) {
    this.x = x;
    this.y = y;
    this.value = value;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public int getValue() {
    return value;
  }
}
