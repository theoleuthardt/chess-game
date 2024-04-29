package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;

import java.util.ArrayList;

public class Bishop implements Figure {
  private static final FigureType type = FigureType.BISHOP;
  private final FigureColor color;

  public Bishop(FigureColor color) {
    this.color = color;
  }

  public ArrayList<Cell> getAvailableCells(Cell currentCell) {
    ArrayList<Cell> cells = new ArrayList<>();

    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.TOP_LEFT);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.TOP_RIGHT);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.BOTTOM_LEFT);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.BOTTOM_RIGHT);

    return cells;
  }

  public boolean canMoveTo(Cell prevCell, Cell nextCell) {
    ArrayList<Cell> availableCell = getAvailableCells(prevCell);
    return availableCell.contains(nextCell);
  }

  public char symbol() {
    return color == FigureColor.WHITE ? 'B' : 'b';
  }

  public FigureColor color() {
    return color;
  }

  public FigureType type() {
    return type;
  }
}
