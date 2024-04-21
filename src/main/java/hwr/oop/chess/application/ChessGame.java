package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.Figure;
import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.application.figures.PawnFigure;
import hwr.oop.chess.application.figures.RookFigure;
import hwr.oop.chess.application.figures.QueenFigure;
import hwr.oop.chess.application.figures.KingFigure;
import hwr.oop.chess.application.figures.KnightFigure;
import hwr.oop.chess.application.figures.BishopFigure;

import java.util.ArrayList;

public class ChessGame {
    private Board board;

    public ChessGame() {
        board = new Board();
        board.printBoard();
    }

//    void startNewGame() {
//        ArrayList<Figure> list = new ArrayList<>();
//
//
//        // White Player
//        list.add(new RookFigure  (FigureColor.WHITE, 1, 1));
//        list.add(new KnightFigure(FigureColor.WHITE, 2, 1));
//        list.add(new BishopFigure(FigureColor.WHITE, 3, 1));
//        list.add(new QueenFigure (FigureColor.WHITE, 4, 1));
//        list.add(new KingFigure  (FigureColor.WHITE, 5, 1));
//        list.add(new BishopFigure(FigureColor.WHITE, 6, 1));
//        list.add(new KnightFigure(FigureColor.WHITE, 7, 1));
//        list.add(new RookFigure  (FigureColor.WHITE, 8, 1));
//
//        for(int x = 1; x <= 8; x++) {
//            list.add(new PawnFigure(FigureColor.WHITE, x, 2));
//        }
//
//        // Black Player
//        list.add(new RookFigure  (FigureColor.BLACK, 1, 8));
//        list.add(new KnightFigure(FigureColor.BLACK, 2, 8));
//        list.add(new BishopFigure(FigureColor.BLACK, 3, 8));
//        list.add(new QueenFigure (FigureColor.BLACK, 4, 8));
//        list.add(new KingFigure  (FigureColor.BLACK, 5, 8));
//        list.add(new BishopFigure(FigureColor.BLACK, 6, 8));
//        list.add(new KnightFigure(FigureColor.BLACK, 7, 8));
//        list.add(new RookFigure  (FigureColor.BLACK, 8, 8));
//
//        for(int x = 1; x <= 8; x++) {
//            list.add(new PawnFigure(FigureColor.BLACK, x, 7));
//        }
//
//        Board.setFigures(list);
//
//    }
}
