package hwr.oop.chess.persistence;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.MoveType;
import hwr.oop.chess.application.figures.FigureColor;

public class PortableGameNotation {
  private PortableGameNotation(){}

  public static String generatePGN(Board board, Cell startCell, Cell endCell, MoveType moveType) {
    String pgnString =
        switch (moveType) {
          case MoveType.KING_CASTLING -> "O-O";
          case MoveType.QUEEN_CASTLING -> "O-O-O";
          default -> {
            char charFigure = startCell.figure().symbol();
            String endPosition = endCell.toCoordinates().toLowerCase();
            StringBuilder newPgn = new StringBuilder();
            if (charFigure != 'p' && charFigure != 'P') {
              newPgn.append(charFigure);
            }
            if (startCell.figure().color() != endCell.figure().color()) {
              if (charFigure == 'p' || charFigure == 'P') {
                newPgn.append(startCell.x().toInt());
              }
              newPgn.append('x');
            }
            newPgn.append(endPosition);
            yield newPgn.toString();
          }
        };

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
