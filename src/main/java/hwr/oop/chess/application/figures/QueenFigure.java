package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Position;

import java.util.ArrayList;

public class QueenFigure implements Figure {
    private Position startPosition = null;
    private Position currentPosition = null;
    private static final FigureType type = FigureType.QUEEN;
    private final FigureColor color;

    public QueenFigure(FigureColor color, int x, int y) {
        Position position = new Position(x, y);
        this.startPosition = position;
        this.currentPosition = position;
        this.color = color;
    }

    public boolean canMoveTo(int x, int y) {
        Position to = new Position(x, y);
        Position from = this.currentPosition;

        // this move is not allowed as it does not obey the rules.
        return false;
    }

    public boolean isCaptured() {
        return this.currentPosition == null;
    }

    @Override
    public void setPosition(Position position) {

    }

    @Override
    public ArrayList<Position> getAvailablePosition(Position currentRook) {
        return null;
    }

    @Override
    public boolean canMoveTo(Position prevPosition, Position nextPosition) {
        return false;
    }

    @Override
    public boolean isOnField(int x, int y) {
        return false;
    }

    @Override
    public void moveTo(int x, int y) {

    }

    @Override
    public void moveTo(Position prevPosition, Position nextPosition) {

    }

    public Position getPosition() {
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
            return 'Q';
        } else {
            return 'q';
        }
    }
}
