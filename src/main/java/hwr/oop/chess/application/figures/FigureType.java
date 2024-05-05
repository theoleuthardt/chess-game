package hwr.oop.chess.application.figures;

import hwr.oop.chess.cli.InvalidUserInputException;

import java.util.Arrays;
import java.util.List;

public enum FigureType {
    KING, // König
    QUEEN, // Dame
    ROOK, // Turm
    BISHOP, // Läufer
    KNIGHT, // Pferd
    PAWN; // Bauer


    public static List<String> allFigureTypes() {
        return Arrays.stream(FigureType.values()).map(Enum::name).toList();
    }

    public static FigureType fromString(String figure) {
        figure = figure.toUpperCase();

        List<String> allTypes = allFigureTypes();
        for (String figureType : allTypes) {
            if (figureType.equals(figure)) {
                return FigureType.valueOf(figureType);
            }
        }
        throw new InvalidUserInputException("The figure type '" + figure + "' is not valid.");
    }
}
