package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;
import hwr.oop.chess.cli.InvalidUserInputException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pawn implements Figure {
  private static final FigureType type = FigureType.PAWN;
  private final FigureColor color;

  public Pawn(FigureColor color) {
    this.color = color;
  }

  private boolean isInStartPosition(Cell cell) {
    return cell.y() == (color == FigureColor.WHITE ? 2 : 7);
  }

  public CellDirection forwards() {
    return color() == FigureColor.WHITE ? CellDirection.TOP : CellDirection.BOTTOM;
  }

  public List<Cell> availableCells(Cell currentCell) {

    Cell oneFieldForwards = currentCell.cellInDirection(forwards());
    if (!currentCell.hasCellInDirection(forwards())) {
      return Collections.emptyList();
    }
    List<Cell> cells = new ArrayList<>();
    // move one field forwards
    if (oneFieldForwards.isFree()) {
      cells.add(oneFieldForwards);
    }

    // move two fields forwards
    Cell twoFieldForwards = oneFieldForwards.cellInDirection(forwards());
    if (isInStartPosition(currentCell) && oneFieldForwards.isFree() && twoFieldForwards.isFree()) {
      cells.add(twoFieldForwards);
    }

    // move one field diagonally left
    Cell diagonalLeftCell = oneFieldForwards.leftCell();
    if (oneFieldForwards.hasLeftCell()
        && (diagonalLeftCell.isOccupiedByOpponentOf(color())
            || canPerformEnPassant(currentCell, diagonalLeftCell))) {
      cells.add(diagonalLeftCell);
    }

    // move one field diagonally right
    Cell diagonalRightCell = oneFieldForwards.rightCell();
    if (oneFieldForwards.hasRightCell()
        && (diagonalRightCell.isOccupiedByOpponentOf(color())
            || canPerformEnPassant(currentCell, diagonalRightCell))) {
      cells.add(diagonalRightCell);
    }
    return cells;
  }

  public boolean canMoveTo(Cell prevCell, Cell nextCell) {
    List<Cell> availableCell = availableCells(prevCell);
    return availableCell.contains(nextCell);
  }

  public boolean isAbleToPromote(Cell currentCell) {
    return currentCell.cellInDirection(forwards()) == null;
  }

  // Pawn Promotion
  public void promotePawn(Cell currentCell, FigureType toType) {
    if (!isAbleToPromote(currentCell)) {
      throw new InvalidUserInputException(
          "The pawn is not allowed to be promoted as it has not reached the end.");
    }
    List<FigureType> canPromoteTo = getPromotionTypes();
    if (!canPromoteTo.contains(toType)) {
      throw new InvalidUserInputException("The pawn cannot become a " + toType.name() + ".");
    }
    Figure promoteTo = Figure.fromTypeAndColor(toType, color());
    currentCell.setFigure(promoteTo);
  }

  public char symbol() {
    return color == FigureColor.WHITE ? 'P' : 'p';
  }

  public FigureColor color() {
    return color;
  }

  public FigureType type() {
    return type;
  }

  public List<FigureType> getPromotionTypes() {
    return List.of(FigureType.QUEEN, FigureType.ROOK, FigureType.BISHOP, FigureType.KNIGHT);
  }

  public boolean canPerformEnPassant(Cell startCell, Cell endCell) {
    if (!endCell.isEnPassant()) {
      return false;
    }

    Cell forwardsCell = startCell.cellInDirection(forwards());
    CellDirection direction;
    if (endCell == forwardsCell.leftCell()) {
      direction = CellDirection.LEFT;
    } else if (endCell == forwardsCell.rightCell()) {
      direction = CellDirection.RIGHT;
    } else {
      return false;
    }

    Cell opponentPawnCell = startCell.cellInDirection(direction);
    return opponentPawnCell.isOccupiedBy(color().opposite(), FigureType.PAWN);
  }
}
