package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.Figure;
import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.application.figures.PawnFigure;
import hwr.oop.chess.application.figures.RookFigure;
import hwr.oop.chess.application.figures.QueenFigure;
import hwr.oop.chess.application.figures.KingFigure;
import hwr.oop.chess.application.figures.KnightFigure;
import hwr.oop.chess.application.figures.BishopFigure;

public class ConnectedBoard {
    private Cell startingCell;

    public ConnectedBoard() {
        initializeBoard();
    }

    private void initializeBoard() {
        System.out.println("initializeBoard");
        // Create the first cell and store it in startingCell
        startingCell = new Cell(0, 0);
        Cell previousCell = startingCell;
        Cell previousRowStart = startingCell;
//        Cell currentRowStart = startingCell;
        Cell previousRowCell = startingCell;
        // Create and connect cells for each row and column of the board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                System.out.print(row + col + ", ");

                // Skip the first cell since it's already created
                if (row == 0 && col == 0) {
                    continue;
                }
                if (col == 0) {
                    previousRowCell = previousRowStart;
                }
                // Create a new cell
                Cell currentCell = new Cell(row, col);
                // Connect the cell to the previous cell
                connectCells(previousCell, currentCell);
                if (row != 0) {
                    connectCells(currentCell, previousRowCell);
                    if (col != 0) {
                        connectCells(currentCell, previousRowCell.getLeftCell());
                    }
                    if (col != 7) {
                        connectCells(currentCell, previousRowCell.getRightCell());
                    }
                }

                // Set the next cell as the current cell
                previousCell = currentCell;
                previousRowCell = previousRowCell.getRightCell();

                if (col == 7) {
                    previousRowStart = previousRowStart.getTopCell();
                }
            }
            System.out.println("  :" + row + "Row");

        }

        // Set up the initial chess positions
        setUpInitialChessPositions();
    }

    // Method to connect each cell
    private void connectCells(Cell currentCell, Cell nextCell) {
        // Connect horizontally
        if (currentCell.getRow() == nextCell.getRow()) {
            if (currentCell.getCol() < nextCell.getCol()) {
                currentCell.setRightCell(nextCell);
                nextCell.setLeftCell(currentCell);
            } else if (currentCell.getCol() > nextCell.getCol()) {
                currentCell.setLeftCell(nextCell);
                nextCell.setRightCell(currentCell);
            }
        }

        // Connect vertically
        if (currentCell.getCol() == nextCell.getCol()) {
            // Connect vertically
            if (currentCell.getRow() > nextCell.getRow()) {
                currentCell.setBottomCell(nextCell);
                nextCell.setTopCell(currentCell);
            } else if (currentCell.getRow() < nextCell.getRow()) {
                currentCell.setTopCell(nextCell);
                nextCell.setBottomCell(currentCell);
            }
        }

        // Connect diagonally
        if (currentCell.getRow() < nextCell.getRow() && currentCell.getCol() < nextCell.getCol()) {
            currentCell.setBottomRightCell(nextCell);
            nextCell.setTopLeftCell(currentCell);
        } else if (currentCell.getRow() < nextCell.getRow() && currentCell.getCol() > nextCell.getCol()) {
            currentCell.setBottomLeftCell(nextCell);
            nextCell.setTopRightCell(currentCell);
        } else if (currentCell.getRow() > nextCell.getRow() && currentCell.getCol() < nextCell.getCol()) {
            currentCell.setTopRightCell(nextCell);
            nextCell.setBottomLeftCell(currentCell);
        } else if (currentCell.getRow() > nextCell.getRow() && currentCell.getCol() > nextCell.getCol()) {
            currentCell.setTopLeftCell(nextCell);
            nextCell.setBottomRightCell(currentCell);
        }
    }

    public Cell getStartingCell() {
        return startingCell;
    }

    private void setUpInitialChessPositions() {
        // Set up white figures
        placeFigure(new RookFigure(FigureColor.WHITE), 0, 0);
        placeFigure(new KnightFigure(FigureColor.WHITE), 0, 1);
        placeFigure(new BishopFigure(FigureColor.WHITE), 0, 2);
        placeFigure(new QueenFigure(FigureColor.WHITE), 0, 3);
        placeFigure(new KingFigure(FigureColor.WHITE), 0, 4);
        placeFigure(new BishopFigure(FigureColor.WHITE), 0, 5);
        placeFigure(new KnightFigure(FigureColor.WHITE), 0, 6);
        placeFigure(new RookFigure(FigureColor.WHITE), 0, 7);
        for (int col = 0; col < 8; col++) {
            placeFigure(new PawnFigure(FigureColor.WHITE), 1, col);
        }

        // Set up black figures
        placeFigure(new RookFigure(FigureColor.BLACK), 7, 0);
        placeFigure(new KnightFigure(FigureColor.BLACK), 7, 1);
        placeFigure(new BishopFigure(FigureColor.BLACK), 7, 2);
        placeFigure(new QueenFigure(FigureColor.BLACK), 7, 3);
        placeFigure(new KingFigure(FigureColor.BLACK), 7, 4);
        placeFigure(new BishopFigure(FigureColor.BLACK), 7, 5);
        placeFigure(new KnightFigure(FigureColor.BLACK), 7, 6);
        placeFigure(new RookFigure(FigureColor.BLACK), 7, 7);
        for (int col = 0; col < 8; col++) {
            placeFigure(new PawnFigure(FigureColor.BLACK), 6, col);
        }
    }

    private void placeFigure(Figure figure, int row, int col) {
        Cell cell = findCell(row, col);
        if (cell != null) {
            cell.setFigure(figure);
        }
    }

    private Cell findCell(int row, int col) {
        Cell currentCell = startingCell;
        while (currentCell != null) {
            if (currentCell.getRow() == row && currentCell.getCol() == col) {
                return currentCell;
            }
            currentCell = currentCell.getRightCell();
        }
        return null;
    }

    public void printBoard() {
        Cell currentCell = startingCell;
        while (currentCell != null) {
            Cell rowStartCell = currentCell; // Starting cell of the current row

            while (currentCell != null) {
                // Print figure if it exists, otherwise print empty cell
                Figure figure = currentCell.getFigure();
                // Print figure if it exists, otherwise print empty cell
                if (figure != null) {
                    System.out.print(figure.getSymbol() + " "); // Assuming Figure class has getSymbol method
                } else {
                    System.out.print("- "); // Empty cell symbol
                }

                // Move to the next cell in the current row
                currentCell = currentCell.getRightCell();
            }

            // Move to the next row
            currentCell = rowStartCell.getBottomCell();
        }
    }
}
