package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;

import java.util.ArrayList;

public class Knight implements Figure {
    private Cell startPosition = null;
    private Cell currentPosition = null;
    private static final FigureType type = FigureType.KNIGHT;
    private final FigureColor color;

    public Knight(FigureColor color, int x, int y) {
        Cell position = new Cell(x, y);
        this.startPosition = position;
        this.currentPosition = position;
        this.color = color;
    }

    @Override
    public ArrayList<Cell> getAvailablePosition(Cell currentRook) {
        return null;
    }

    @Override
    public boolean canMoveTo(Cell prevPosition, Cell nextPosition) {
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
        return this.currentPosition == null;
    }

    @Override
    public void setPosition(Cell position) {

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
            return 'N';
        } else {
            return 'n';
        }
    }
}