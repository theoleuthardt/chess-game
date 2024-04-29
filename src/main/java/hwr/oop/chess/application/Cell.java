package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.Figure;
import hwr.oop.chess.application.figures.FigureColor;

import java.util.ArrayList;

import static hwr.oop.chess.application.Board.isValidCoordinate;

public class Cell {
  private Figure figure;
  private Cell topCell;
  private Cell bottomCell;
  private Cell leftCell;
  private Cell rightCell;
  private Cell topLeftCell;
  private Cell topRightCell;
  private Cell bottomLeftCell;
  private Cell bottomRightCell;

  private final int y;
  private final int x;

  public Cell(int x, int y) {
    if(!isValidCoordinate(x, y)) {
        throw new IllegalArgumentException("Invalid Position");
    }
    this.x = x;
    this.y = y;
  }

  public Cell(char x, int y) {
    this(x - 96, y);
  } // TODO add isValidCoordinate

  // Method to set the figure
  public void setFigure(Figure figure) {
    this.figure = figure;
  }

  // Method to get the figure
  public Figure getFigure() {
    return figure;
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

  // Method to return all positions in the row to which this position belongs
//  public List<Cell> getCellsInRow() {
//    ArrayList<Cell> cells = this.allCells();
//    cells.removeIf(cell -> cell.y() != this.y());
//    return cells;
//  }

  // Method to return all positions in the column to which this position belongs
//  public List<Cell> getCellsInColumn() {
//    ArrayList<Cell> cells = Board.allCells();
//    cells.removeIf(cell -> cell.x() != this.x());
//    return cells;
//  }

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

  public void addAvailableCellsInDirectionToList(ArrayList<Cell> list, CellDirection direction) {
   // TODO fix test error testMoveQueen line 54
    Cell current = this;
    while ((current = current.cellInDirection(direction)) != null) {
      if (current.getFigure() == null) {
        list.add(current);
        continue;
      }
      if (current.getFigure().color() != getFigure().color()) {
        list.add(current);
        break;
      } else {
        break;
      }
    }
  }

  public boolean isEqualTo(int x1, int y1, int x2, int y2) {
    return (x1 == x2) && (y1 == y2);
  }

  public static boolean canCaptureKing(Cell current, FigureColor myColor) {
    ArrayList<Cell> availableCells = current.getFigure().getAvailableCells(current);
    for (Cell cell : availableCells) {
      if(isOpponentKing(cell, myColor )){
        return true;
      }
    }
    return false;
  }

  public static boolean isOpponentKing(Cell cell, FigureColor myColor){
    if(myColor == FigureColor.WHITE){
      return cell.getFigure().symbol() == 'k';
    }else {
      return cell.getFigure().symbol() == 'K';
    }
  }
}
