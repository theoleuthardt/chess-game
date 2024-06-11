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

  @Override
  public List<Cell> availableCells(Cell currentCell) {
    List<Cell> cells = new ArrayList<>();

    // Check above
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.LEFT);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.RIGHT);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.TOP);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.BOTTOM);

    return cells;
  }

  @Override
  public boolean canMoveTo(Cell startCell, Cell endCell) {
    return availableCells(startCell).contains(endCell);
  }

  @Override
  public char symbol() {
    return color == FigureColor.WHITE ? 'R' : 'r';
  }

  @Override
  public FigureColor color() {
    return color;
  }

  @Override
  public FigureType type() {
    return type;
  }

  public boolean hasMoved() {
    return this.hasMoved;
  }

  public void figureMoved() {
    if (!this.hasMoved) {
      this.hasMoved = true;
    }
  }
}
