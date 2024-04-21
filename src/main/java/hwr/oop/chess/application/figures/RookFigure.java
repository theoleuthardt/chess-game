package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Position;

import java.util.ArrayList;

public class RookFigure implements Figure {
    Position startPosition = null;
    Position currentPosition = null;
    FigureType type = null;
    FigureColor color = null;

    public RookFigure(FigureColor color, int x, int y) {
        Position position = new Position(x, y);
        this.type = FigureType.ROOK;
        this.color = color;
        this.startPosition = position;
        this.currentPosition = position;
    }

    public ArrayList<Position> getAvailablePosition(Position currentRook) {
        ArrayList<Position> list = new ArrayList<>();

        // Check above
        Position current = currentRook.getTopPosition();

        //If there is no figure or if it's a different color, the piece can move
        while (current != null && current.getFigure() == null) {
            list.add(current);
            current = current.getTopPosition();
        }
        if (current != null && current.getFigure() != null && current.getFigure().getColor() != currentRook.getFigure().getColor()) {
            list.add(current);
        }

        // Check below
        current = currentRook.getBottomPosition();
        while (current != null && current.getFigure() == null) {
            list.add(current);
            current = current.getBottomPosition();
        }
        if (current != null && current.getFigure() != null && current.getFigure().getColor() != currentRook.getFigure().getColor()) {
            list.add(current);
        }

        // Check the right
        current = currentRook.getRightPosition();
        while (current != null && current.getFigure() == null) {
            list.add(current);
            current = current.getRightPosition();
        }
        if (current != null && current.getFigure() != null && current.getFigure().getColor() != currentRook.getFigure().getColor()) {
            list.add(current);
        }

        // Check the left
        current = currentRook.getLeftPosition();
        while (current != null && current.getFigure() == null) {
            list.add(current);
            current = current.getLeftPosition();
        }
        if (current != null && current.getFigure() != null && current.getFigure().getColor() != currentRook.getFigure().getColor()) {
            list.add(current);
        }
        System.out.println("availablePosition" + list.toArray().length);
        return list;
    }

    public boolean canMoveTo(Position prevPosition, Position nextPosition) {
        ArrayList<Position> availablePosition = getAvailablePosition(prevPosition);
        System.out.println("canMove: " + availablePosition.contains(nextPosition));
        return availablePosition.contains(nextPosition);
    }

    public void moveTo(Position prevPosition, Position nextPosition) {
        if (canMoveTo(prevPosition, nextPosition)) {
            setPosition(nextPosition);
        }
    }

    @Override
    public boolean isOnField(int x, int y) {
        Position field = new Position(x, y);
        return this.currentPosition != null && this.currentPosition.equals(field);
    }

    @Override
    public void moveTo(int x, int y) {
        // TODO Delete
    }

    @Override
    public boolean isCaptured() {
        return this.currentPosition != null;
    }

    @Override
    public void setPosition(Position position) {
        this.currentPosition = position;
    }

    @Override
    public Position getPosition() {
        return this.currentPosition;
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