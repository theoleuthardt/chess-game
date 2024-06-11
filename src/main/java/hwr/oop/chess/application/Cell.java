package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.Figure;
import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.application.figures.FigureType;

import java.util.ArrayList;
import java.util.List;

public class Cell {
  private final Coordinate y;
  private final Coordinate x;
  private Figure figure;
  private Cell topCell;
  private Cell bottomCell;
  private Cell leftCell;
  private Cell rightCell;
  private Cell topLeftCell;
  private Cell topRightCell;
  private Cell bottomLeftCell;
  private Cell bottomRightCell;
  private boolean isEnPassant = false;

  public Cell(Coordinate x, Coordinate y) {
    this.x = x;
    this.y = y;
  }

  // Method to set the figure
  public void setFigure(Figure figure) {
    this.figure = figure;
  }

  // Method to get the figure
  public Figure figure() {
    return figure;
  }

  public boolean isOccupiedBy(FigureColor color) {
    return isOccupied() && figure.color() == color;
  }

  public boolean isOccupiedBy(FigureType type) {
    return isOccupied() && figure.type() == type;
  }

  public boolean isOccupiedBy(FigureColor color, FigureType type) {
    return isOccupiedBy(color) && isOccupiedBy(type);
  }

  public boolean isOccupiedByOpponentOf(FigureColor color) {
    return isOccupied() && figure.color() != color;
  }

  public boolean isOccupiedByOpponentOf(Figure figure) {
    return isOccupiedByOpponentOf(figure.color());
  }

  public boolean isFree() {
    return figure == null;
  }

  public boolean isOccupied() {
    return figure != null;
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

  public boolean hasTopCell() {
    return topCell != null;
  }

  public boolean hasBottomCell() {
    return bottomCell != null;
  }

  public boolean hasLeftCell() {
    return leftCell != null;
  }

  public boolean hasRightCell() {
    return rightCell != null;
  }

  public boolean hasTopLeftCell() {
    return topLeftCell != null;
  }

  public boolean hasTopRightCell() {
    return topRightCell != null;
  }

  public boolean hasBottomLeftCell() {
    return bottomLeftCell != null;
  }

  public boolean hasBottomRightCell() {
    return bottomRightCell != null;
  }

  // Method to return the index of the column to which the position belongs
  public Coordinate x() {
    return x;
  }

  // Method to return the index of the row to which the position belongs
  public Coordinate y() {
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

  public boolean hasCellInDirection(CellDirection direction) {
    return cellInDirection(direction) != null;
  }

  public void addAvailableCellsInDirectionToList(List<Cell> list, CellDirection direction) {
    for (Cell cell : allCellsInDirection(direction)) {
      if (cell.isFree() || cell.isOccupiedByOpponentOf(figure)) {
        list.add(cell);
      }
      if (cell.isOccupied()) {
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
    int diffX = anotherCell.x().toInt() - x.toInt();

    // -1 if anotherCell is below
    // 1 if anotherCell is above
    int diffY = anotherCell.y().toInt() - y.toInt();

    final String notNeighboursError =
        "The cells " + this + " and " + anotherCell + " are not neighbours to each other";

    switch (diffX) {
      case 0 -> {
        // anotherCell is above or below currentCell
        switch (diffY) {
          case 1 -> topCell = anotherCell;
          case 0 -> throw new IllegalArgumentException("The cells are identical");
          case -1 -> bottomCell = anotherCell;
          default -> throw new IllegalArgumentException(notNeighboursError);
        }
      }
      case 1 -> {
        // anotherCell is right of currentCell
        switch (diffY) {
          case 1 -> topRightCell = anotherCell;
          case 0 -> rightCell = anotherCell;
          case -1 -> bottomRightCell = anotherCell;
          default -> throw new IllegalArgumentException(notNeighboursError);
        }
      }
      case -1 -> {
        // anotherCell is left of currentCell
        switch (diffY) {
          case 1 -> topLeftCell = anotherCell;
          case 0 -> leftCell = anotherCell;
          case -1 -> bottomLeftCell = anotherCell;
          default -> throw new IllegalArgumentException(notNeighboursError);
        }
      }
      default -> throw new IllegalArgumentException(notNeighboursError);
    }
  }

  @Override
  public String toString() {
    return "Cell_" + toCoordinates() + "(" + (isFree() ? "-" : figure.symbol()) + ")";
  }

  public String toCoordinates() {
    return (char) (x.toInt() + 'A' - 1) + String.valueOf(y.toInt());
  }

  public List<Cell> allCellsInDirection(CellDirection direction) {
    List<Cell> cells = new ArrayList<>();
    Cell cell = this;
    while (cell.hasCellInDirection(direction)) {
      cell = cell.cellInDirection(direction);
      cells.add(cell);
    }
    return cells;
  }

  public Cell findCellInDirection(int count, CellDirection direction) {
    for (Cell cell : allCellsInDirection(direction)) {
      if (--count == 0) {
        return cell;
      }
    }
    throw new IllegalArgumentException(
        "The cell is not reachable in direction " + direction.name());
  }

  public boolean isFreeInDirection(int count, CellDirection direction) {
    for (Cell cell : allCellsInDirection(direction)) {
      if (cell.isOccupied()) {
        return false;
      }
      if (--count == 0) {
        return true;
      }
    }
    return false;
  }

  public boolean isEnPassant() {
    return isEnPassant;
  }

  public void setIsEnPassant(boolean enPassant) {
    isEnPassant = enPassant;
  }

  public boolean isCellBackgroundColorWhite() {
    return (x.toInt() + y.toInt()) % 2 == 1;
  }
}
