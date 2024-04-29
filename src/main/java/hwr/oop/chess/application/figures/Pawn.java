package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;
import java.util.logging.Logger;

import java.util.ArrayList;

public class Pawn implements Figure {
  Logger logger = Logger.getLogger(getClass().getName());
  private static final FigureType type = FigureType.PAWN;
  private final FigureColor color;

  public Pawn(FigureColor color) {
    this.color = color;
  }

  public ArrayList<Cell> getAvailableCells(Cell currentCell) {
    ArrayList<Cell> list = new ArrayList<>();

    return list;
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
