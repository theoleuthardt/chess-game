package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Position;

import java.util.ArrayList;

public interface Figure {
  public ArrayList<Position> getAvailablePosition(Position currentRook);
  public boolean canMoveTo(Position prevPosition, Position nextPosition);

  boolean isOnField(int x, int y);
  // void capture();
  // boolean isCaptured();

  void moveTo(int x, int y); // #TODO delete
  void moveTo(Position prevPosition, Position nextPosition);

  boolean isCaptured();

  void setPosition(Position position);

  char getSymbol();

  public Position getPosition();

  public FigureColor getColor();

  public FigureType getType();
}
