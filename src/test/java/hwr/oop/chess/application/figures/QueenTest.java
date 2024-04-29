package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static hwr.oop.chess.application.Board.isValidCoordinate;
import static org.junit.jupiter.api.Assertions.*;

public class QueenTest {
  Board board;

  @BeforeEach
  void setUp() {
    // Initialize the board
    board = new Board(false);
  }

  @Test
  void testQueenDirections() {
    Queen queen = new Queen(FigureColor.BLACK);
    assertEquals(8, queen.directions().size());
  }

  // @Test
  void testMoveQueen() {
    int x = 4;
    int y = 1;
    Cell originalCell = board.cell(x,y);
    Cell movedCell = null;

    originalCell.setFigure(new Queen(FigureColor.WHITE));
    Figure whiteQueen = originalCell.getFigure();

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
        assertEquals(whiteQueen, movedCell.getFigure());
//        if(isValidCoordinate(x,y)){ // TODO write this codes also below // TEST error
//          movedCell = board.cell(x, y);
//          assertNotNull(movedCell);
//          assertEquals(whiteQueen, movedCell.getFigure());
//          assertTrue(whiteQueen.canMoveTo(originalCell, movedCell));
//        }

        board.moveFigureDiagonal(board, CellDirection.TOP_RIGHT, x, y, randomDiff);
        x += randomDiff;
        y += randomDiff;
        movedCell = board.cell(x, y);
        assertNotNull(movedCell);
        assertEquals(whiteQueen, movedCell.getFigure());

        board.moveFigureDiagonal(board, CellDirection.BOTTOM_LEFT, x, y, randomDiff);
        x -= randomDiff;
        y -= randomDiff;
        movedCell = board.cell(x, y);
        assertNotNull(movedCell);
        assertEquals(whiteQueen, movedCell.getFigure());

        board.moveFigureDiagonal(board, CellDirection.BOTTOM_RIGHT, x, y, randomDiff);
        x += randomDiff;
        y -= randomDiff;
        movedCell = board.cell(x, y);
        assertNotNull(movedCell);
        assertEquals(whiteQueen, movedCell.getFigure());
      } catch (IllegalArgumentException e) {
        System.out.println("IllegalArgumentException occurred: " + e.getMessage());
      }

      testCount++;
    }
  }
}
