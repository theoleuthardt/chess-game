package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.Figure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
  private Board board;

  @BeforeEach
  public void setUp() {
    // Initialize the board
    board = new Board(true);
  }

  @Test
  public void testPositionConnections() {
    // Check cell connections
    Cell currentCell = board.firstCell();
    while (currentCell != null) {
      Cell rowStartCell = currentCell; // Starting cell of the current row

      while (currentCell != null) {
        //                System.out.print(currentCell.getRow() + "" + currentCell.getCol()
        // + ", ");
        // Check right position
        if (currentCell.rightCell() != null) {
          assertEquals(
              currentCell,
              currentCell.rightCell().leftCell(),
              "Right cell is not correctly connected");
        }
        // Check bottom position
        if (currentCell.bottomCell() != null) {
          assertEquals(
              currentCell,
              currentCell.bottomCell().topCell(),
              "Bottom cell is not correctly connected");
        }
        // Check bottom right position
        if (currentCell.bottomRightCell() != null) {
          assertEquals(
              currentCell,
              currentCell.bottomRightCell().topLeftCell(),
              "Bottom right cell is not correctly connected");
        }
        // Check bottom left position
        if (currentCell.bottomLeftCell() != null) {
          assertEquals(
              currentCell,
              currentCell.bottomLeftCell().topRightCell(),
              "Bottom left cell is not correctly connected");
        }

        // Move to the next position in the current row
        currentCell = currentCell.rightCell();
      }
      //            System.out.println();

      // Move to the next row
      currentCell = rowStartCell.topCell();
    }

    // Print message if all tests pass in yellow color
    System.out.println("\u001B[33mPositionConnections: Cells are correctly connected.\u001B[0m");
  }

  @Test
  public void testFigurePlacement() {
    // Check figure placement on each position
    Cell currentCell = Board.firstCell();
    Cell rowStartPosiCell = Board.firstCell(); // Starting position of the current row

    while (currentCell != null) {
      Figure figure = currentCell.getFigure();
      if (currentCell.y() == 1
          || currentCell.y() == 2
          || currentCell.y() == 7
          || currentCell.y() == 8) {
        assertNotNull(
            figure,
            "Figure is not placed on cell (" + currentCell.y() + ", " + currentCell.x() + ")");
      } else {
        assertNull(
            figure,
            "Figure should not be placed on cell ("
                + currentCell.y()
                + ", "
                + currentCell.x()
                + ")");
      }
      // Move to the next cell
      if (currentCell.x() < 8) {
        // Move to the next position in the current row
        currentCell = currentCell.rightCell();
      } else {
        // Move to the next row
        rowStartPosiCell = rowStartPosiCell.bottomCell();
        currentCell = rowStartPosiCell;
      }
    }
  }

  // @Test
  public void testMoveFigure() {
    // this.moveFigureAndCheck(1, 1, 1, 5);
    // this.moveFigureAndCheck(1, 8, 1, 5);
    // this.moveFigureAndCheck(8, 8, 5, 5);
    // this.moveFigureAndCheck(8, 1, 3, 7);
    // this.moveFigureAndCheck(5, 5, 6, 1);
  }

  private void moveFigureAndCheck(int startX, int startY, int endX, int endY) {
    Figure prevFigure = board.cell(startX, startY).getFigure();
    //        Figure nextFigure = board.findCell(endX, endY).getFigure();
    //        if (prevFigure != null && nextFigure != null) {
    //            FigureType prevFigureType = prevFigure.getType();
    board.moveFigure(startX, startY, endX, endY);
    //            FigureType nextFigureType = nextFigure.getType();
    //            assertEquals(prevFigureType, nextFigureType, "Figure moves incorrect");
    //        }
    board.printBoard();
  }

  @Test
  public void testMoveFigureInvalidCoordinates() {
    // TODO fix
    Board board = new Board(false);

    int startX = 0;
    int startY = 0;

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          board.moveFigure(0, 1, 8, 8);
        });

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          board.moveFigure(4, 1, 9, 9);
        });
  }
}
