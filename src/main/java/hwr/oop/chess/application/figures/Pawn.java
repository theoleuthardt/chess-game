package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;
import hwr.oop.chess.cli.InvalidUserInputException;

import java.util.ArrayList;
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

  private CellDirection forwards() {
    return color() == FigureColor.WHITE ? CellDirection.TOP : CellDirection.BOTTOM;
  }

  public List<Cell> getAvailableCells(Cell currentCell) {
    List<Cell> cells = new ArrayList<>();

    Cell oneFieldForwards = currentCell.cellInDirection(forwards());
    if (!currentCell.hasCellInDirection(forwards())) {
      return cells;
    }

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
        && diagonalLeftCell.isOccupied()
        && diagonalLeftCell.figure().color() != color()) {
      cells.add(diagonalLeftCell);
    }

    // move one field diagonally right
    Cell diagonalRightCell = oneFieldForwards.rightCell();
    if (oneFieldForwards.hasRightCell()
        && diagonalRightCell.isOccupied()
        && diagonalRightCell.figure().color() != color()) {
      cells.add(diagonalRightCell);
    }
    return cells;
  }

  public boolean canMoveTo(Cell prevCell, Cell nextCell) {
    List<Cell> availableCell = getAvailableCells(prevCell);
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
    if (promoteTo.type() == FigureType.ROOK) {
      ((Rook) promoteTo).figureMoved();
    }
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
}
