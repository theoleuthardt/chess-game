package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;

import java.util.ArrayList;

public class Queen implements Figure {
  private static final FigureType type = FigureType.QUEEN;
  private final FigureColor color;

  public Queen(FigureColor color) {
    this.color = color;
  }

  public ArrayList<Cell> getAvailableCells(Cell currentCell) {
    ArrayList<Cell> cells = new ArrayList<>();

    for (CellDirection direction : CellDirection.values()) {
      currentCell.addAvailableCellsInDirectionToList(cells, direction);
    }
     return cells;
  }

  public boolean canMoveTo(Cell prevCell, Cell nextCell) {
    ArrayList<Cell> availableCell = getAvailableCells(prevCell);
    return availableCell.contains(nextCell);
  }

  public char symbol() {
    return color == FigureColor.WHITE ? 'Q' : 'q';
  }

  public FigureColor color() {
    return color;
  }

  public FigureType type() {
    return type;
  }

}
