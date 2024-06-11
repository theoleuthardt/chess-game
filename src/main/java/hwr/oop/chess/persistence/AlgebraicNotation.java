package hwr.oop.chess.persistence;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.MoveType;
import hwr.oop.chess.application.figures.Figure;
import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.application.figures.FigureType;
import hwr.oop.chess.application.figures.Pawn;

import java.util.function.Predicate;

public class AlgebraicNotation {
  private final StringBuilder notationString = new StringBuilder();
  private Board board;
  private Figure movingFigure;
  private Cell startCell;
  private Cell endCell;

  public void recordAction(Board board, Cell startCell, Cell endCell) {
    notationString.setLength(0);
    this.board = board;
    this.movingFigure = startCell.figure();
    this.startCell = startCell;
    this.endCell = endCell;

    switch (board.moveType(startCell, endCell)) {
      case MoveType.KING_CASTLING -> notationString.append("O-O");
      case MoveType.QUEEN_CASTLING -> notationString.append("O-O-O");
      default -> {
        addFigureSymbol();
        addDisambiguation();
        addCaptureModifier();
        addEndCell();
      }
    }
  }

  public void recordPawnPromotion(Board board, Cell startCell, FigureType toFigure) {
    this.board = board;
    this.startCell = this.endCell = startCell;
    Pawn pawn = (Pawn) startCell.figure();
    if (pawn.getPromotionTypes().contains(toFigure)) {
      addEndCell();
      notationString.append('=');
      notationString.append(Figure.fromTypeAndColor(toFigure, FigureColor.WHITE).symbol());
    }
  }

  private Predicate<Cell> mightBeAmbiguous() {
    Predicate<Cell> isNotStartCell = cell -> !cell.isEqualTo(startCell);
    Predicate<Cell> figureSymbolIsEqual =
        cell -> cell.isOccupied() && cell.figure().symbol() == movingFigure.symbol();
    Predicate<Cell> figureCanMoveToEndCell =
        cell -> board.availableCellsWithoutCheckMoves(cell).contains(endCell);
    return isNotStartCell.and(figureSymbolIsEqual).and(figureCanMoveToEndCell);
  }

  private void addDisambiguation() {
    String disambiguation = startCell.toCoordinates().toLowerCase();
    Predicate<Cell> isOnSameFile = cell -> cell.x() == startCell.x();
    Predicate<Cell> isOnSameRank = cell -> cell.y() == startCell.y();
    boolean isFileAmbiguous = board.cellExistsWhere(mightBeAmbiguous().and(isOnSameFile));
    boolean isRankAmbiguous = board.cellExistsWhere(mightBeAmbiguous().and(isOnSameRank));
    boolean isPawnCapture = movingFigure.type() == FigureType.PAWN && endCell.isOccupied();

    if (isFileAmbiguous && isRankAmbiguous) {
      notationString.append(disambiguation);
    } else if (isRankAmbiguous || isPawnCapture) {
      notationString.append(disambiguation.charAt(0));
    } else if (isFileAmbiguous) {
      notationString.append(disambiguation.charAt(1));
    }
  }

  private void addCaptureModifier() {
    if (endCell.isOccupied()) {
      notationString.append('x');
    }
  }

  public void actionFinished() {
    addCheckOrCheckmateModifier();
  }

  private void addFigureSymbol() {
    if (movingFigure.type() != FigureType.PAWN) {
      notationString.append(Character.toUpperCase(movingFigure.symbol()));
    }
  }

  private void addEndCell() {
    notationString.append(endCell.toCoordinates().toLowerCase());
  }

  private void addCheckOrCheckmateModifier() {
    FigureColor opponent = movingFigure.color().ofOpponent();
    if (board.isCheckmate(opponent)) {
      notationString.append("#");
    } else if (board.isCheck(opponent)) {
      notationString.append("+");
    }
  }

  @Override
  public String toString() {
    return notationString.toString();
  }
}
