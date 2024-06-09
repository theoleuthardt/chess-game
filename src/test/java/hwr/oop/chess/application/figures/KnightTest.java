package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;

class KnightTest {
  Board board;

  @BeforeEach
  void setUp() {
    board = new Board(true);
  }

  @Test
  void createKnight() {
    Knight knight = new Knight(FigureColor.BLACK);
    assertThat(knight.color()).isEqualTo(FigureColor.BLACK);
    assertThat(knight.type()).isEqualTo(FigureType.KNIGHT);
  }

  @ParameterizedTest
  @ValueSource(chars = {'a', 'c'})
  void moveWhiteKnight_isAllowed(char args) {
    Cell from = board.findCell('b', 1);
    Cell to = board.findCell(args, 3);

    Figure knight = from.figure();
    assertThat(knight.type()).isEqualTo(FigureType.KNIGHT);
    assertThat(knight.canMoveTo(from, to)).isTrue();
  }

  @ParameterizedTest
  @ValueSource(ints = {2, 3})
  void moveWhiteKnight_cannotMoveForward(int args) {
    Cell from = board.findCell('b', 1);
    Cell to = board.findCell('b', args);

    Figure knight = from.figure();
    assertThat(knight.type()).isEqualTo(FigureType.KNIGHT);
    assertThat(knight.canMoveTo(from, to)).isFalse();
  }

  @ParameterizedTest
  @ValueSource(chars = {'a', 'c'})
  void moveBlackKnight_isAllowed(char args) {
    Cell from = board.findCell('b', 8);
    Cell to = board.findCell(args, 6);

    Figure knight = from.figure();
    assertThat(knight.type()).isEqualTo(FigureType.KNIGHT);
    assertThat(knight.canMoveTo(from, to)).isTrue();
  }

  @ParameterizedTest
  @ValueSource(ints = {6, 7})
  void moveBlackKnight_cannotMoveForward(int args) {
    Cell from = board.findCell('b', 8);
    Cell to = board.findCell('b', args);

    Figure knight = from.figure();
    assertThat(knight.type()).isEqualTo(FigureType.KNIGHT);
    assertThat(knight.canMoveTo(from, to)).isFalse();
  }

  @Test
  void moveWhiteKnight_cannotMoveIntoVoid1() {
    Cell from = board.findCell('b', 1);
    Cell to = board.findCell('a', 3);

    Figure knight = from.figure();
    assertThat(knight.type()).isEqualTo(FigureType.KNIGHT);
    assertThat(knight.canMoveTo(from, to)).isTrue();
  }

  @Test
  void moveWhiteKnight_cannotMoveIntoVoid2() {
    Cell from = board.findCell('b', 1);
    Cell to = board.findCell('a', 3);

    Figure knight = from.figure();
    board.moveFigure(from, to);
    assertThat(knight.canMoveTo(to, from)).isTrue();
  }

  @Test
  void moveWhiteKnight_ifOccupied() {
    Cell from = board.findCell('b', 1);
    Cell to = board.findCell('d', 2);

    Figure knight = from.figure();
    assertThat(knight.type()).isEqualTo(FigureType.KNIGHT);
    assertThat(knight.canMoveTo(from, to)).isFalse();
  }
}
