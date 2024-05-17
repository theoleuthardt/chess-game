package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;

import java.util.List;

public interface Figure {
  List<Cell> getAvailableCells(Cell currentRook);

  boolean canMoveTo(Cell prevCell, Cell nextCell);

  char symbol();

  FigureColor color();

  FigureType type();

  static Figure fromTypeAndColor(FigureType type, FigureColor color) {
    return switch (type) {
      case BISHOP -> new Bishop(color);
      case KNIGHT -> new Knight(color);
      case QUEEN -> new Queen(color);
      case ROOK -> new Rook(color);
      default -> throw new IllegalArgumentException("This is not a valid FigureType");
    };
  }

  static Figure fromChar(char c) {
    return switch (c) {
      case 'b' -> new Bishop(FigureColor.BLACK);
      case 'k' -> new King(FigureColor.BLACK);
      case 'n' -> new Knight(FigureColor.BLACK);
      case 'p' -> new Pawn(FigureColor.BLACK);
      case 'q' -> new Queen(FigureColor.BLACK);
      case 'r' -> new Rook(FigureColor.BLACK);
      case 'B' -> new Bishop(FigureColor.WHITE);
      case 'K' -> new King(FigureColor.WHITE);
      case 'N' -> new Knight(FigureColor.WHITE);
      case 'P' -> new Pawn(FigureColor.WHITE);
      case 'Q' -> new Queen(FigureColor.WHITE);
      case 'R' -> new Rook(FigureColor.WHITE);
      default -> throw new IllegalArgumentException("Invalid char for figure type!");
    };
  }
}
