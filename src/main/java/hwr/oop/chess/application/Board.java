package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.Figure;

import java.util.ArrayList;

public class Board {
    private Board() {};

    private static ArrayList<Figure> figures = new ArrayList<>();

    public static void setFigures(ArrayList<Figure> figures) {
        Board.figures = figures;
    }

    public static Figure getFigureOnField(Position position) {
        for (Figure figure : figures) {
          if (figure.getPosition().isEqualTo(position)) {
            return figure;
          }
        }
        return null;
    }

    public static boolean isFigureOnField(Position position) {
        return Board.getFigureOnField(position) != null;
    }


    public static void moveFigure(Position from, Position to) {
        Figure figure = Board.getFigureOnField(from);
        if(figure == null) {
            throw new RuntimeException("On this field there is no Figure!");
        }

        if(!figure.canMoveTo(to)) {
            throw new RuntimeException("The Figure can't move there!");
        }

//        figure.moveTo(to);
    }
}
