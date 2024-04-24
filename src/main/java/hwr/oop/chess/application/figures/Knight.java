package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;

import java.util.logging.Logger;

import java.util.ArrayList;

public class Knight implements Figure {
    Logger logger = Logger.getLogger(getClass().getName());
    private static final FigureType type = FigureType.KNIGHT;
    private final FigureColor color;

    public Knight(FigureColor color) {
        this.color = color;
    }

    public ArrayList<Cell> getAvailableCells(Cell currentCell) {
        ArrayList<Cell> list = new ArrayList<>();

        list.add(currentCell.topCell().topLeftCell());
        list.add(currentCell.topCell().topRightCell());
        list.add(currentCell.bottomCell().bottomLeftCell());
        list.add(currentCell.bottomCell().bottomRightCell());
        list.add(currentCell.leftCell().topLeftCell());
        list.add(currentCell.leftCell().bottomLeftCell());
        list.add(currentCell.rightCell().topRightCell());
        list.add(currentCell.rightCell().bottomRightCell());

        // Remove cell if figure is mine
        for(Cell cell : list){
            if(cell.getFigure() != null && cell.getFigure().color() == currentCell.getFigure().color()){
                list.remove(cell);
            }
        }

        return list;
    }

    public boolean canMoveTo(Cell prevCell, Cell nextCell) {
        ArrayList<Cell> availableCell = getAvailableCells(prevCell);
        logger.info("canMove: " + availableCell.contains(nextCell));
        return availableCell.contains(nextCell);
    }

    public char symbol() {
        return color == FigureColor.WHITE ? 'N' : 'n';
    }

    public FigureColor color() {
        return color;
    }

    public FigureType type() {
        return type;
    }
}
