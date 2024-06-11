package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;

import java.util.ArrayList;
import java.util.List;

public record Queen(FigureColor color) implements Figure {
  private static final FigureType type = FigureType.QUEEN;

  @Override
  public List<Cell> availableCells(Cell currentCell) {
    List<Cell> cells = new ArrayList<>();

    for (CellDirection direction : CellDirection.values()) {
      currentCell.addAvailableCellsInDirectionToList(cells, direction);
    }
    return cells;
  }

  @Override
  public boolean canMoveTo(Cell startCell, Cell endCell) {
    return availableCells(startCell).contains(endCell);
  }

  @Override
  public char symbol() {
    return color == FigureColor.WHITE ? 'Q' : 'q';
  }

  @Override
  public FigureType type() {
    return type;
  }
}
