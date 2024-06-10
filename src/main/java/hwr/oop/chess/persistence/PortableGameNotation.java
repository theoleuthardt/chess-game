package hwr.oop.chess.persistence;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.figures.FigureColor;

public class PortableGameNotation {
  private final Board board;

  public String generatePGN(Cell startCell, Cell endCell) {
    char charFigure = startCell.figure().symbol();
    String endPosition = endCell.toCoordinates().toLowerCase();
    String pgnString = "";
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
    return pgnString;
  }
}
