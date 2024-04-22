package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;

import java.util.ArrayList;

public class Bishop implements Figure {
    private Cell startCell = null;
    private Cell currentCell = null;
    private static final FigureType type = FigureType.BISHOP;
    private final FigureColor color;

    public Bishop(FigureColor color, int x, int y) {
        Cell cell = new Cell(x, y);
        this.startCell = cell;
        this.currentCell = cell;
        this.color = color;
    }


    public boolean canMoveTo(Cell to) {
        Cell from = this.currentCell;

        // this move is not allowed as it does not obey the rules.
        return false;
    }

    @Override
    public ArrayList<Cell> getAvailableCell(Cell currentRook) {
        return null;
    }

    @Override
    public boolean canMoveTo(Cell prevCell, Cell nextCell) {
        return false;
    }

    @Override
    public boolean isOnField(int x, int y) {
        return false;
    }

    public boolean isCaptured() {
        return this.currentCell == null;
    }

    @Override
    public void setCell(Cell cell) {

    }

    public void moveTo(int x, int y) {
        Cell cell = new Cell(x, y);
        if (canMoveTo(cell)) {
            this.currentCell = cell;
        }
    }

    @Override
    public void moveTo(Cell prevCell, Cell nextCell) {

    }

    public Cell getCell() {
        return this.currentCell;
    }

    public FigureColor getColor() {
        return this.color;
    }

    public FigureType getType() {
        return type;
    }

    public char getSymbol() {
        if (this.color == FigureColor.WHITE) {
            return 'B';
        } else {
            return 'b';
        }
    }
}