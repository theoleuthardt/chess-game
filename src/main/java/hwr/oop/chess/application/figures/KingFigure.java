package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Position;

public class KingFigure implements Figure {
    private Position currentPosition = null;
    private static final FigureType type = FigureType.KING;
    private final FigureColor color;

    public KingFigure(FigureColor color) {
        //  Position position = new Position(x,y);
        //  this.startPosition = position;
        //  this.currentPosition = position;
        this.color = color;
    }


    public boolean canMoveTo(Position to) {
        Position from = this.currentPosition;

        // this move is not allowed as it does not obey the rules.
        return false;
    }

    public boolean isOnField(Position field) {
        return this.currentPosition.isEqualTo(field);
    }

    public boolean isCaptured() {
        return this.currentPosition == null;
    }

    public void moveTo(Position position) {
        if(canMoveTo(position)) {
            this.currentPosition = position;
        }
    }

    public Position position() {
        return this.currentPosition;
    }

    public FigureColor color() {
        return this.color;
    }

    public FigureType type() {
        return type;
    }

    public char getSymbol(){
        if(this.color == FigureColor.WHITE){
            return 'K';
        }else{
            return 'k';
        }
    }
}