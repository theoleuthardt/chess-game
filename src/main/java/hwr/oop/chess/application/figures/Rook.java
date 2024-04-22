package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;

import java.util.ArrayList;

public class Rook implements Figure {
    Cell startCell = null;
    Cell currentCell = null;
    FigureType type = null;
    FigureColor color = null;

    public Rook(FigureColor color, int x, int y) {
        Cell cell = new Cell(x, y);
        this.type = FigureType.ROOK;
        this.color = color;
        this.startCell = cell;
        this.currentCell = cell;
    }

    public ArrayList<Cell> getAvailableCell(Cell currentRook) {
        ArrayList<Cell> list = new ArrayList<>();

        // Check above
        Cell current = currentRook.getTopCell();

        //If there is no figure or if it's a different color, the piece can move
        while (current != null && current.getFigure() == null) {
            list.add(current);
            current = current.getTopCell();
        }
        if (current != null && current.getFigure() != null && current.getFigure().getColor() != currentRook.getFigure().getColor()) {
            list.add(current);
        }

        // Check below
        current = currentRook.getBottomCell();
        while (current != null && current.getFigure() == null) {
            list.add(current);
            current = current.getBottomCell();
        }
        if (current != null && current.getFigure() != null && current.getFigure().getColor() != currentRook.getFigure().getColor()) {
            list.add(current);
        }

        // Check the right
        current = currentRook.getRightCell();
        while (current != null && current.getFigure() == null) {
            list.add(current);
            current = current.getRightCell();
        }
        if (current != null && current.getFigure() != null && current.getFigure().getColor() != currentRook.getFigure().getColor()) {
            list.add(current);
        }

        // Check the left
        current = currentRook.getLeftCell();
        while (current != null && current.getFigure() == null) {
            list.add(current);
            current = current.getLeftCell();
        }
        if (current != null && current.getFigure() != null && current.getFigure().getColor() != currentRook.getFigure().getColor()) {
            list.add(current);
        }
        System.out.println("availableCell" + list.toArray().length);
        return list;
    }

    public boolean canMoveTo(Cell prevCell, Cell nextCell) {
        ArrayList<Cell> availableCell = getAvailableCell(prevCell);
        System.out.println("canMove: " + availableCell.contains(nextCell));
        return availableCell.contains(nextCell);
    }

    public void moveTo(Cell prevCell, Cell nextCell) {
        if (canMoveTo(prevCell, nextCell)) {
            setPosition(nextCell);
        }
    }

    @Override
    public boolean isOnField(int x, int y) {
        Cell field = new Cell(x, y);
        return this.currentCell != null && this.currentCell.equals(field);
    }


    @Override
    public boolean isCaptured() {
        return this.currentCell != null;
    }

    @Override
    public void setCell(Cell cell) {
        this.currentCell = cell;
    }

    @Override
    public Cell getCell() {
        return this.currentCell;
    }

    @Override
    public FigureColor getColor() {
        return this.color;
    }

    @Override
    public FigureType getType() {
        return this.type;
    }

    public char getSymbol() {
        if (this.color == FigureColor.WHITE) {
            return 'R';
        } else {
            return 'r';
        }
    }
}