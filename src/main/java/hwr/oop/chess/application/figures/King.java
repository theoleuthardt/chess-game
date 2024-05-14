package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;
import java.util.ArrayList;
import java.util.List;

import static hwr.oop.chess.application.Cell.findNextCell;
import static hwr.oop.chess.application.Cell.isEmptyBetweenCells;

public class King implements Figure {
  private static final FigureType type = FigureType.KING;
  private final FigureColor color;
  private boolean hasMoved;

  public King(FigureColor color) {
    this.color = color;
    this.hasMoved = false;
  }

  public List<Cell> getAvailableCells(Cell currentCell) {
    List<Cell> cells = new ArrayList<>();

    // Loop though all Directions
    for (CellDirection direction : CellDirection.values()) {
      Cell neighbourCell = currentCell.cellInDirection(direction);
      if (neighbourCell != null
          && (neighbourCell.isFree() || neighbourCell.figure().color() != color())) {
        cells.add(neighbourCell);
      }
    }

    // Add cells for castling King
    if(canCastlingKing(currentCell)){
      Cell kingAfterCastling = findNextCell(currentCell, CellDirection.RIGHT, 2);
      cells.add(kingAfterCastling);
    }

    // Add cells for castling Queen
    if (canCastlingQueen(currentCell)) {
      Cell kingAfterCastling = findNextCell(currentCell, CellDirection.LEFT, 2);
      cells.add(kingAfterCastling);
    }

    return cells;
  }

  public boolean canCastlingKing(Cell currentCell) {
    if (!hasMoved && isEmptyBetweenCells(currentCell, CellDirection.RIGHT, 2)) {
      Cell rookCell = findNextCell(currentCell, CellDirection.RIGHT, 3);
      Rook rook = (Rook) rookCell.figure();
      return rookCell.figure().type() == FigureType.ROOK && !rook.hasMoved();
    }
    return false;
  }

  public boolean canCastlingQueen(Cell currentCell) {
    if(!hasMoved && isEmptyBetweenCells(currentCell, CellDirection.LEFT, 3)) {
      Cell rookCell = findNextCell(currentCell, CellDirection.LEFT, 4);
      Rook rook = (Rook) rookCell.figure();
      return rookCell.figure().type() == FigureType.ROOK && !rook.hasMoved();
    }
    return false;
  }


  public boolean canMoveTo(Cell prevCell, Cell nextCell) {
    List<Cell> availableCell = getAvailableCells(prevCell);
    return availableCell.contains(nextCell);
  }

  public char symbol() {
    return color == FigureColor.WHITE ? 'K' : 'k';
  }

  public FigureColor color() { return color; }

  public FigureType type() { return type; }

  public boolean hasMoved() { return this.hasMoved; }

  public void figureMoved() {
    if (!this.hasMoved) {
      this.hasMoved = true;
    }
  }
}
