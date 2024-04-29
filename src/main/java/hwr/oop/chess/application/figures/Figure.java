package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;

import java.util.ArrayList;

public interface Figure {
  ArrayList<Cell> getAvailableCells(Cell currentRook);

  boolean canMoveTo(Cell prevCell, Cell nextCell);

  char symbol();

  FigureColor color();

  FigureType type();

  // TODO write @Overide in other Figure Class
}
