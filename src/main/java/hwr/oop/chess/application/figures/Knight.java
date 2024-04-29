package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;
import java.util.Objects;
import java.util.ArrayList;

public class Knight implements Figure {
  private static final FigureType type = FigureType.KNIGHT;
  private final FigureColor color;

  public Knight(FigureColor color) {
    this.color = color;
  }

  public ArrayList<Cell> getAvailableCells(Cell currentCell) {
    ArrayList<Cell> cells = new ArrayList<>();

    if (currentCell.topCell() != null) {
      cells.add(currentCell.topCell().topLeftCell());
      cells.add(currentCell.topCell().topRightCell());
    }
    if (currentCell.bottomCell() != null) {
      cells.add(currentCell.bottomCell().bottomLeftCell());
      cells.add(currentCell.bottomCell().bottomRightCell());
    }
    if (currentCell.leftCell() != null) {
      cells.add(currentCell.leftCell().topLeftCell());
      cells.add(currentCell.leftCell().bottomLeftCell());
    }
    if (currentCell.rightCell() != null) {
      cells.add(currentCell.rightCell().topRightCell());
      cells.add(currentCell.rightCell().bottomRightCell());
    }

    // Remove if cell is null
    cells.removeIf(Objects::isNull);

    // Remove cell if figure is mine
    cells.removeIf(cell -> cell.figure() != null && cell.figure().color() == color());

    return cells;
  }

  public boolean canMoveTo(Cell prevCell, Cell nextCell) {
    ArrayList<Cell> availableCell = getAvailableCells(prevCell);
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
