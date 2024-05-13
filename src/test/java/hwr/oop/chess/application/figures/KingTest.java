package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

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
}
