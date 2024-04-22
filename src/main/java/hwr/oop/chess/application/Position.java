package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.Figure;

import java.util.ArrayList;
import java.util.List;

public class Position {
  private Figure figure;
  private Position topPosition;
  private Position bottomPosition;
  private Position leftPosition;
  private Position rightPosition;
  private Position topLeftPosition;
  private Position topRightPosition;
  private Position bottomLeftPosition;
  private Position bottomRightPosition;
  private int y;
  private int x;

  public Position(int x, int y) {
    if (x < 1 || x > 8 || y < 1 || y > 8) {
      throw new IllegalArgumentException("Invalid Position");
    }

    this.x = x;
    this.y = y;
    this.figure = null;
    this.topPosition = null;
    this.bottomPosition = null;
    this.leftPosition = null;
    this.rightPosition = null;
    this.topLeftPosition = null;
    this.topRightPosition = null;
    this.bottomLeftPosition = null;
    this.bottomRightPosition = null;
  }

  public Position(char x, int y) {
    this(x - 96, y);
  }


  // Method to set the figure
  public void setFigure(Figure figure) {
    this.figure = figure;
  }

  // Method to get the figure
  public Figure getFigure() {
    return figure;
  }

  // Methods to set adjacent positions
  public void setTopPosition(Position position) {
    this.topPosition = position;
  }

  public void setBottomPosition(Position position) {
    this.bottomPosition = position;
  }

  public void setLeftPosition(Position position) {
    this.leftPosition = position;
  }

  public void setRightPosition(Position position) {
    this.rightPosition = position;
  }

  public void setTopLeftPosition(Position position) {
    this.topLeftPosition = position;
  }

  public void setTopRightPosition(Position position) {
    this.topRightPosition = position;
  }

  public void setBottomLeftPosition(Position position) {
    this.bottomLeftPosition = position;
  }

  public void setBottomRightPosition(Position position) {
    this.bottomRightPosition = position;
  }

  // Methods to get adjacent positions
  public Position getTopPosition() {
    return topPosition;
  }

  public Position getBottomPosition() {
    return bottomPosition;
  }

  public Position getLeftPosition() {
    return leftPosition;
  }

  public Position getRightPosition() {
    return rightPosition;
  }

  public Position getTopLeftPosition() {
    return topLeftPosition;
  }

  public Position getTopRightPosition() {
    return topRightPosition;
  }

  public Position getBottomLeftPosition() {
    return bottomLeftPosition;
  }

  public Position getBottomRightPosition() {
    return bottomRightPosition;
  }

  // Method to return the index of the column to which the position belongs
  public int x() {
    return x;
  }

  // Method to return the index of the row to which the position belongs
  public int y() {
    return y;
  }

  // Method to return all positions in the row to which this position belongs
  public List<Position> getRowPositions() {
    List<Position> rowPositions = new ArrayList<>();
    Position currentPosition = this;
    // Add all positions belonging to the row by moving right from the current position
    while (currentPosition != null) {
      rowPositions.add(currentPosition);
      currentPosition = currentPosition.getRightPosition();
    }
    return rowPositions;
  }

  // Method to return all positions in the column to which this position belongs
  public List<Position> getColPositions() {
    List<Position> colPositions = new ArrayList<>();
    Position currentPosition = this;
    // Add all positions belonging to the column by moving down from the current position
    while (currentPosition != null) {
      colPositions.add(currentPosition);
      currentPosition = currentPosition.getBottomPosition();
    }
    return colPositions;
  }

  public boolean isEqualTo(Position pos1) {
    Position pos2 = this;
    return (pos1.x() == pos2.x()) && (pos1.y() == pos2.y());
  }
}

