package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class QueenTest {
  Board board;

  @BeforeEach
  void setUp() {
    // Initialize the board
    board = new Board(false);
  }

  @Test
  void moveQueen_diagonalIsValid() {
    Queen queen = new Queen(FigureColor.WHITE);
    Cell from = board.findCell(4, 4);
    from.setFigure(queen);

    for(int distance : List.of(1,2,3)) {
      assertThat(queen.canMoveTo(from, board.findCell(4 - distance, 4 - distance))).isTrue();
      assertThat(queen.canMoveTo(from, board.findCell(4 + distance, 4 - distance))).isTrue();
      assertThat(queen.canMoveTo(from, board.findCell(4 - distance, 4 + distance))).isTrue();
      assertThat(queen.canMoveTo(from, board.findCell(4 + distance, 4 + distance))).isTrue();
    }
  }


  @Test
  void moveQueen_straightIsValid() {
    Queen queen = new Queen(FigureColor.BLACK);
    Cell from = board.findCell(4, 4);
    from.setFigure(queen);

    for(int distance : List.of(1,2,3)) {
      assertThat(queen.canMoveTo(from, board.findCell(4 - distance, 4))).isTrue();
      assertThat(queen.canMoveTo(from, board.findCell(4 + distance, 4))).isTrue();
      assertThat(queen.canMoveTo(from, board.findCell(4, 4 - distance))).isTrue();
      assertThat(queen.canMoveTo(from, board.findCell(4, 4 + distance))).isTrue();
    }
  }

  @Test
  void moveQueen_cannotStayOnItsOwnField() {
    Queen queen = new Queen(FigureColor.BLACK);
    Cell from = board.findCell(4, 4);
    from.setFigure(queen);

    assertThat(queen.canMoveTo(from, from)).isFalse();
  }

  @Test
  void moveQueen_cannotMoveInACurve() {
    Queen queen = new Queen(FigureColor.BLACK);
    int x = 4;
    int y = 4;
    Cell from = board.findCell(x, y);
    from.setFigure(queen);

    assertThat(queen.canMoveTo(from, new Cell(x + 1, y + 2))).isFalse();
    assertThat(queen.canMoveTo(from, new Cell(x - 1, y + 2))).isFalse();
    assertThat(queen.canMoveTo(from, new Cell(x - 2, y + 3))).isFalse();
  }
}
