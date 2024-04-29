package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Rook implements Figure {
  private static final FigureType type = FigureType.ROOK;
  private final FigureColor color;

  public Rook(FigureColor color) {
    this.color = color;
  }

  public ArrayList<Cell> getAvailableCells(Cell currentCell) {
    ArrayList<Cell> cells = new ArrayList<>();

    // Check above
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.LEFT);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.RIGHT);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.TOP);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.BOTTOM);

    Logger logger = Logger.getLogger(this.getClass().getName());
    logger.info("Available cells: " + cells.toArray().length);
    return cells;
  }

  public boolean canMoveTo(Cell prevCell, Cell nextCell) {
    ArrayList<Cell> availableCell = getAvailableCells(prevCell);
    Logger logger = Logger.getLogger(this.getClass().getName());
    logger.info("canMove: " + availableCell.contains(nextCell));
    return availableCell.contains(nextCell);
  }

  /*public boolean canCaptureKing(Cell current, FigureColor myColor) {
    ArrayList<Cell> availableCells = getAvailableCells(current);
    for (Cell cell : availableCells) {
      isOpponentKing(cell, myColor);
    }
    return true;
  }*/

  public char symbol() {
    return color == FigureColor.WHITE ? 'R' : 'r';
  }

  public FigureColor color() {
    return color;
  }

  public FigureType type() {
    return type;
  }
}
