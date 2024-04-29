package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;

import java.util.ArrayList;

public interface Figure {
  ArrayList<Cell> getAvailableCells(Cell currentRook);

  boolean canMoveTo(Cell prevCell, Cell nextCell);

  // TODO write boolean isCheck(Cell currentCell);

  char symbol();

  FigureColor color();

  FigureType type();
}
