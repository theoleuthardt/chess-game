package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;
import hwr.oop.chess.persistence.FenNotation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class KingTest {
  Board board;

  @BeforeEach
  void setUp() {
    board = new Board(true);
  }

  @Test
  void createKing() {
    King king = new King(FigureColor.WHITE);
    assertThat(king.color()).isEqualTo(FigureColor.WHITE);
    assertThat(king.type()).isEqualTo(FigureType.KING);
  }

  void putKingOnDefaultCell() {
    board.findCell('e', 1).setFigure(new King(FigureColor.BLACK));
    board.findCell('e', 8).setFigure(new King(FigureColor.WHITE));
  }

  @Test
  void king_hasCorrectSymbol() {
    King whiteKing = new King(FigureColor.WHITE);
    assertThat(whiteKing.symbol()).isEqualTo('K');

    King blackKing = new King(FigureColor.BLACK);
    assertThat(blackKing.symbol()).isEqualTo('k');
  }

  @ParameterizedTest
  @EnumSource(FigureColor.class)
  void king_isCorrectColor(FigureColor color) {
    King blackKing = new King(color);
    assertThat(blackKing.color()).isEqualTo(color);
  }

  @Test
  void moveKing() {
    putKingOnDefaultCell();
    Cell cellE1 = board.findCell('e', 1);
    Cell cellE2 = board.findCell('e', 2);
    Figure king = cellE1.figure();
    assertThat(king.canMoveTo(cellE1, cellE2)).isTrue();
  }

  @ParameterizedTest
  @ValueSource(ints = {2, 3, 4})
  void moveKing_cannotMoveMoreThanOneUp(int args) {
    Cell from = board.findCell('e', 1);
    Cell to = board.findCell('e', args);

    Figure king = from.figure();
    assertThat(king.type()).isEqualTo(FigureType.KING);
    assertThat(king.canMoveTo(from, to)).isFalse();
  }

  @Test
  void testCastlingKingWhite() {
    board = new Board(false);
    String fenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQK2R w KQkq - 0 10";
    FenNotation.parseFEN(board, fenString);
    Cell king = board.findCell(5, 1);
    // Expected only available castling King
    assertThat(king.isFreeInDirection(2, CellDirection.RIGHT)).isTrue();
    assertThat(king.isFreeInDirection(3, CellDirection.RIGHT)).isFalse();
    assertThat(king.isFreeInDirection(2, CellDirection.LEFT)).isFalse();
    assertThat(king.isFreeInDirection(3, CellDirection.LEFT)).isFalse();

    List<Cell> cells = board.availableCellsWithoutCheckMoves(king);
    assertThat(cells).contains(board.findCell(7, 1));

    // Move king
    board.moveFigure("e1", "g1");
    assertThat(board.findCell(7, 1).figure().symbol()).isEqualTo('K');
    assertThat(board.findCell(6, 1).figure().symbol()).isEqualTo('R');
    assertThat(board.findCell(8, 1).figure()).isNull();
    assertThat(board.findCell(5, 1).figure()).isNull();

    // Check board status
    assertThat(board.canPerformKingSideCastling(FigureColor.WHITE)).isFalse();
    assertThat(((Rook) board.findCell(6, 1).figure()).hasMoved()).isTrue();
  }

  @Test
  void testCastlingQueenWhite() {
    board = new Board(false);
    String fenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R3KBNR w KQkq - 0 10";
    FenNotation.parseFEN(board, fenString);
    Cell king = board.findCell(5, 1);
    // Expected only available castling King
    assertThat(king.isFreeInDirection(2, CellDirection.RIGHT)).isFalse();
    assertThat(king.isFreeInDirection(3, CellDirection.RIGHT)).isFalse();
    assertThat(king.isFreeInDirection(4, CellDirection.LEFT)).isFalse();
    assertThat(king.isFreeInDirection(3, CellDirection.LEFT)).isTrue();

    List<Cell> cells = board.availableCellsWithoutCheckMoves(king);
    assertThat(cells).contains(board.findCell(3, 1));

    // Move king
    board.moveFigure("e1", "c1");
    assertThat(board.findCell(3, 1).figure().symbol()).isEqualTo('K');
    assertThat(board.findCell(4, 1).figure().symbol()).isEqualTo('R');
    assertThat(board.findCell(1, 1).figure()).isNull();
    assertThat(board.findCell(5, 1).figure()).isNull();

    // Check board status
    assertThat(board.canPerformKingSideCastling(FigureColor.WHITE)).isFalse();
    assertThat(((Rook) board.findCell(4, 1).figure()).hasMoved()).isTrue();
  }
}
