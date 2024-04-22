package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;

import java.util.ArrayList;

public class King implements Figure {
    private Cell startCell = null;
    private Cell currentCell = null;
    private static final FigureType type = FigureType.KING;
    private final FigureColor color;

    public King(FigureColor color, int x, int y) {
        Cell cell = new Cell(x, y);
        this.startCell = cell;
        this.currentCell = cell;
        this.color = color;
    }


    @Override
    public ArrayList<Cell> getAvailablePosition(Cell currentRook) {
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

    @Override
    public void moveTo(int x, int y) {

    }

    public boolean isCaptured() {
        return this.currentCell == null;
    }

    @Override
    public void setCell(Cell cel) {

    }

    @Override
    public void moveTo(Cell prevPosition, Cell nextPosition) {

    }

    public Cell getPosition() {
        return this.currentPosition;
    }

    public FigureColor getColor() {
        return this.color;
    }

    public FigureType getType() {
        return type;
    }

    public char getSymbol() {
        if (this.color == FigureColor.WHITE) {
            return 'K';
        } else {
            return 'k';
        }
    }
}