package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;
import hwr.oop.chess.application.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class QueenTest {
  Board board;

  @BeforeEach
  void setUp() {
    // Initialize the board
    board = new Board(false);
  }

  @Test
  void queen_hasTypeQueen() {
    Queen queen = new Queen(FigureColor.WHITE);
    assertThat(queen.type()).isEqualTo(FigureType.QUEEN);
  }

  @Test
  void moveQueen_diagonalIsValid() {
    Queen queen = new Queen(FigureColor.WHITE);
    Cell from = board.findCell(Coordinate.FOUR, Coordinate.FOUR);
    from.setFigure(queen);

    for (int distance : List.of(1, 2, 3)) {
      assertThat(queen.canMoveTo(from, from.findCellInDirection(distance, CellDirection.TOP_LEFT)))
          .isTrue();
      assertThat(queen.canMoveTo(from, from.findCellInDirection(distance, CellDirection.TOP_RIGHT)))
          .isTrue();
      assertThat(
              queen.canMoveTo(from, from.findCellInDirection(distance, CellDirection.BOTTOM_LEFT)))
          .isTrue();
      assertThat(
              queen.canMoveTo(from, from.findCellInDirection(distance, CellDirection.BOTTOM_RIGHT)))
          .isTrue();
    }
  }

  @Test
  void moveQueen_straightIsValid() {
    Queen queen = new Queen(FigureColor.BLACK);
    Cell from = board.findCell(Coordinate.FOUR, Coordinate.FOUR);
    from.setFigure(queen);

    for (int distance : List.of(1, 2, 3)) {
      assertThat(queen.canMoveTo(from, from.findCellInDirection(distance, CellDirection.TOP)))
          .isTrue();
      assertThat(queen.canMoveTo(from, from.findCellInDirection(distance, CellDirection.LEFT)))
          .isTrue();
      assertThat(queen.canMoveTo(from, from.findCellInDirection(distance, CellDirection.RIGHT)))
          .isTrue();
      assertThat(queen.canMoveTo(from, from.findCellInDirection(distance, CellDirection.BOTTOM)))
          .isTrue();
    }
  }

  @Test
  void moveQueen_cannotStayOnItsOwnField() {
    Queen queen = new Queen(FigureColor.BLACK);
    Cell from = board.findCell(Coordinate.FOUR, Coordinate.FOUR);
    from.setFigure(queen);

    assertThat(queen.canMoveTo(from, from)).isFalse();
  }

  @Test
  void moveQueen_cannotMoveInACurve() {
    Queen queen = new Queen(FigureColor.BLACK);
    Cell from = board.findCell(Coordinate.FOUR, Coordinate.FOUR);
    from.setFigure(queen);

    assertThat(queen.canMoveTo(from, from.topCell().topLeftCell())).isFalse();
    assertThat(queen.canMoveTo(from, from.topCell().topRightCell())).isFalse();
    assertThat(queen.canMoveTo(from, from.topCell().topRightCell().topRightCell())).isFalse();
  }
}
