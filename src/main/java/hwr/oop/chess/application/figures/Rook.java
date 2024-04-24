package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;

import java.util.ArrayList;

public class Rook implements Figure {
  private static final FigureType type = FigureType.ROOK;
  private final FigureColor color;

  public Rook(FigureColor color) {
    this.color = color;
  }

  public ArrayList<Cell> getAvailableCells(Cell currentRook) {
    ArrayList<Cell> list = new ArrayList<>();

    // Check above
    Cell current = currentRook.topCell();

    // If there is no figure or if it's a different color, the piece can move
    while (current != null && current.getFigure() == null) {
      list.add(current);
      current = current.topCell();
    }
    if (current != null
        && current.getFigure() != null
        && current.getFigure().color() != currentRook.getFigure().color()) {
      list.add(current);
    }

    // Check below
    current = currentRook.bottomCell();
    while (current != null && current.getFigure() == null) {
      list.add(current);
      current = current.bottomCell();
    }
    if (current != null
        && current.getFigure() != null
        && current.getFigure().color() != currentRook.getFigure().color()) {
      list.add(current);
    }

    // Check the right
    current = currentRook.rightCell();
    while (current != null && current.getFigure() == null) {
      list.add(current);
      current = current.rightCell();
    }
    if (current != null
        && current.getFigure() != null
        && current.getFigure().color() != currentRook.getFigure().color()) {
      list.add(current);
    }

    // Check the left
    current = currentRook.leftCell();
    while (current != null && current.getFigure() == null) {
      list.add(current);
      current = current.leftCell();
    }
    if (current != null
        && current.getFigure() != null
        && current.getFigure().color() != currentRook.getFigure().color()) {
      list.add(current);
    }
    System.out.println("availableCell: " + list.toArray().length);
    return list;
  }

  public boolean canMoveTo(Cell prevCell, Cell nextCell) {
    ArrayList<Cell> availableCell = getAvailableCells(prevCell);
    System.out.println("canMove: " + availableCell.contains(nextCell));
    return availableCell.contains(nextCell);
  }

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
