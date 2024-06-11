package hwr.oop.chess.application.figures;

import hwr.oop.chess.cli.InvalidUserInputException;

public enum FigureType {
  KING, // König
  QUEEN, // Dame
  ROOK, // Turm
  BISHOP, // Läufer
  KNIGHT, // Pferd
  PAWN; // Bauer

  public static FigureType fromString(String figure) {
    figure = figure.toUpperCase();

    for (FigureType figureType : FigureType.values()) {
      if (figureType.name().equals(figure)) {
        return figureType;
      }
    }
    throw new InvalidUserInputException("The figure type '" + figure + "' is not valid.");
  }
}
