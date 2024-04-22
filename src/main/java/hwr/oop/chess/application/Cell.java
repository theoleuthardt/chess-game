package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.Figure;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private Figure figure;
    private Cell topCell;
    private Cell bottomCell;
    private Cell leftCell;
    private Cell rightCell;
    private Cell topLeftCell;
    private Cell topRightCell;
    private Cell bottomLeftCell;
    private Cell bottomRightCell;

    private final int y;
    private final int x;

    public Cell(int x, int y) {
        if (x < 1 || x > 8 || y < 1 || y > 8) {
            throw new IllegalArgumentException("Invalid Position");
        }

        this.x = x;
        this.y = y;
    }

    public Cell(char x, int y) {
        this(x - 96, y);
    }


    // Method to set the figure
    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    // Method to get the figure
    public Figure getFigure() {
        return figure;
    }

    // Methods to set adjacent positions
    public void setTopPosition(Cell position) {
        this.topCell = position;
    }

    public void setBottomPosition(Cell position) {
        this.bottomCell = position;
    }

    public void setLeftPosition(Cell position) {
        this.leftCell = position;
    }

    public void setRightPosition(Cell position) {
        this.rightCell = position;
    }

    public void setTopLeftPosition(Cell position) {
        this.topLeftCell = position;
    }

    public void setTopRightPosition(Cell position) {
        this.topRightCell = position;
    }

    public void setBottomLeftPosition(Cell position) {
        this.bottomLeftCell = position;
    }

    public void setBottomRightPosition(Cell position) {
        this.bottomRightCell = position;
    }

    // Methods to get adjacent positions
    public Cell topCell() {
        return topCell;
    }

    public Cell bottomCell() {
        return bottomCell;
    }

    public Cell leftCell() {
        return leftCell;
    }

    public Cell rightCell() {
        return rightCell;
    }

    public Cell topLeftCell() {
        return topLeftCell;
    }

    public Cell topRightCell() {
        return topRightCell;
    }

    public Cell bottomLeftCell() {
        return bottomLeftCell;
    }

    public Cell bottomRightCell() {
        return bottomRightCell;
    }

    // Method to return the index of the column to which the position belongs
    public int x() {
        return x;
    }

    // Method to return the index of the row to which the position belongs
    public int y() {
        return y;
    }

    // Method to return all positions in the row to which this position belongs
    public List<Cell> getCellsInRow() {
        List<Cell> rowCells = new ArrayList<>();
        Cell currentCell = this;
        // move to the first cell (left) in the row
        while(currentCell.leftCell() != null) {
            currentCell = currentCell.leftCell();
        }
        // Add all positions belonging to the row by moving right from the current position
        while (currentCell != null) {
            rowCells.add(currentCell);
            currentCell = currentCell.rightCell();
        }
        return rowCells;
    }

    // Method to return all positions in the column to which this position belongs
    public List<Cell> getCellsInColumn() {
        List<Cell> columnCells = new ArrayList<>();
        Cell currentCell = this;
        // move to the first cell (top) in the colum
        while(currentCell.topCell() != null) {
            currentCell = currentCell.topCell();
        }
        // Add all positions belonging to the column by moving down from the current position
        while (currentCell != null) {
            columnCells.add(currentCell);
            currentCell = currentCell.bottomCell();
        }
        return columnCells;
    }

    public boolean isEqualTo(Cell pos1) {
        Cell pos2 = this;
        return (pos1.x() == pos2.x()) && (pos1.y() == pos2.y());
    }
}
