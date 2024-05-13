package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;

import java.util.List;

public interface Figure {
  List<Cell> getAvailableCells(Cell currentRook);

  boolean canMoveTo(Cell prevCell, Cell nextCell);

  char symbol();

  FigureColor color();

  FigureType type();

  default Figure getFigureFromTypeAndColor(FigureType type, FigureColor color) {
    return switch (type) {
      case BISHOP -> new Bishop(color);
      case KNIGHT -> new Knight(color);
      case QUEEN -> new Queen(color);
      case ROOK -> new Rook(color);
      default -> throw new IllegalArgumentException("This is not a valid FigureType");
    };
  }
}
