package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RookTest {
  Board board;

  @BeforeEach
  void before() {
    board = new Board(false);
  }

  void putRooksOnDefaultCells() {
    board.findCell('a', 1).setFigure(new Rook(FigureColor.WHITE));
    board.findCell('h', 1).setFigure(new Rook(FigureColor.WHITE));

    board.findCell('a', 8).setFigure(new Rook(FigureColor.BLACK));
    board.findCell('h', 8).setFigure(new Rook(FigureColor.BLACK));
  }

  @Test
  void rook_hasTypeRook() {
    Rook rook = new Rook(FigureColor.WHITE);
    assertThat(rook.type()).isEqualTo(FigureType.ROOK);
  }

  @Test
  void rook_hasCorrectSymbol() {
    Rook whiteRook = new Rook(FigureColor.WHITE);
    assertThat(whiteRook.symbol()).isEqualTo('R');

    Rook blackRook = new Rook(FigureColor.BLACK);
    assertThat(blackRook.symbol()).isEqualTo('r');
  }

  @ParameterizedTest
  @EnumSource(FigureColor.class)
  void rook_isCorrectColor(FigureColor color) {
    Rook whiteRook = new Rook(color);
    assertThat(whiteRook.color()).isEqualTo(color);
  }

  @Test
  void moveRook() {
    putRooksOnDefaultCells();
    Cell cellA1 = board.findCell('a', 1);
    Cell cellA6 = board.findCell('a', 6);
    Figure rook = cellA1.figure();
    Assertions.assertThat(rook.canMoveTo(cellA1, cellA6)).isTrue();
  }

  @Test
  void moveRook_cannotMoveDiagonal() {
    Rook rook = new Rook(FigureColor.WHITE);
    Cell from = board.findCell(4, 4);
    from.setFigure(rook);

    for (int distance : List.of(1, 2, 3)) {
      assertThat(rook.canMoveTo(from, board.findCell(4 - distance, 4 - distance))).isFalse();
      assertThat(rook.canMoveTo(from, board.findCell(4 + distance, 4 - distance))).isFalse();
      assertThat(rook.canMoveTo(from, board.findCell(4 - distance, 4 + distance))).isFalse();
      assertThat(rook.canMoveTo(from, board.findCell(4 + distance, 4 + distance))).isFalse();
    }
  }

  @Test
  void moveRook_straightIsValid() {
    Rook rook = new Rook(FigureColor.BLACK);
    Cell from = board.findCell(4, 4);
    from.setFigure(rook);

    for (int distance : List.of(1, 2, 3)) {
      assertThat(rook.canMoveTo(from, board.findCell(4 - distance, 4))).isTrue();
      assertThat(rook.canMoveTo(from, board.findCell(4 + distance, 4))).isTrue();
      assertThat(rook.canMoveTo(from, board.findCell(4, 4 - distance))).isTrue();
      assertThat(rook.canMoveTo(from, board.findCell(4, 4 + distance))).isTrue();
    }
  }

  @Test
  void moveRook_cannotStayOnItsOwnField() {
    Rook rook = new Rook(FigureColor.BLACK);
    Cell from = board.findCell(4, 4);
    from.setFigure(rook);

    assertThat(rook.canMoveTo(from, from)).isFalse();
  }

  @Test
  void moveRook_cannotMoveInACurve() {
    Rook rook = new Rook(FigureColor.BLACK);
    int x = 4;
    int y = 4;
    Cell from = board.findCell(x, y);
    from.setFigure(rook);

    assertThat(rook.canMoveTo(from, board.findCell(x + 1, y + 2))).isFalse();
    assertThat(rook.canMoveTo(from, board.findCell(x - 1, y + 2))).isFalse();
    assertThat(rook.canMoveTo(from, board.findCell(x - 2, y + 3))).isFalse();
  }
}
