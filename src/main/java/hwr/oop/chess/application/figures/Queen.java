package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;
import java.util.logging.Logger;

import java.util.ArrayList;

public class Queen implements Figure {
  Logger logger = Logger.getLogger(getClass().getName());
  private static final FigureType type = FigureType.QUEEN;
  private final FigureColor color;

  public Queen(FigureColor color) {
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

  public char symbol() {
    return color == FigureColor.WHITE ? 'Q' : 'q';
  }

  public FigureColor color() {
    return color;
  }

  public FigureType type() {
    return type;
  }
}
