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
    private int row;
    private int col;

    public Cell(int x, int y) {
        this.row = row;
        this.col = col;
        this.figure = null;
        this.topCell = null;
        this.bottomCell = null;
        this.leftCell = null;
        this.rightCell = null;
        this.topLeftCell = null;
        this.topRightCell = null;
        this.bottomLeftCell = null;
        this.bottomRightCell = null;
    }

    // Method to set the figure
    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    // Method to get the figure
    public Figure getFigure() {
        return figure;
    }

    // Methods to set adjacent cells
    public void setTopCell(Cell cell) {
        this.topCell = cell;
    }

    public void setBottomCell(Cell cell) {
        this.bottomCell = cell;
    }

    public void setLeftCell(Cell cell) {
        this.leftCell = cell;
    }

    public void setRightCell(Cell cell) {
        this.rightCell = cell;
    }

    public void setTopLeftCell(Cell cell) {
        this.topLeftCell = cell;
    }

    public void setTopRightCell(Cell cell) {
        this.topRightCell = cell;
    }

    public void setBottomLeftCell(Cell cell) {
        this.bottomLeftCell = cell;
    }

    public void setBottomRightCell(Cell cell) {
        this.bottomRightCell = cell;
    }

    // Methods to get adjacent cells
    public Cell getTopCell() {
        return topCell;
    }

    public Cell getBottomCell() {
        return bottomCell;
    }

    public Cell getLeftCell() {
        return leftCell;
    }

    public Cell getRightCell() {
        return rightCell;
    }

    public Cell getTopLeftCell() {
        return topLeftCell;
    }

    public Cell getTopRightCell() {
        return topRightCell;
    }

    public Cell getBottomLeftCell() {
        return bottomLeftCell;
    }

    public Cell getBottomRightCell() {
        return bottomRightCell;
    }

    // Method to return the index of the row to which the cell belongs
    public int getRow() {
        return row;
    }

    // Method to return the index of the column to which the cell belongs
    public int getCol() {
        return col;
    }

    // Method to return all cells in the row to which this cell belongs
    public List<Cell> getRowCells() {
        List<Cell> rowCells = new ArrayList<>();
        Cell currentCell = this;
        // Add all cells belonging to the row by moving right from the current cell
        while (currentCell != null) {
            rowCells.add(currentCell);
            currentCell = currentCell.getRightCell();
        }
        return rowCells;
    }

    // Method to return all cells in the column to which this cell belongs
    public List<Cell> getColCells() {
        List<Cell> colCells = new ArrayList<>();
        Cell currentCell = this;
        // Add all cells belonging to the column by moving down from the current cell
        while (currentCell != null) {
            colCells.add(currentCell);
            currentCell = currentCell.getBottomCell();
        }
        return colCells;
    }
}

