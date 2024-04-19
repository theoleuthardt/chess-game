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
                placeFigure(new RookFigure(FigureColor.WHITE, 1, 1));
                placeFigure(new KnightFigure(FigureColor.WHITE, 2, 1));
                placeFigure(new BishopFigure(FigureColor.WHITE, 3, 1));
                placeFigure(new QueenFigure(FigureColor.WHITE, 4, 4));
                placeFigure(new KingFigure(FigureColor.WHITE, 5, 1));
                placeFigure(new BishopFigure(FigureColor.WHITE, 6, 1));
                placeFigure(new KnightFigure(FigureColor.WHITE, 7, 1));
                placeFigure(new RookFigure(FigureColor.WHITE, 8, 1));
            }
            if (currentCell.getRow() == 2) {
                placeFigure(new PawnFigure(FigureColor.WHITE, currentCell.getCol(), 2));
            }

            // Set up black figures
            if (currentCell.getRow() == 7) {
                placeFigure(new PawnFigure(FigureColor.BLACK, currentCell.getCol(), 7));
            }
            if (currentCell.getRow() == 8) {
                placeFigure(new RookFigure(FigureColor.BLACK, 1, 8));
                placeFigure(new KnightFigure(FigureColor.BLACK, 2, 8));
                placeFigure(new BishopFigure(FigureColor.BLACK, 3, 8));
                placeFigure(new QueenFigure(FigureColor.BLACK, 4, 8));
                placeFigure(new KingFigure(FigureColor.BLACK, 5, 8));
                placeFigure(new BishopFigure(FigureColor.BLACK, 6, 8));
                placeFigure(new KnightFigure(FigureColor.BLACK, 7, 8));
                placeFigure(new RookFigure(FigureColor.BLACK, 8, 8));
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
        Cell cell = findCell(figure.getPosition().x(), figure.getPosition().y());
        if (cell != null) {
            cell.setFigure(figure);
        }
    }

    public Cell findCell(int col, int row) {
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
        while (currentCell.getTopCell() != null) {
            currentCell = currentCell.getTopCell();
        }
        Cell rowStartCell = currentCell;

        // Starting cell of the current row
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
                rowStartCell = rowStartCell.getBottomCell();
                currentCell = rowStartCell;
                System.out.println();
            }
        }
        System.out.println();
    }

    // Method to move a piece on the board
    public void moveFigure(int startCol, int startRow, int endCol, int endRow) {
        // Get the piece at the start position
        Cell prevCell = this.findCell(startCol, startRow);
        Cell nextCell = this.findCell(endCol, endRow);

        Figure figure = prevCell.getFigure();
        figure.moveTo(endRow, endCol);
        // Move the piece to the end position
        prevCell.setFigure(null); // Remove the piece from the start position
        nextCell.setFigure(figure);   // Place the piece at the end position
    }
}
