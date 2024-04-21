package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.Figure;
import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.application.figures.PawnFigure;
import hwr.oop.chess.application.figures.RookFigure;
import hwr.oop.chess.application.figures.QueenFigure;
import hwr.oop.chess.application.figures.KingFigure;
import hwr.oop.chess.application.figures.KnightFigure;
import hwr.oop.chess.application.figures.BishopFigure;

import java.util.Objects;

public class Board {
    private static Position startingPosition;

    public Board() {
        initializeBoard();
    }

    private void initializeBoard() {
        System.out.println("\u001B[32minitialize Board\u001B[0m");
        // Create the first position and store it in startingPosition
        startingPosition = new Position(1, 1);
        Position previousPosition = startingPosition;
        Position previousRowStart = startingPosition;
        Position previousRowPosition = startingPosition;

        // Create and connect positions for each row and column of the board
        for (int y = 1; y < 9; y++) {
            for (int x = 1; x < 9; x++) {
                // System.out.print(row + "" + col + ", ");
                // Skip the first position since it's already created
                if (y == 1 && x == 1) {
                    continue;
                }
                if (x == 1) {
                    previousRowPosition = previousRowStart;
                }
                // Create a new position
                Position currentPosition = new Position(x, y);
                // Connect the position to the previous position
                connectPositions(previousPosition, currentPosition);
                if (y != 1) {
                    connectPositions(currentPosition, previousRowPosition);
                    if (x != 1) {
                        connectPositions(currentPosition, previousRowPosition.getLeftPosition());
                    }
                    if (x != 8) {
                        connectPositions(currentPosition, previousRowPosition.getRightPosition());
                    } else {
                        previousRowStart = previousRowStart.getTopPosition();
                    }
                }

                // Set the next position as the current position
                previousPosition = currentPosition;
                previousRowPosition = previousRowPosition.getRightPosition();
            }
            // System.out.println("  :" + row + "Row");
        }

        // Set up the initial chess positions
        setUpInitialChessPositions();
    }

    // Method to connect each position
    private void connectPositions(Position currentPosition, Position nextPosition) {
        if (nextPosition == null) {
            return; // Skip if the next position is null
        }

        // Connect horizontally
        if (currentPosition.y() == nextPosition.y()) {
            if (currentPosition.x() < nextPosition.x()) {
                currentPosition.setRightPosition(nextPosition);
                nextPosition.setLeftPosition(currentPosition);
            } else if (currentPosition.x() > nextPosition.x()) {
                currentPosition.setLeftPosition(nextPosition);
                nextPosition.setRightPosition(currentPosition);
            }
        }

        // Connect vertically
        if (currentPosition.x() == nextPosition.x()) {
            // Connect vertically
            if (currentPosition.y() > nextPosition.y()) {
                currentPosition.setBottomPosition(nextPosition);
                nextPosition.setTopPosition(currentPosition);
            } else if (currentPosition.y() < nextPosition.y()) {
                currentPosition.setTopPosition(nextPosition);
                nextPosition.setBottomPosition(currentPosition);
            }
        }

        // Connect diagonally
        if (currentPosition.y() < nextPosition.y() && currentPosition.x() < nextPosition.x()) {
            currentPosition.setBottomRightPosition(nextPosition);
            nextPosition.setTopLeftPosition(currentPosition);
        } else if (currentPosition.y() < nextPosition.y() && currentPosition.x() > nextPosition.x()) {
            currentPosition.setBottomLeftPosition(nextPosition);
            nextPosition.setTopRightPosition(currentPosition);
        } else if (currentPosition.y() > nextPosition.y() && currentPosition.x() < nextPosition.x()) {
            if (currentPosition.y() != 8) { // Exclude bottom right connection for the last row (row 8)
                currentPosition.setTopRightPosition(nextPosition);
                nextPosition.setBottomLeftPosition(currentPosition);
            }
        } else if (currentPosition.y() > nextPosition.y() && currentPosition.x() > nextPosition.x()) {
            if (currentPosition.y() != 8) { // Exclude bottom left connection for the last row (row 8)
                currentPosition.setTopLeftPosition(nextPosition);
                nextPosition.setBottomRightPosition(currentPosition);
            }
        }
    }


    public static Position getStartingPosition() {
        return startingPosition;
    }

    private void setUpInitialChessPositions() {
        Position currentPosition = this.getStartingPosition();
        Position rowStartPosition = this.getStartingPosition();  // Starting position of the current row
        while (currentPosition != null) {
            // Set up white figures
            if (currentPosition.y() == 1) {
                placeFigure(new RookFigure(FigureColor.WHITE, 1, 1));
                placeFigure(new KnightFigure(FigureColor.WHITE, 2, 1));
                placeFigure(new BishopFigure(FigureColor.WHITE, 3, 1));
                placeFigure(new QueenFigure(FigureColor.WHITE, 4, 1));
                placeFigure(new KingFigure(FigureColor.WHITE, 5, 1));
                placeFigure(new BishopFigure(FigureColor.WHITE, 6, 1));
                placeFigure(new KnightFigure(FigureColor.WHITE, 7, 1));
                placeFigure(new RookFigure(FigureColor.WHITE, 8, 1));
            }
            if (currentPosition.y() == 2) {
            //    placeFigure(new PawnFigure(FigureColor.WHITE, currentPosition.x(), 2));
            }

            // Set up black figures
            if (currentPosition.y() == 7) {
            //    placeFigure(new PawnFigure(FigureColor.BLACK, currentPosition.x(), 7));
            }
            if (currentPosition.y() == 8) {
                placeFigure(new RookFigure(FigureColor.BLACK, 1, 8));
                placeFigure(new KnightFigure(FigureColor.BLACK, 2, 8));
                placeFigure(new BishopFigure(FigureColor.BLACK, 3, 8));
                placeFigure(new QueenFigure(FigureColor.BLACK, 4, 8));
                placeFigure(new KingFigure(FigureColor.BLACK, 5, 8));
                placeFigure(new BishopFigure(FigureColor.BLACK, 6, 8));
                placeFigure(new KnightFigure(FigureColor.BLACK, 7, 8));
                placeFigure(new RookFigure(FigureColor.BLACK, 8, 8));
            }
            if (currentPosition.x() < 8) {
                // Move to the next position in the current row
                currentPosition = currentPosition.getRightPosition();
            } else {
                // Move to the next row
                rowStartPosition = rowStartPosition.getTopPosition();
                currentPosition = rowStartPosition;
            }
        }
    }

    private void placeFigure(Figure figure) {
        Position position = findPosition(figure.getPosition().x(), figure.getPosition().y());
        if (position != null) {
            position.setFigure(figure);
        }
    }

    public static Position findPosition(int x, int y) {
        Position currentPosition = getStartingPosition();
        Position rowStartPosition = getStartingPosition();  // Starting position of the current row
        while (currentPosition != null) {
            if (currentPosition.y() == y && currentPosition.x() == x) {
                return currentPosition;
            }

            if (currentPosition.x() < 8) {
                // Move to the next position in the current row
                currentPosition = currentPosition.getRightPosition();
            } else {
                // Move to the next row
                rowStartPosition = rowStartPosition.getTopPosition();
                currentPosition = rowStartPosition;
            }
        }
        return null;
    }

    public void printBoard() {
        Position currentPosition = this.getStartingPosition();
        while (currentPosition.getTopPosition() != null) {
            currentPosition = currentPosition.getTopPosition();
        }
        Position rowStartPosition = currentPosition;

        // Starting position of the current row
        while (currentPosition != null) {
            Figure figure = currentPosition.getFigure();
            // Print figure if it exists, otherwise print empty position
            if (figure != null) {
                System.out.print(figure.getSymbol() + " "); // Assuming Figure class has getSymbol method
            } else {
                System.out.print("- "); // Empty position symbol
            }

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
        System.out.println();
    }

    // Method to move a piece on the board
    public void moveFigure(int startCol, int startRow, int endCol, int endRow) {
        // Get the piece at the start position
        Position prevPosition = findPosition(startCol, startRow);
        Position nextPosition = findPosition(endCol, endRow);

        assert prevPosition != null;
        assert nextPosition != null;

        Figure prevFigure = prevPosition.getFigure();

        if (prevFigure.canMoveTo(prevPosition, nextPosition)) {
            // Move the piece to the end position
            // prevFigure.setPosition(nextPosition); // #TODO delete
            nextPosition.setFigure(prevFigure);   // Place the piece at the end position
            prevPosition.setFigure(null); // Remove the piece from the start position
        } else {
            System.out.println("This Move invalid.");
        }
    }

    public static Figure getFigureOnField(int x, int y) {
        return Objects.requireNonNull(findPosition(x, y)).getFigure();
    }

    public static boolean isFigureOnField(int x, int y) {
        return getFigureOnField(x, y) != null;
    }
}
