package hwr.oop.chess.persistence;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.MoveType;
import hwr.oop.chess.application.figures.Figure;
import hwr.oop.chess.application.figures.FigureColor;
import java.util.Optional;

public class PortableGameNotation {
  private PortableGameNotation(){}

  public static String generatePgn(Board board, Cell startCell, Cell endCell, MoveType moveType) {
    String pgnString =
        switch (moveType) {
          case MoveType.KING_CASTLING -> "O-O";
          case MoveType.QUEEN_CASTLING -> "O-O-O";
          default -> {
            char charFigure = startCell.figure().symbol();
            String endPosition = endCell.toCoordinates().toLowerCase();
            Optional<Figure> startFigure = Optional.ofNullable(startCell.figure());
            Optional<Figure> endFigure = Optional.ofNullable(endCell.figure());
            StringBuilder newPgn = new StringBuilder();
            if (charFigure != 'p' && charFigure != 'P') {
              newPgn.append(charFigure);
            }
            if (startFigure.isPresent() && endFigure.isPresent() && startFigure.get().color() != endFigure.get().color()) {
              if (charFigure == 'p' || charFigure == 'P') {
                newPgn.append((char) (startCell.x().toInt() + 96));
              }
              newPgn.append('x');
            }
            newPgn.append(endPosition);
            yield newPgn.toString();
          }
        };

    boolean white = board.isCheck(FigureColor.WHITE);
    boolean black = board.isCheck(FigureColor.BLACK);

    if (board.isCheck(board.turn() == FigureColor.WHITE ? FigureColor.BLACK : FigureColor.WHITE)) {
      pgnString += "+";
    }

    if (board.isCheckmate(
        board.turn() == FigureColor.WHITE ? FigureColor.BLACK : FigureColor.WHITE)) {
        pgnString = pgnString.substring(0, pgnString.length() - 1) + "#";
    }

    return pgnString;
  }
}
