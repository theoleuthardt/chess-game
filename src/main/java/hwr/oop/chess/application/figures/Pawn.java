package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;

import java.util.ArrayList;

public class Pawn implements Figure {
  private Cell startPosition = null;
  private Cell currentPosition = null;
  private static final FigureType type = FigureType.PAWN;
  private final FigureColor color;

  public Pawn(FigureColor color, int x, int y) {
    Cell position = new Cell(x,y);
    this.startPosition = position;
    this.currentPosition = position;
    this.color = color;
  }

  public boolean canMoveTo(Cell to) {
    Cell from = this.currentPosition;

    // white figures move from bottom up (increasing y position)
    // black figures move from top down (decreasing y position)
    int oneField = (this.color == FigureColor.BLACK) ? -1 : 1;
    int twoFields = oneField * 2;

    // Move one field straight up/down if the way is free
    boolean isToFieldBlocked = Board.isFigureOnField(to.x(), to.y());
    if (!isToFieldBlocked
            && (from.x() == to.x())
            && (from.y() + oneField) == to.y()) {
      return true;
    }

    // Moving two fields from the start position if both fields are free
    boolean isFieldInFrontOfPawnBlocked = Board.isFigureOnField(from.x(), from.y() + oneField);
    if (!isToFieldBlocked
            && !isFieldInFrontOfPawnBlocked

            && (startPosition == from)
            && (from.x()      == to.x())
            && (from.y() + twoFields) == to.y()) {
      return true;
    }

    // Move one field diagonally if the opponent is there
    Figure opponent = Board.getFigureOnField(to.x(), to.y());
    if (isToFieldBlocked // check if a different figure is on the new field
            && opponent != null
            && (opponent.getColor() == FigureColor.WHITE)
            && (Math.abs(from.x() - to.x()) == 1)
            && (from.y() + oneField) == to.y()) {
      opponent.moveTo(-1,-1); // catch opponent figure
      return true;
    }

    // this move is not allowed as it does not obey the rules.
    return false;
  }

  @Override
  public ArrayList<Cell> getAvailablePosition(Cell currentRook) {
    return null;
  }

  @Override
  public boolean canMoveTo(Cell prevPosition, Cell nextPosition) {
    return true;
  }

  public boolean isOnField(int x, int y) {
    Cell field = new Cell(x, y);
    return this.currentPosition.isEqualTo(field);
  }

  public boolean isCaptured() {
    return this.currentPosition == null;
  }

  public void moveTo(int x, int y) {
    Cell position = new Cell(x, y);
    if(canMoveTo(position)) {
      this.setPosition(position);
    }
  }






  

  @Override
  public void moveTo(Cell prevPosition, Cell nextPosition) {

  }

  public void setPosition(Cell position) {
    this.currentPosition = position;
  }

  public Cell cell() {
    return this.currentPosition;
  }

  public FigureColor color() {
    return this.color;
  }

  public FigureType type() {
    return type;
  }

  public char symbol(){
    if(this.color == FigureColor.WHITE){
      return 'P';
    }else{
      return 'p';
    }
  }
}
