package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;
import java.util.ArrayList;
import java.util.List;

public class King implements Figure {
  private static final FigureType type = FigureType.KING;
  private final FigureColor color;
  private boolean hasMoved;

  public King(FigureColor color) {
    this.color = color;
    this.hasMoved = false;
  }

  public List<Cell> availableCells(Cell currentCell) {
    List<Cell> cells = new ArrayList<>();

    // Loop though all Directions
    for (CellDirection direction : CellDirection.values()) {
      if (currentCell.hasCellInDirection(direction)) {
        Cell neighbourCell = currentCell.cellInDirection(direction);
        if (neighbourCell.isFree() || neighbourCell.isOccupiedByOpponentOf(color())) {
          cells.add(neighbourCell);
        }
      }
    }

    // Add cells for castling King
    if (canPerformKingSideCastling(currentCell)) {
      cells.add(kingSideCastlingCell(currentCell));
    }

    // Add cells for castling Queen
    if (canPerformQueenSideCastling(currentCell)) {
      cells.add(queenSideCastlingCell(currentCell));
    }

    return cells;
  }

  public Cell kingSideCastlingCell(Cell currentCell) {
    return currentCell.findCellInDirection(2, CellDirection.RIGHT);
  }

  public Cell queenSideCastlingCell(Cell currentCell) {
    return currentCell.findCellInDirection(2, CellDirection.LEFT);
  }

  public boolean canPerformKingSideCastling(Cell currentCell) {
    if (hasMoved || !currentCell.isFreeInDirection(2, CellDirection.RIGHT)) {
      return false;
    }

    Cell rookCell = currentCell.findCellInDirection(3, CellDirection.RIGHT);
    return rookCell.isOccupiedBy(color(), FigureType.ROOK)
        && !((Rook) rookCell.figure()).hasMoved();
  }

  public boolean canPerformQueenSideCastling(Cell currentCell) {
    if (hasMoved || !currentCell.isFreeInDirection(3, CellDirection.LEFT)) {
      return false;
    }

    Cell rookCell = currentCell.findCellInDirection(4, CellDirection.LEFT);
    return rookCell.isOccupiedBy(color(), FigureType.ROOK)
        && !((Rook) rookCell.figure()).hasMoved();
  }

  public boolean canMoveTo(Cell prevCell, Cell nextCell) {
    List<Cell> availableCell = availableCells(prevCell);
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

  public boolean hasMoved() {
    return this.hasMoved;
  }

  public void figureMoved() {
    if (!this.hasMoved) {
      this.hasMoved = true;
    }
  }
}
