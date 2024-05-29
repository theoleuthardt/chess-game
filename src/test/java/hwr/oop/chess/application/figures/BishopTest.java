package hwr.oop.chess.application.figures;

import static org.assertj.core.api.Assertions.assertThat;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import java.util.List;

import hwr.oop.chess.application.CellDirection;
import hwr.oop.chess.application.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BishopTest {
  Board board;

  @BeforeEach
  void setUp() {
    // Initialize the board
    board = new Board(false);
  }

  @Test
  void bishop_hasTypeBishop() {
    Bishop bishop = new Bishop(FigureColor.WHITE);
    assertThat(bishop.type()).isEqualTo(FigureType.BISHOP);
  }

  @Test
  void moveBishop_diagonalIsValid() {
    Bishop bishop = new Bishop(FigureColor.WHITE);
    Cell from = board.findCell(Coordinate.FOUR, Coordinate.FOUR);
    from.setFigure(bishop);

    for (int distance : List.of(1, 2, 3)) {
      assertThat(bishop.canMoveTo(from, from.findCellInDirection(distance, CellDirection.TOP_LEFT)))
          .isTrue();
      assertThat(
              bishop.canMoveTo(from, from.findCellInDirection(distance, CellDirection.TOP_RIGHT)))
          .isTrue();
      assertThat(
              bishop.canMoveTo(from, from.findCellInDirection(distance, CellDirection.BOTTOM_LEFT)))
          .isTrue();
      assertThat(
              bishop.canMoveTo(
                  from, from.findCellInDirection(distance, CellDirection.BOTTOM_RIGHT)))
          .isTrue();
    }
  }

  @Test
  void moveBishop_straightIsInvalid() {
    Bishop bishop = new Bishop(FigureColor.BLACK);
    Cell from = board.findCell(Coordinate.FOUR, Coordinate.FOUR);
    from.setFigure(bishop);

    for (int distance : List.of(1, 2, 3)) {
      assertThat(bishop.canMoveTo(from, from.findCellInDirection(distance, CellDirection.TOP)))
          .isFalse();
      assertThat(bishop.canMoveTo(from, from.findCellInDirection(distance, CellDirection.LEFT)))
          .isFalse();
      assertThat(bishop.canMoveTo(from, from.findCellInDirection(distance, CellDirection.RIGHT)))
          .isFalse();
      assertThat(bishop.canMoveTo(from, from.findCellInDirection(distance, CellDirection.BOTTOM)))
          .isFalse();
    }
  }

  @Test
  void moveBishop_cannotStayOnItsOwnField() {
    Bishop bishop = new Bishop(FigureColor.BLACK);
    Cell from = board.findCell(Coordinate.FOUR, Coordinate.FOUR);
    from.setFigure(bishop);

    assertThat(bishop.canMoveTo(from, from)).isFalse();
  }

  @Test
  void moveBishop_cannotMoveInACurve() {
    Bishop bishop = new Bishop(FigureColor.BLACK);
    Cell from = board.findCell(Coordinate.FOUR, Coordinate.FOUR);
    from.setFigure(bishop);

    assertThat(bishop.canMoveTo(from, from.topCell().topRightCell())).isFalse();
    assertThat(bishop.canMoveTo(from, from.topCell().topLeftCell())).isFalse();
    assertThat(bishop.canMoveTo(from, from.topCell().topLeftCell().topLeftCell())).isFalse();
  }
}
