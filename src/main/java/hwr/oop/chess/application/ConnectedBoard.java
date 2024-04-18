package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.Figure;
import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.application.figures.PawnFigure;
import hwr.oop.chess.application.figures.RookFigure;
import hwr.oop.chess.application.figures.QueenFigure;
import hwr.oop.chess.application.figures.KingFigure;
import hwr.oop.chess.application.figures.KnightFigure;
import hwr.oop.chess.application.figures.BishopFigure;

import java.util.function.Consumer;

public class ConnectedBoard {
    private Cell startingCell;

    public ConnectedBoard() {
        initializeBoard();
    }

    private void initializeBoard() {
        System.out.println("initializeBoard");
        // Create the first cell and store it in startingCell
        startingCell = new Cell(1, 1);
        Cell previousCell = startingCell;
        Cell previousRowStart = startingCell;
        Cell previousRowCell = startingCell;

        // Create and connect cells for each row and column of the board
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
               // System.out.print(row + "" + col + ", ");
                // Skip the first cell since it's already created
                if (row == 1 && col == 1) {
                    continue;
                }
                if (col == 1) {
                    previousRowCell = previousRowStart;
                }
                // Create a new cell
                Cell currentCell = new Cell(row, col);
                // Connect the cell to the previous cell
                connectCells(previousCell, currentCell);
                if (row != 1) {
                    connectCells(currentCell, previousRowCell);
                    if (col != 1) {
                        connectCells(currentCell, previousRowCell.getLeftCell());
                    }
                    if (col != 8) {
                        connectCells(currentCell, previousRowCell.getRightCell());
                    } else {
                        previousRowStart = previousRowStart.getTopCell();
                    }
                }

                // Set the next cell as the current cell
                previousCell = currentCell;
                previousRowCell = previousRowCell.getRightCell();
            }
           // System.out.println("  :" + row + "Row");
        }

        // Set up the initial chess positions
        setUpInitialChessPositions();
    }

    // Method to connect each cell
    private void connectCells(Cell currentCell, Cell nextCell) {
        if (nextCell == null) {
            return; // Skip if the next cell is null
        }

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
            if (currentCell.getRow() != 8) { // Exclude bottom right connection for the last row (row 8)
                currentCell.setTopRightCell(nextCell);
                nextCell.setBottomLeftCell(currentCell);
            }
        } else if (currentCell.getRow() > nextCell.getRow() && currentCell.getCol() > nextCell.getCol()) {
            if (currentCell.getRow() != 8) { // Exclude bottom left connection for the last row (row 8)
                currentCell.setTopLeftCell(nextCell);
                nextCell.setBottomRightCell(currentCell);
            }
        }
    }


    public Cell getStartingCell() {
        return startingCell;
    }

    private void setUpInitialChessPositions() {
        Cell currentCell = this.getStartingCell();
        Cell rowStartCell = this.getStartingCell();  // Starting cell of the current row
        while (currentCell != null) {
            // Set up white figures
            if (currentCell.getRow() == 1) {
                placeFigure(new RookFigure(FigureColor.WHITE,1, 1));
                placeFigure(new KnightFigure(FigureColor.WHITE, 1, 2));
                placeFigure(new BishopFigure(FigureColor.WHITE, 1,3));
                placeFigure(new QueenFigure(FigureColor.WHITE,1,4));
                placeFigure(new KingFigure(FigureColor.WHITE,1,5));
                placeFigure(new BishopFigure(FigureColor.WHITE,1,6));
                placeFigure(new KnightFigure(FigureColor.WHITE,1,7));
                placeFigure(new RookFigure(FigureColor.WHITE,1,8));
            }
            if (currentCell.getRow() == 2) {
                placeFigure(new PawnFigure(FigureColor.WHITE, 2, currentCell.getCol()));
            }

            // Set up black figures
            if (currentCell.getRow() == 7) {
                placeFigure(new PawnFigure(FigureColor.BLACK,7, currentCell.getCol()));
            }
            if (currentCell.getRow() == 8) {
                placeFigure(new RookFigure(FigureColor.BLACK, 8,1));
                placeFigure(new KnightFigure(FigureColor.BLACK, 8,2));
                placeFigure(new BishopFigure(FigureColor.BLACK, 8,3));
                placeFigure(new QueenFigure(FigureColor.BLACK,8,4));
                placeFigure(new KingFigure(FigureColor.BLACK, 8,5));
                placeFigure(new BishopFigure(FigureColor.BLACK, 8, 6));
                placeFigure(new KnightFigure(FigureColor.BLACK,8,7));
                placeFigure(new RookFigure(FigureColor.BLACK,8,8));
            }
            if (currentCell.getCol() < 8) {
                // Move to the next cell in the current row
                currentCell = currentCell.getRightCell();
            } else {
                // Move to the next row
                rowStartCell = rowStartCell.getTopCell();
                currentCell = rowStartCell;
            }
        }
    }

    private void placeFigure(Figure figure) {
        Cell cell = findCell(figure.position().x(), figure.position().y());
        if (cell != null) {
            cell.setFigure(figure);
        }
    }

    private Cell findCell(int row, int col) {
        Cell currentCell = this.getStartingCell();
        Cell rowStartCell = this.getStartingCell();  // Starting cell of the current row
        while (currentCell != null) {
            if (currentCell.getRow() == row && currentCell.getCol() == col) {
                return currentCell;
            }

            if (currentCell.getCol() < 8) {
                // Move to the next cell in the current row
                currentCell = currentCell.getRightCell();
            } else {
                // Move to the next row
                rowStartCell = rowStartCell.getTopCell();
                currentCell = rowStartCell;
            }
        }
        return null;
    }

    public void printBoard() {
        Cell currentCell = this.getStartingCell();
        Cell rowStartCell = this.getStartingCell();  // Starting cell of the current row
        while (currentCell != null) {
            Figure figure = currentCell.getFigure();
            // Print figure if it exists, otherwise print empty cell
            if (figure != null) {
                System.out.print(figure.getSymbol() + " "); // Assuming Figure class has getSymbol method
            } else {
                System.out.print("- "); // Empty cell symbol
            }

            if (currentCell.getCol() < 8) {
                // Move to the next cell in the current row
                currentCell = currentCell.getRightCell();
            } else {
                // Move to the next row
                rowStartCell = rowStartCell.getTopCell();
                currentCell = rowStartCell;
                System.out.println();
            }
        }
    }
}
