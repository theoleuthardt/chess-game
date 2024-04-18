package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Position;

public interface Figure {


  boolean canMoveTo(Position newPosition);

  boolean isOnField(Position field);
  // void capture();
  // boolean isCaptured();

  void moveTo(Position position);
  Position position();
  FigureColor color();
  FigureType type();

  char getSymbol();
}
