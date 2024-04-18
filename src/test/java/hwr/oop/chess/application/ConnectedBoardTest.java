package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.Figure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectedBoardTest {
    private ConnectedBoard board;

    @BeforeEach
    public void setUp() {
        // Initialize the board
        board = new ConnectedBoard();
    }

    @Test
    public void testCellConnections() {
        // Check cell connections
        Cell currentCell = board.getStartingCell();
        while (currentCell != null) {
            Cell rowStartCell = currentCell; // Starting cell of the current row

            while (currentCell != null) {
//                System.out.print(currentCell.getRow() + "" + currentCell.getCol() + ", ");
                // Check right cell
                if (currentCell.getRightCell() != null) {
                    assertEquals(currentCell, currentCell.getRightCell().getLeftCell(), "Right cell is not correctly connected");
                }
                // Check bottom cell
                if (currentCell.getBottomCell() != null) {
                    assertEquals(currentCell, currentCell.getBottomCell().getTopCell(), "Bottom cell is not correctly connected");
                }
                // Check bottom right cell
                if (currentCell.getBottomRightCell() != null) {
                    assertEquals(currentCell, currentCell.getBottomRightCell().getTopLeftCell(), "Bottom right cell is not correctly connected");
                }
                // Check bottom left cell
                if (currentCell.getBottomLeftCell() != null) {
                    assertEquals(currentCell, currentCell.getBottomLeftCell().getTopRightCell(), "Bottom left cell is not correctly connected");
                }

                // Move to the next cell in the current row
                currentCell = currentCell.getRightCell();
            }
//            System.out.println();

            // Move to the next row
            currentCell = rowStartCell.getTopCell();
        }

        // Print message if all tests pass in yellow color
        System.out.println("\u001B[33mCellConnections: Cells are correctly connected.\u001B[0m");
    }

    @Test
    public void testFigurePlacement() {
        // Check figure placement on each cell
        Cell currentCell = board.getStartingCell();
        while (currentCell != null) {
            Figure figure = currentCell.getFigure();
            if (currentCell.getRow() == 1 || currentCell.getRow() == 2 || currentCell.getRow() == 6 || currentCell.getRow() == 8) {
                assertNotNull(figure, "Figure is not placed on cell (" + currentCell.getRow() + ", " + currentCell.getCol() + ")");
            } else {
                assertNull(figure, "Figure should not be placed on cell (" + currentCell.getRow() + ", " + currentCell.getCol() + ")");
            }
            // Move to the next cell
            if (currentCell.getRightCell() != null) {
                currentCell = currentCell.getRightCell();
            } else {
                currentCell = currentCell.getBottomCell();
            }
        }
        board.printBoard();
    }
}
