package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;

import java.util.ArrayList;
import java.util.List;

public class Queen implements Figure {
  private static final FigureType type = FigureType.QUEEN;
  private final FigureColor color;

  public Queen(FigureColor color) {
    this.color = color;
  }

  @Override
  public List<Cell> availableCells(Cell currentCell) {
    List<Cell> cells = new ArrayList<>();

    for (CellDirection direction : CellDirection.values()) {
      currentCell.addAvailableCellsInDirectionToList(cells, direction);
    }
    return cells;
  }

  @Override
  public boolean canMoveTo(Cell prevCell, Cell nextCell) {
    List<Cell> availableCell = availableCells(prevCell);
    return availableCell.contains(nextCell);
  }

  @Override
  public char symbol() {
    return color == FigureColor.WHITE ? 'Q' : 'q';
  }

  @Override
  public FigureColor color() {
    return color;
  }

  @Override
  public FigureType type() {
    return type;
  }
}
