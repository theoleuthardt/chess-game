package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.Figure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    private Board board;

    @BeforeEach
    public void setUp() {
        // Initialize the board
        board = new Board();
    }

    @Test
    public void testPositionConnections() {
        // Check position connections
        Cell currentPosition = board.getStartingPosition();
        while (currentPosition != null) {
            Cell rowStartPosition = currentPosition; // Starting position of the current row

            while (currentPosition != null) {
//                System.out.print(currentPosition.getRow() + "" + currentPosition.getCol() + ", ");
                // Check right position
                if (currentPosition.getRightPosition() != null) {
                    assertEquals(currentPosition, currentPosition.rightCell().leftCell(), "Right position is not correctly connected");
                }
                // Check bottom position
                if (currentPosition.bottomCell() != null) {
                    assertEquals(currentPosition, currentPosition.bottomCell().topCell(), "Bottom position is not correctly connected");
                }
                // Check bottom right position
                if (currentPosition.getBottomRightPosition() != null) {
                    assertEquals(currentPosition, currentPosition.bottomRightCell().topLeftCell(), "Bottom right position is not correctly connected");
                }
                // Check bottom left position
                if (currentPosition.getBottomLeftPosition() != null) {
                    assertEquals(currentPosition, currentPosition.bottomLeftCell().topRightCell(), "Bottom left position is not correctly connected");
                }

                // Move to the next position in the current row
                currentPosition = currentPosition.getRightPosition();
            }
//            System.out.println();

            // Move to the next row
            currentPosition = rowStartPosition.getTopPosition();
        }

        // Print message if all tests pass in yellow color
        System.out.println("\u001B[33mPositionConnections: Positions are correctly connected.\u001B[0m");
    }

    @Test
    public void testFigurePlacement() {
        // Check figure placement on each position
        Cell currentPosition = board.getStartingPosition();
        Cell rowStartPosition = board.getStartingPosition();  // Starting position of the current row

        while (currentPosition != null) {
            Figure figure = currentPosition.getFigure();
            if (currentPosition.y() == 1 || currentPosition.y() == 2 || currentPosition.y() == 7 || currentPosition.y() == 8) {
                assertNotNull(figure, "Figure is not placed on position (" + currentPosition.y() + ", " + currentPosition.x() + ")");
            } else {
                assertNull(figure, "Figure should not be placed on position (" + currentPosition.y() + ", " + currentPosition.x() + ")");
            }
            // Move to the next position
            if (currentPosition.x() < 8) {
                // Move to the next position in the current row
                currentPosition = currentPosition.getRightPosition();
            } else {
                // Move to the next row
                rowStartPosition = rowStartPosition.getBottomPosition();
                currentPosition = rowStartPosition;
                System.out.println();
            }
        }
    }

    @Test
    public void testMoveFigure() {
        // Check move
        this.moveFigureAndCheck(1, 1, 1, 5);
        this.moveFigureAndCheck(1, 8, 1, 5);
//        this.moveFigureAndCheck(8, 8, 5, 5);
//        this.moveFigureAndCheck(8, 1, 3, 7);
//        this.moveFigureAndCheck(5, 5, 6, 1);

    }

    private void moveFigureAndCheck(int startX, int startY, int endX, int endY) {
        Figure prevFigure = board.findPosition(startX, startY).getFigure();
//        Figure nextFigure = board.findPosition(endX, endY).getFigure();
//        if (prevFigure != null && nextFigure != null) {
//            FigureType prevFigureType = prevFigure.getType();
        board.moveFigure(startX, startY, endX, endY);
//            FigureType nextFigureType = nextFigure.getType();
//            assertEquals(prevFigureType, nextFigureType, "Figure moves incorrect");
//        }
        board.printBoard();

    }
}
