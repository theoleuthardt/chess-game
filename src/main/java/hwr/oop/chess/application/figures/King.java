package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;
import java.util.ArrayList;

public class King implements Figure {
  private static final FigureType type = FigureType.KING;
  private final FigureColor color;

  public King(FigureColor color) {
    this.color = color;
  }

  public ArrayList<Cell> getAvailableCells(Cell currentCell) {
    ArrayList<Cell> cells = new ArrayList<>();

    // Loop though all Directions
    for (CellDirection direction : CellDirection.values()) {
      Cell neighbourCell = currentCell.cellInDirection(direction);
      if (neighbourCell != null
          && (neighbourCell.figure() == null || neighbourCell.figure().color() != color())) {
        cells.add(neighbourCell);
      }
    }

    return cells;
  }

  public boolean canMoveTo(Cell prevCell, Cell nextCell) {
    ArrayList<Cell> availableCell = getAvailableCells(prevCell);
    return availableCell.contains(nextCell);
  }

  public char symbol() {
    return color == FigureColor.WHITE ? 'K' : 'k';
  }

  public FigureColor color() {
    return color;
  }

  public FigureType type() {
    return type;
  }
}
