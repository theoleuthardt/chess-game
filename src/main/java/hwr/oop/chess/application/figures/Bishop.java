package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;

import java.util.ArrayList;
import java.util.List;

public record Bishop(FigureColor color) implements Figure {
  private static final FigureType type = FigureType.BISHOP;

  @Override
  public List<Cell> availableCells(Cell currentCell) {
    List<Cell> cells = new ArrayList<>();

    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.TOP_LEFT);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.TOP_RIGHT);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.BOTTOM_LEFT);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.BOTTOM_RIGHT);

    return cells;
  }

  @Override
  public boolean canMoveTo(Cell prevCell, Cell nextCell) {
    List<Cell> availableCell = availableCells(prevCell);
    return availableCell.contains(nextCell);
  }

  @Override
  public char symbol() {
    return color == FigureColor.WHITE ? 'B' : 'b';
  }

  @Override
  public FigureType type() {
    return type;
  }
}
