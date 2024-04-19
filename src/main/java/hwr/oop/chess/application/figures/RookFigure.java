package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Position;

import java.util.Objects;

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

    @Override
    public boolean canMoveTo(Position newPosition) {
        // #Todo Write Castling
        Position oldPosition = this.currentPosition;
        Figure otherFigure = Board.getFigureOnField(newPosition);
        boolean isOtherFigureOnRoad = this.checkOtherFigureOnRoad(oldPosition, newPosition);

        if (!isOtherFigureOnRoad) {
            if (otherFigure == null
                    && oldPosition.x() == newPosition.x()
                    && oldPosition.y() == newPosition.y()) {
                return true;
            } else {
                return Objects.requireNonNull(otherFigure).getColor() == this.getColor();
            }
        } else {
            return false;
        }
    }

    @Override
    public void moveTo(int x, int y) {
        Position position = new Position(x, y);
        if (canMoveTo(position)) {
            setPosition(position);
        }
    }

    @Override
    public boolean isOnField(int x, int y) {
        Position field = new Position(x, y);
        return this.currentPosition != null && this.currentPosition.equals(field);
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

    private boolean checkOtherFigureOnRoad(Position currentPosition, Position newPosition) {
        int x = currentPosition.x();
        int y = currentPosition.y();

        if (x < newPosition.x()) {
            x++;
            for (; x < newPosition.x(); x++) {
                if (Board.isFigureOnField(new Position(x, y))) {
                    return true;
                }
            }
        } else {
            x--;
            for (; x > newPosition.x(); x--) {
                if (Board.isFigureOnField(new Position(x, y))) {
                    return true;
                }
            }
        }

        if (y < newPosition.y()) {
            y++;
            for (; y < newPosition.y(); y++) {
                if (Board.isFigureOnField(new Position(x, y))) {
                    return true;
                }
            }
        } else {
            y--;
            for (; y > newPosition.y(); y--) {
                if (Board.isFigureOnField(new Position(x, y))) {
                    return true;
                }
            }
        }

        return false;
    }
}