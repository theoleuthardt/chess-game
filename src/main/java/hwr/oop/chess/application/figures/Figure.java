package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;

import java.util.ArrayList;

public interface Figure {
  public ArrayList<Cell> getAvailableCell(Cell currentRook);
  public boolean canMoveTo(Cell prevCell, Cell nextCell);

//  boolean isOnField(int x, int y);
// void capture();
// boolean isCaptured();
Cell
  void moveTo(Cell prevCell, Cell nextCell);

//  boolean isCaptured();

  void setCell(Cell cell);

  char getSymbol();

  public Cell getCell();

  public FigureColor getColor();

  public FigureType getType();
}
