package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;

import java.util.ArrayList;
import java.util.List;

public class Rook implements Figure {
  private static final FigureType type = FigureType.ROOK;
  private final FigureColor color;
  private boolean hasMoved;

  public Rook(FigureColor color) {
    this.color = color;
    this.hasMoved = false;
  }

  public List<Cell> getAvailableCells(Cell currentCell) {
    List<Cell> cells = new ArrayList<>();

    // Check above
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.LEFT);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.RIGHT);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.TOP);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.BOTTOM);

    return cells;
  }

  public boolean canMoveTo(Cell prevCell, Cell nextCell) {
    List<Cell> availableCell = getAvailableCells(prevCell);
    return availableCell.contains(nextCell);
  }

  public char symbol() {
    return color == FigureColor.WHITE ? 'R' : 'r';
  }

  public FigureColor color() {
    return color;
  }

  public FigureType type() {
    return type;
  }

  public boolean hasMoved() { return this.hasMoved; }

  public void figureMoved() {
    if (!this.hasMoved) {
      this.hasMoved = true;
    }
  }
}
