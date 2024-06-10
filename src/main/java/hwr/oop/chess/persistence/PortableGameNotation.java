package hwr.oop.chess.persistence;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.MoveType;
import hwr.oop.chess.application.figures.FigureColor;

public class PortableGameNotation {
  private final Board board;

  private PortableGameNotation(Board board) {
    this.board = board;
  }

  public String generatePGN(Board board, Cell startCell, Cell endCell, MoveType moveType) {
    String pgnString =
        switch (moveType) {
          case MoveType.KING_CASTLING -> "O-O";
          case MoveType.QUEEN_CASTLING -> "O-O-O";
          default -> "";
        };

    char charFigure = startCell.figure().symbol();
    String endPosition = endCell.toCoordinates().toLowerCase();

    if (charFigure != 'p' && charFigure != 'P') {
      pgnString = "" + charFigure;
    }
    if (startCell.figure().color() != endCell.figure().color()) {
      if (charFigure == 'p' || charFigure == 'P') {
        pgnString += startCell.x().toInt();
      }
      pgnString += 'x';
    }
    pgnString += endPosition;

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
