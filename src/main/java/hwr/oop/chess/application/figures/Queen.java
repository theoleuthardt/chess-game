package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;

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
    ArrayList<Cell> cells = new ArrayList<>();

    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.LEFT);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.RIGHT);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.TOP);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.TOP_LEFT);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.TOP_RIGHT);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.BOTTOM);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.BOTTOM_LEFT);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.BOTTOM_RIGHT);

    return cells;
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
