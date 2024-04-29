package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RookTest {
  Board board;

  @BeforeEach
  void before() {
    board = new Board(false);

    board.findCell('a', 1).setFigure(new Rook(FigureColor.WHITE));
    board.findCell('h', 1).setFigure(new Rook(FigureColor.WHITE));

    board.findCell('a', 8).setFigure(new Rook(FigureColor.WHITE));
    board.findCell('h', 8).setFigure(new Rook(FigureColor.WHITE));
  }

  @Test
  void moveRook() {
    Cell cellA1 = board.findCell('a', 1);
    Cell cellA6 = board.findCell('a', 6);
    boolean canMove = cellA1.figure().canMoveTo(cellA1, cellA6);
    Assertions.assertThat(canMove).isTrue();
  }
}
