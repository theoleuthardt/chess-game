package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;

import java.util.logging.Logger;

import java.util.ArrayList;

public class Queen implements Figure {
  Logger logger = Logger.getLogger(getClass().getName());
  private static final FigureType type = FigureType.QUEEN;
  private final FigureColor color;
  private final ArrayList<CellDirection> directions;

  public Queen(FigureColor color) {
    this.color = color;
    directions = new ArrayList<>();
    directions.add(CellDirection.LEFT);
    directions.add(CellDirection.RIGHT);
    directions.add(CellDirection.TOP);
    directions.add(CellDirection.TOP_LEFT);
    directions.add(CellDirection.TOP_RIGHT);
    directions.add(CellDirection.BOTTOM);
    directions.add(CellDirection.BOTTOM_LEFT);
    directions.add(CellDirection.BOTTOM_RIGHT);
  }

  public ArrayList<Cell> getAvailableCells(Cell currentCell) {
    ArrayList<Cell> cells = new ArrayList<>();

    for (CellDirection direction : directions) {
      currentCell.addAvailableCellsInDirectionToList(cells, direction);
    }
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

  public ArrayList<CellDirection> directions() {
    return directions;
  }
}
