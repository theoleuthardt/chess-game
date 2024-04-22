package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.Figure;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
  private Board board;

  @BeforeEach
   void setUp() {
    // Initialize the board
    board = new Board();
  }

  @Test
   void testBoardCells() {
    // Check cell connections
    Cell currentCell = board.firstCell();
    while (currentCell != null) {
      Cell rowStartCell = currentCell; // Starting cell of the current row

      while (currentCell != null) {
        //                System.out.print(currentCell.getRow() + "" + currentCell.getCol()
        // + ", ");
        // Check right cell
        if (currentCell.rightCell() != null) {
          assertEquals(
              currentCell,
              currentCell.rightCell().leftCell(),
              "Right cell is not correctly connected");
        }
        // Check bottom cell
        if (currentCell.bottomCell() != null) {
          assertEquals(
              currentCell,
              currentCell.bottomCell().topCell(),
              "Bottom cell is not correctly connected");
        }
        // Check bottom right cell
        if (currentCell.bottomRightCell() != null) {
          assertEquals(
              currentCell,
              currentCell.bottomRightCell().topLeftCell(),
              "Bottom right cell is not correctly connected");
        }
        // Check bottom left cell
        if (currentCell.bottomLeftCell() != null) {
          assertEquals(
              currentCell,
              currentCell.bottomLeftCell().topRightCell(),
              "Bottom left cell is not correctly connected");
        }

        // Move to the next cell in the current row
        currentCell = currentCell.rightCell();
      }
      //            System.out.println();

      // Move to the next row
      currentCell = rowStartCell.topCell();
    }

    // Print message if all tests pass in yellow color
    System.out.println("\u001B[33mCellConnections: Cells are correctly connected.\u001B[0m");
  }

  @Test
  void testCellConnections(){
    ArrayList<Cell> cells = new ArrayList<>();      String message = "Cells are not correctly connected";

    // Check right
    cells = generateVerticalCells(1, 1);
    board.connectCells(cells.get(0), cells.get(1));
    Assertions.assertEquals(cells.get(0).rightCell(), cells.get(2), message);

    cells = generateVerticalCells(1, 1);
    board.connectCells(cells.get(0), cells.get(1));
    Assertions.assertEquals(cells.get(0).rightCell(), cells.get(2), message);

    // Check left
    cells = generateVerticalCells(-1, 1);
    board.connectCells(cells.get(0), cells.get(1));
    Assertions.assertEquals(cells.get(0).leftCell(), cells.get(2), message);

  }
  @Test
   void testFigurePlacement() {
    // Check figure placement on each cell
    Cell currentCell = Board.firstCell();
    Cell rowStartPosiCell = Board.firstCell(); // Starting cell of the current row

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
        // Move to the next cell in the current row
        currentCell = currentCell.rightCell();
      } else {
        // Move to the next row
        rowStartPosiCell = rowStartPosiCell.bottomCell();
        currentCell = rowStartPosiCell;
      }
    }
  }

  @Test
   void testMoveFigure() {
    // Check move
    this.moveFigureAndCheck(1, 1, 1, 5);
    this.moveFigureAndCheck(1, 8, 1, 5);
    //        this.moveFigureAndCheck(8, 8, 5, 5);
    //        this.moveFigureAndCheck(8, 1, 3, 7);
    //        this.moveFigureAndCheck(5, 5, 6, 1);

  }

  private void moveFigureAndCheck(int startX, int startY, int endX, int endY) {
    Figure prevFigure = board.findCell(startX, startY).getFigure();
    //        Figure nextFigure = board.findCell(endX, endY).getFigure();
    //        if (prevFigure != null && nextFigure != null) {
    //            FigureType prevFigureType = prevFigure.getType();
    board.moveFigure(startX, startY, endX, endY);
    //            FigureType nextFigureType = nextFigure.getType();
    //            assertEquals(prevFigureType, nextFigureType, "Figure moves incorrect");
    //        }
    board.printBoard();
  }

  ArrayList<Cell> generateVerticalCells(int xOperator, int yOperator){
    ArrayList<Cell> list = new ArrayList<>();
    Cell cellFirst = null;
    Cell cellSecond = null;
    int randomNumX;
    int randomNumY;
    Random rand = new Random();
    switch(xOperator){
      case 1:
        randomNumX = rand.nextInt(7) + 1;
        randomNumY = rand.nextInt(7) + 1;
        cellFirst = new Cell(randomNumX, randomNumY );
        cellSecond = new Cell(randomNumX +1, yOperator > 0 ? randomNumY + 1 :randomNumY - 1  );
        break;
      case 0:
        break;
      case -1:
        randomNumX = rand.nextInt(7) + 2;
        randomNumY = rand.nextInt(7) + 2;
        cellFirst = new Cell(randomNumX, randomNumY );
        cellSecond = new Cell(randomNumX - 1, yOperator > 0 ? randomNumY + 1 :randomNumY - 1  );
        break;
    }

    list.add(cellFirst);
    list.add(cellSecond);
    return list;
  }
}
