package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.Figure;
import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.application.figures.Rook;
import hwr.oop.chess.application.figures.Queen;
import hwr.oop.chess.application.figures.King;
import hwr.oop.chess.application.figures.Knight;
import hwr.oop.chess.application.figures.Bishop;

import java.util.Objects;

public class Board {
    private static Cell firstCell;

    public Board() {
        initializeBoard();
    }

    private void initializeBoard() {
        // Create the first position and store it in startingPosition
        firstCell = new Cell(1, 1);
        Cell leftCell = firstCell;
        Cell topCell = firstCell;
        Cell topCellRowStart = firstCell;

        // Create and connect positions for each row and column of the board
        for (int y = 1; y <= 8; y++) {
            for (int x = 1; x <= 8; x++) {
                // Skip the first position since it's already created
                if (y == 1 && x == 1) {
                    continue;
                }
                if (x == 1) {
                    topCell = topCellRowStart;
                }
                // Create a new position
                Cell newCell = new Cell(x, y);
                // Connect the position to the previous position
                connectCells(newCell, leftCell); // left
                if (topCell != null) {
                    connectCells(newCell, topCell); // top
                    if (topCell.leftCell() != null) {
                        connectCells(newCell, topCell.leftCell()); // top left
                    }
                    if (topCell.rightCell() != null) {
                        connectCells(newCell, topCell.rightCell()); // top right
                    }
                    if(topCell.rightCell() == null && topCellRowStart != null){
                        topCellRowStart = topCellRowStart.topCell();
                    }
                }

                // Set the next position as the current position
                leftCell = newCell;
                if(topCell != null) {
                    topCell = topCell.rightCell();
                }
            }
        }

        // Set up the initial chess positions
        setUpInitialChessPositions();
    }

    // Method to connect each position
    private void connectCells(Cell cell1, Cell cell2) {
        Objects.requireNonNull(cell1);
        Objects.requireNonNull(cell2);

        if(cell1.isEqualTo(cell2)) {
            throw new IllegalArgumentException("The cells are not allowed to be equal");
        }

        // Connect horizontally
        if (cell1.y() == cell2.y()) {
            if (cell1.x() < cell2.x()) { // cell1 is left of cell2
                cell1.setRightPosition(cell2);
                cell2.setLeftPosition(cell1);
            } else if (cell1.x() > cell2.x()) { // cell1 is right of cell2
                cell1.setLeftPosition(cell2);
                cell2.setRightPosition(cell1);
            }
            return;
        }

        // Connect vertically
        if (cell1.x() == cell2.x()) {
            if (cell1.y() > cell2.y()) { // cell1 is above cell2
                cell1.setBottomPosition(cell2);
                cell2.setTopPosition(cell1);
            } else if (cell1.y() < cell2.y()) { // cell1 is below cell2
                cell1.setTopPosition(cell2);
                cell2.setBottomPosition(cell1);
            }
            return;
        }

        // Connect diagonally
        if (cell1.y() > cell2.y()) {
            }
        }
        if (cell1.y() < cell2.y() && cell1.x() < cell2.x()) {
            cell1.setBottomRightPosition(cell2);
            cell2.setTopLeftPosition(cell1);
        } else if (cell1.y() < cell2.y() && cell1.x() > cell2.x()) {
            cell1.setBottomLeftPosition(cell2);
            cell2.setTopRightPosition(cell1);
        } else if (cell1.y() > cell2.y() && cell1.x() < cell2.x()) {
            if (cell1.y() != 8) { // Exclude bottom right connection for the last row (row 8)
                cell1.setTopRightPosition(cell2);
                cell2.setBottomLeftPosition(cell1);
            }
        } else if (cell1.y() > cell2.y() && cell1.x() > cell2.x()) {
            if (cell1.y() != 8) { // Exclude bottom left connection for the last row (row 8)
                cell1.setTopLeftPosition(cell2);
                cell2.setBottomRightPosition(cell1);
            }
        }
    }


    public static Cell getStartingPosition() {
        return firstCell;
    }

    private void setUpInitialChessPositions() {
        Cell currentPosition = this.getStartingPosition();
        Cell rowStartPosition = this.getStartingPosition();  // Starting position of the current row
        while (currentPosition != null) {
            // Set up white figures
            if (currentPosition.y() == 1) {
                placeFigure(new Rook(FigureColor.WHITE, 1, 1));
                placeFigure(new Knight(FigureColor.WHITE, 2, 1));
                placeFigure(new Bishop(FigureColor.WHITE, 3, 1));
                placeFigure(new Queen(FigureColor.WHITE, 4, 1));
                placeFigure(new King(FigureColor.WHITE, 5, 1));
                placeFigure(new Bishop(FigureColor.WHITE, 6, 1));
                placeFigure(new Knight(FigureColor.WHITE, 7, 1));
                placeFigure(new Rook(FigureColor.WHITE, 8, 1));
            }
            if (currentPosition.y() == 2) {
                //    placeFigure(new PawnFigure(FigureColor.WHITE, currentPosition.x(), 2));
            }

            // Set up black figures
            if (currentPosition.y() == 7) {
                //    placeFigure(new PawnFigure(FigureColor.BLACK, currentPosition.x(), 7));
            }
            if (currentPosition.y() == 8) {
                placeFigure(new Rook(FigureColor.BLACK, 1, 8));
                placeFigure(new Knight(FigureColor.BLACK, 2, 8));
                placeFigure(new Bishop(FigureColor.BLACK, 3, 8));
                placeFigure(new Queen(FigureColor.BLACK, 4, 8));
                placeFigure(new King(FigureColor.BLACK, 5, 8));
                placeFigure(new Bishop(FigureColor.BLACK, 6, 8));
                placeFigure(new Knight(FigureColor.BLACK, 7, 8));
                placeFigure(new Rook(FigureColor.BLACK, 8, 8));
            }
            if (currentPosition.x() < 8) {
                // Move to the next position in the current row
                currentPosition = currentPosition.rightCell();
            } else {
                // Move to the next row
                rowStartPosition = rowStartPosition.topCell();
                currentPosition = rowStartPosition;
            }
        }
    }

    private void placeFigure(Figure figure) {
        Cell position = findPosition(figure.getPosition().x(), figure.getPosition().y());
        if (position != null) {
            position.setFigure(figure);
        }
    }

    public static Cell findPosition(int x, int y) {
        Cell currentPosition = getStartingPosition();
        Cell rowStartPosition = getStartingPosition();  // Starting position of the current row
        while (currentPosition != null) {
            if (currentPosition.y() == y && currentPosition.x() == x) {
                return currentPosition;
            }

            if (currentPosition.x() < 8) {
                // Move to the next position in the current row
                currentPosition = currentPosition.rightCell();
            } else {
                // Move to the next row
                rowStartPosition = rowStartPosition.topCell();
                currentPosition = rowStartPosition;
            }
        }
        return null;
    }

    public void printBoard() {
        Cell currentPosition = this.getStartingPosition();
        while (currentPosition.topCell() != null) {
            currentPosition = currentPosition.topCell();
        }
        Cell rowStartPosition = currentPosition;

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
                currentPosition = currentPosition.rightCell();
            } else {
                // Move to the next row
                rowStartPosition = rowStartPosition.bottomCell();
                currentPosition = rowStartPosition;
                System.out.println();
            }
        }
        System.out.println();
    }

    // Method to move a piece on the board
    public void moveFigure(int startCol, int startRow, int endCol, int endRow) {
        // Get the piece at the start position
        Cell prevPosition = findPosition(startCol, startRow);
        Cell nextPosition = findPosition(endCol, endRow);

        assert prevPosition != null;
        assert nextPosition != null;

        Figure prevFigure = prevPosition.getFigure();

        if (prevFigure.canMoveTo(prevPosition, nextPosition)) {
            // Move the piece to the end position
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
