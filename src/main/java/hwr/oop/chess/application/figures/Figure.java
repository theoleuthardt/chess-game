package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;

import java.util.ArrayList;

public interface Figure {
  ArrayList<Cell> getAvailableCells(Cell currentRook);

  boolean canMoveTo(Cell prevCell, Cell nextCell);

  // boolean canCaptureKing(Cell current, FigureColor myColor);
  // boolean isChecked(Cell current);

  char symbol();

  FigureColor color();

  FigureType type();
}
