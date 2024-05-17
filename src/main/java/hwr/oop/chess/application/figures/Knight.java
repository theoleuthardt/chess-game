package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;

import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

public class Knight implements Figure {
  private static final FigureType type = FigureType.KNIGHT;
  private final FigureColor color;

  public Knight(FigureColor color) {
    this.color = color;
  }

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

  public boolean canMoveTo(Cell prevCell, Cell nextCell) {
    List<Cell> availableCell = availableCells(prevCell);
    return availableCell.contains(nextCell);
  }

  public char symbol() {
    return color == FigureColor.WHITE ? 'N' : 'n';
  }

  public FigureColor color() {
    return color;
  }

  public FigureType type() {
    return type;
  }
}
