package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;

import java.util.logging.Logger;

import java.util.ArrayList;

public class Pawn implements Figure {
  // TODO pawn promotion
  Logger logger = Logger.getLogger(getClass().getName());
  private static final FigureType type = FigureType.PAWN;
  private final FigureColor color;

  public Pawn(FigureColor color) {
    this.color = color;
  }

  // TODO Fix AvailableCells
  public ArrayList<Cell> getAvailableCells(Cell currentCell) {
    ArrayList<Cell> cells = new ArrayList<>();

    CellDirection forwards = CellDirection.TOP;
    boolean isInStartPosition = currentCell.y() == 2;
    if (color() == FigureColor.BLACK) {
      forwards = CellDirection.BOTTOM;
      isInStartPosition = currentCell.y() == 7;
    }

    Cell oneFieldForwards = currentCell.cellInDirection(forwards);
    Cell twoFieldForwards =
        oneFieldForwards == null ? null : oneFieldForwards.cellInDirection(forwards);

    // move one field forwards
    if (oneFieldForwards != null && oneFieldForwards.getFigure() == null) {
      cells.add(oneFieldForwards);
    }

    // move two fields forwards
    if (isInStartPosition
        && oneFieldForwards != null
        && twoFieldForwards != null
        && oneFieldForwards.getFigure() == null
        && twoFieldForwards.getFigure() == null) {
      cells.add(twoFieldForwards);
    }

    // move one field diagonally
    if (oneFieldForwards != null) {
      Cell diagonalLeftCell = oneFieldForwards.leftCell();
      if (diagonalLeftCell != null
          && diagonalLeftCell.getFigure() != null
          && diagonalLeftCell.getFigure().color() != color()) {
        cells.add(diagonalLeftCell);
      }

      Cell diagonalRightCell = oneFieldForwards.rightCell();
      if (diagonalRightCell != null
          && diagonalRightCell.getFigure() != null
          && diagonalRightCell.getFigure().color() != color()) {
        cells.add(diagonalRightCell);
      }
    }
    return cells;
  }

  public boolean canMoveTo(Cell prevCell, Cell nextCell) {
    ArrayList<Cell> availableCell = getAvailableCells(prevCell);
    logger.info("canMove: " + availableCell.contains(nextCell));
    return availableCell.contains(nextCell);
  }

  // Pawn Promotion
  public boolean promotePawn(Cell currentCell, Cell nextCell) {
    ArrayList<Cell> availableCell = getAvailableCells(currentCell);
    logger.info("promotePawn: " + availableCell.contains(nextCell));
    return availableCell.contains(nextCell);
  }

  // choose figure for the promotion
  public int getPromotionFigure() {
    int selectFigure;
    int promotionFigure;
    switch (selectFigure) {
      case 1:
        promotionFigure = FigureType.ROOK;
        break;
      case 2:
        promotionFigure = FigureType.BISHOP;
        break;
      case 3:
        promotionFigure = FigureType.KNIGHT;
        break;
      default:
      case 0:
        promotionFigure = FigureType.QUEEN;
    }
    return promotionFigure;
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
