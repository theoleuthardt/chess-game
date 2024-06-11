package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;

import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

public record Knight(FigureColor color) implements Figure {
  private static final FigureType type = FigureType.KNIGHT;

  @Override
  public List<Cell> availableCells(Cell currentCell) {
    List<Cell> cells = new ArrayList<>();

    if (currentCell.hasTopCell()) {
      cells.add(currentCell.topCell().topLeftCell());
      cells.add(currentCell.topCell().topRightCell());
    }
    if (currentCell.hasBottomCell()) {
      cells.add(currentCell.bottomCell().bottomLeftCell());
      cells.add(currentCell.bottomCell().bottomRightCell());
    }
    if (currentCell.hasLeftCell()) {
      cells.add(currentCell.leftCell().topLeftCell());
      cells.add(currentCell.leftCell().bottomLeftCell());
    }
    if (currentCell.hasRightCell()) {
      cells.add(currentCell.rightCell().topRightCell());
      cells.add(currentCell.rightCell().bottomRightCell());
    }

    // Remove if cell is null
    cells.removeIf(Objects::isNull);

    // Remove cell if figure is mine
    cells.removeIf(cell -> cell.isOccupiedBy(color()));

    return cells;
  }

  @Override
  public boolean canMoveTo(Cell startCell, Cell endCell) {
    return availableCells(startCell).contains(endCell);
  }

  @Override
  public char symbol() {
    return color == FigureColor.WHITE ? 'N' : 'n';
  }

  @Override
  public FigureType type() {
    return type;
  }
}
