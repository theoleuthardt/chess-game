package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Position;

public class PawnFigure implements Figure {
  private Position startPosition = null;
  private Position currentPosition = null;
  private static final FigureType type = FigureType.PAWN;
  private final FigureColor color;

public PawnFigure(FigureColor color) {
  //  Position position = new Position(x,y);
  //  this.startPosition = position;
  //  this.currentPosition = position;
    this.color = color;
  }  
  

  public boolean canMoveTo(Position to) {
    Position from = this.currentPosition;

    // white figures move from bottom up (increasing y position)
    // black figures move from top down (decreasing y position)
    int oneField = (this.color == FigureColor.BLACK) ? -1 : 1;
    int twoFields = oneField * 2;

    // Move one field straight up/down if the way is free
    boolean isToFieldBlocked = Board.isFigureOnField(to);
    if (!isToFieldBlocked
        && (from.x() == to.x())
        && (from.y() + oneField) == to.y()) {
      return true;
    }

    // Moving two fields from the start position if both fields are free
    boolean isFieldInFrontOfPawnBlocked = Board.isFigureOnField(new Position(from.x(), from.y() + oneField));
    if (!isToFieldBlocked
        && !isFieldInFrontOfPawnBlocked

        && (startPosition == from)
        && (from.x()      == to.x())
        && (from.y() + twoFields) == to.y()) {
      return true;
    }

    // Move one field diagonally if the opponent is there
    Figure opponent = Board.getFigureOnField(to);
    if (isToFieldBlocked // check if a different figure is on the new field
        && opponent != null
        && (opponent.color() == FigureColor.WHITE)
        && (Math.abs(from.x() - to.x()) == 1)
        && (from.y() + oneField) == to.y()) {
      opponent.moveTo(null); // catch opponent figure
      return true;
    }

    // this move is not allowed as it does not obey the rules.
    return false;
  }

  public boolean isOnField(Position field) {
    return this.currentPosition.isEqualTo(field);
  }

  public boolean isCaptured() {
    return this.currentPosition == null;
  }

  public void moveTo(Position position) {
    if(canMoveTo(position)) {
      this.currentPosition = position;
    }
  }

  public Position position() {
    return this.currentPosition;
  }

  public FigureColor color() {
    return this.color;
  }

  public FigureType type() {
    return type;
  }

  public char getSymbol(){
    if(this.color == FigureColor.WHITE){
      return 'P';
    }else{
      return 'p';
    }
  }
}
