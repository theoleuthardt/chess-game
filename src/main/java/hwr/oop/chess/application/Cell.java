package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.Figure;
import hwr.oop.chess.cli.InvalidUserInputException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Cell {
  private final int y;
  private final int x;
  private Figure figure;
  private Cell topCell;
  private Cell bottomCell;
  private Cell leftCell;
  private Cell rightCell;
  private Cell topLeftCell;
  private Cell topRightCell;
  private Cell bottomLeftCell;
  private Cell bottomRightCell;

  public Cell(int x, int y) {
    if (!isValidCoordinate(x, y)) {
      throw new InvalidUserInputException("Invalid Position");
    }
    this.x = x;
    this.y = y;
  }

  public Cell(char x, int y) {
    this(x - 96, y);
  }

  public boolean isValidCoordinate(int c) {
    return c >= 1 && c <= 8;
  }

  public boolean isInvalidCoordinate(int c) {
    return !isValidCoordinate(c);
  }

  public boolean isValidCoordinate(int x, int y) {
    return isValidCoordinate(x) && isValidCoordinate(y);
  }

  public boolean isInvalidCoordinate(int x, int y) {
    return !isValidCoordinate(x, y);
  }

  // Method to set the figure
  public void setFigure(Figure figure) {
    this.figure = figure;
  }

  // Method to get the figure
  public Figure figure() {
    return figure;
  }

  public boolean isFree() {
    return figure == null;
  }

  public boolean isOccupied() {
    return figure != null;
  }

  // Methods to set adjacent positions
  public void setTopCell(Cell position) {
    this.topCell = position;
  }

  public void setBottomCell(Cell position) {
    this.bottomCell = position;
  }

  public void setLeftCell(Cell position) {
    this.leftCell = position;
  }

  public void setRightCell(Cell position) {
    this.rightCell = position;
  }

  public void setTopLeftCell(Cell position) {
    this.topLeftCell = position;
  }

  public void setTopRightCell(Cell position) {
    this.topRightCell = position;
  }

  public void setBottomLeftCell(Cell position) {
    this.bottomLeftCell = position;
  }

  public void setBottomRightCell(Cell position) {
    this.bottomRightCell = position;
  }

  // Methods to get adjacent positions
  public Cell topCell() {
    return topCell;
  }

  public Cell bottomCell() {
    return bottomCell;
  }

  public Cell leftCell() {
    return leftCell;
  }

  public Cell rightCell() {
    return rightCell;
  }

  public Cell topLeftCell() {
    return topLeftCell;
  }

  public Cell topRightCell() {
    return topRightCell;
  }

  public Cell bottomLeftCell() {
    return bottomLeftCell;
  }

  public Cell bottomRightCell() {
    return bottomRightCell;
  }

  // Method to return the index of the column to which the position belongs
  public int x() {
    return x;
  }

  // Method to return the index of the row to which the position belongs
  public int y() {
    return y;
  }

  public Cell cellInDirection(CellDirection direction) {
    return switch (direction) {
      case LEFT -> leftCell();
      case RIGHT -> rightCell();
      case TOP -> topCell();
      case TOP_LEFT -> topLeftCell();
      case TOP_RIGHT -> topRightCell();
      case BOTTOM -> bottomCell();
      case BOTTOM_LEFT -> bottomLeftCell();
      case BOTTOM_RIGHT -> bottomRightCell();
    };
  }

  public void addAvailableCellsInDirectionToList(List<Cell> list, CellDirection direction) {
    Cell current = this;
    while ((current = current.cellInDirection(direction)) != null) {
      boolean cellIsEmpty = current.figure() == null;
      boolean enemyIsOnField = false;
      if (!cellIsEmpty) {
        enemyIsOnField = current.figure().color() != figure().color();
      }

      if (cellIsEmpty || enemyIsOnField) {
        list.add(current);
      }
      if (!cellIsEmpty) {
        break;
      }
    }
  }

  public boolean isEqualTo(Cell pos1) {
    Cell pos2 = this;
    return (pos1.x() == pos2.x()) && (pos1.y() == pos2.y());
  }

  public void connectTo(Cell anotherCell) {
    if (anotherCell == null) {
      return;
    }
    // -1 if anotherCell is to the left
    // 1 if anotherCell is to the right
    int diffX = anotherCell.x() - x;

    // -1 if anotherCell is below
    // 1 if anotherCell is above
    int diffY = anotherCell.y() - y;

    final String notNeighboursError =
        "The cells " + this + " and " + anotherCell + " are not neighbours to each other";

    switch (diffX) {
      case 0 -> {
        // anotherCell is above or below currentCell
        switch (diffY) {
          case 1 -> setTopCell(anotherCell);
          case 0 -> throw new IllegalArgumentException("The cells are identical");
          case -1 -> setBottomCell(anotherCell);
          default -> throw new IllegalArgumentException(notNeighboursError);
        }
      }
      case 1 -> {
        // anotherCell is right of currentCell
        switch (diffY) {
          case 1 -> setTopRightCell(anotherCell);
          case 0 -> setRightCell(anotherCell);
          case -1 -> setBottomRightCell(anotherCell);
          default -> throw new IllegalArgumentException(notNeighboursError);
        }
      }
      case -1 -> {
        // anotherCell is left of currentCell
        switch (diffY) {
          case 1 -> setTopLeftCell(anotherCell);
          case 0 -> setLeftCell(anotherCell);
          case -1 -> setBottomLeftCell(anotherCell);
          default -> throw new IllegalArgumentException(notNeighboursError);
        }
      }
      default -> throw new IllegalArgumentException(notNeighboursError);
    }
  }

  public String toString() {
    return "Cell_" + toCoordinates() + "(" + (figure == null ? "-" : figure.symbol()) + ")";
  }

  public String toCoordinates() {
    return (char) (x + 64) + String.valueOf(y);
  }

  public static boolean isEmptyBetweenCells(Cell currentCell, CellDirection direction, int move) {
    List<Cell> cells = new ArrayList<>();
   while (move > 0) {
     Cell targetCell = currentCell.cellInDirection(direction);
     cells.add(targetCell);
     currentCell = targetCell;
     move--;
   }

    return cells.stream().noneMatch(Cell::isOccupied);
  }

  public static Cell findNextCell(Cell currentCell, CellDirection direction, int move) {
    Cell nextCell = currentCell;
    while (move > 0) {
      nextCell = nextCell.cellInDirection(direction);
      move--;
    }
    return nextCell;
  }
}
