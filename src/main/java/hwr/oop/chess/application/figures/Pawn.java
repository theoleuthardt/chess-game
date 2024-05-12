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

  public ArrayList<Cell> getAvailableCells(Cell currentCell) {
    ArrayList<Cell> cells = new ArrayList<>();

    Cell oneFieldForwards = currentCell.cellInDirection(forwards());
    if (oneFieldForwards == null) {
      return cells;
    }

    Cell twoFieldForwards = oneFieldForwards.cellInDirection(forwards());

    // move one field forwards
    if (oneFieldForwards.figure() == null) {
      cells.add(oneFieldForwards);
    }

    // move two fields forwards
    if (isInStartPosition(currentCell)
        && twoFieldForwards != null
        && oneFieldForwards.figure() == null
        && twoFieldForwards.figure() == null) {
      cells.add(twoFieldForwards);
    }

    // move one field diagonally left
    Cell diagonalLeftCell = oneFieldForwards.leftCell();
    if (diagonalLeftCell != null
        && diagonalLeftCell.figure() != null
        && diagonalLeftCell.figure().color() != color()) {
      cells.add(diagonalLeftCell);
    }

    // move one field diagonally right
    Cell diagonalRightCell = oneFieldForwards.rightCell();
    if (diagonalRightCell != null
        && diagonalRightCell.figure() != null
        && diagonalRightCell.figure().color() != color()) {
      cells.add(diagonalRightCell);
    }
    return cells;
  }

  public boolean canMoveTo(Cell prevCell, Cell nextCell) {
    ArrayList<Cell> availableCell = getAvailableCells(prevCell);
    return availableCell.contains(nextCell);
  }

  private boolean isAbleToPromote(Cell currentCell) {
    return currentCell.cellInDirection(forwards()) == null;
  }

  // Pawn Promotion
  public void promotePawn(Cell currentCell, FigureType toType) {
    if (!isAbleToPromote(currentCell)) {
      throw new InvalidUserInputException(
          "The pawn is not allowed to be promoted as it has not reached the end.");
    }
    List<FigureType> canPromoteTo =
        List.of(FigureType.QUEEN, FigureType.ROOK, FigureType.BISHOP, FigureType.KNIGHT);
    if (!canPromoteTo.contains(toType)) {
      throw new InvalidUserInputException("The pawn cannot become a " + toType.name() + ".");
    }
    currentCell.setFigure(getFigureFromTypeAndColor(toType, color()));
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
}
