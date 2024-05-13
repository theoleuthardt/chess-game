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
  private boolean castlingKing;
  private boolean castlingQueen;
  private boolean hasMoved;

  public King(FigureColor color) {
    this.color = color;
    this.castlingKing = false;
    this.castlingQueen = false;
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
    if (!castlingKing && !hasMoved && isEmptyBetweenCells(currentCell, CellDirection.RIGHT, 2)) {
      Cell rook = findNextCell(currentCell, CellDirection.RIGHT, 3);
      if(rook.figure().type() == FigureType.ROOK){
        cells.add(rook);
      }
    }
    // Add cells for castling Queen
    if(!castlingQueen && !hasMoved && isEmptyBetweenCells(currentCell, CellDirection.LEFT, 3)) {
      Cell rook = findNextCell(currentCell, CellDirection.LEFT, 4);
      if(rook.figure().type() == FigureType.ROOK){
        cells.add(rook);
      }
    }

    return cells;
  }

  public boolean canMoveTo(Cell prevCell, Cell nextCell) {
    List<Cell> availableCell = getAvailableCells(prevCell);
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
}
