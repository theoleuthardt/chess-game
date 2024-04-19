package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Position;

public interface Figure {


    boolean canMoveTo(Position position);

    boolean isOnField(int x, int y);
    // void capture();
    // boolean isCaptured();

    void moveTo(int x, int y);

    boolean isCaptured();

    void setPosition(Position position);

    char getSymbol();

    public Position getPosition();

    public FigureColor getColor();

    public FigureType getType();
}
