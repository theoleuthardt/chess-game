package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QueenTest {
  Board board;

  @BeforeEach
  void setUp() {
    // Initialize the board
    board = new Board(false);
  }

  // @Test
  void testMoveQueen() {
    int x = 4;
    int y = 1;
    Cell whiteQueen = board.cell(x, y);
    whiteQueen.setFigure(new Queen(FigureColor.WHITE));

    Cell movedCell = null;

    int testCount = 0;
    while (testCount < 10) {

      try {
        Random rand = new Random();

        // Generated random number between 1 and 7
        int randomDiff = rand.nextInt(6) + 1;
        // Test white Queen
        board.moveFigureDiagonal(board, CellDirection.TOP_LEFT, x, y, randomDiff);
        x -= randomDiff;
        y += randomDiff;
        movedCell = board.cell(x, y);
        assertNotNull(movedCell);
        assertEquals(whiteQueen.getFigure(), movedCell.getFigure());

        board.moveFigureDiagonal(board, CellDirection.TOP_RIGHT, x, y, randomDiff);
        x += randomDiff;
        y += randomDiff;
        movedCell = board.cell(x, y);
        assertNotNull(movedCell);
        assertEquals(whiteQueen.getFigure(), movedCell.getFigure());

        board.moveFigureDiagonal(board, CellDirection.BOTTOM_LEFT, x, y, randomDiff);
        x -= randomDiff;
        y -= randomDiff;
        movedCell = board.cell(x, y);
        assertNotNull(movedCell);
        assertEquals(whiteQueen.getFigure(), movedCell.getFigure());

        board.moveFigureDiagonal(board, CellDirection.BOTTOM_RIGHT, x, y, randomDiff);
        x += randomDiff;
        y -= randomDiff;
        movedCell = board.cell(x, y);
        assertNotNull(movedCell);
        assertEquals(whiteQueen.getFigure(), movedCell.getFigure());
      } catch (IllegalArgumentException e) {
        System.out.println("IllegalArgumentException occurred: " + e.getMessage());
      }

      testCount++;
    }
  }
}
