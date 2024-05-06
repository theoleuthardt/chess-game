package hwr.oop.chess.persistence;

// https://www.chess.com/de/terms/forsyth-edwards-notation-fen

// pawn = "P", knight = "N", bishop = "B", rook = "R", queen = "Q" and king = "K"

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.figures.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FenNotation {
    private String cell;
    private String activeColor;
    private String castling;
    private String enPassant;
    private int halfmoveClock;
    private int fullmoveNumber;

    public FenNotation(
            String cell,
            String activeColor,
            String castling,
            String enPassant,
            int halfmoveClock,
            int fullmoveNumber) {
        this.cell = cell;
        this.activeColor = activeColor;
        this.castling = castling;
        this.enPassant = enPassant;
        this.halfmoveClock = halfmoveClock;
        this.fullmoveNumber = fullmoveNumber;
    }

    // getter setter
    public String cell() {
        return this.cell;
    }

    public static void placeFigureFromFEN(Board board, String fenString) {
        List<String> parts = new ArrayList<>(Arrays.asList(fenString.split(" ")));
        List<String> rows = new ArrayList<>(Arrays.asList(parts.get(0).split("/")));

        int y = 1;
        int x = 1;
        for (String rowString : rows) {
            while(x < 9){
                for (char c : rowString.toCharArray()) {
                    if (Character.isDigit(c)) {
                        int emptySpaces = Character.getNumericValue(c);
                        x = x + emptySpaces;
                    } else {
                        board.findCell(x,y).setFigure(charToFigureType(c));
                        x++;
                    }
                }
            }
            y++;
            x = 1;
        }
    }

    public static Figure charToFigureType(char ch) {
        Figure figure = null;
        switch (ch) {
            case 'b' -> figure = new Bishop(FigureColor.BLACK);
            case 'k' -> figure = new King(FigureColor.BLACK);
            case 'n' -> figure = new Knight(FigureColor.BLACK);
            case 'p' -> figure = new Pawn(FigureColor.BLACK);
            case 'q' -> figure = new Queen(FigureColor.BLACK);
            case 'r' -> figure = new Rook(FigureColor.BLACK);
            case 'B' -> figure = new Bishop(FigureColor.WHITE);
            case 'K' -> figure = new King(FigureColor.WHITE);
            case 'N' -> figure = new Knight(FigureColor.WHITE);
            case 'P' -> figure = new Pawn(FigureColor.WHITE);
            case 'Q' -> figure = new Queen(FigureColor.WHITE);
            case 'R' -> figure = new Rook(FigureColor.WHITE);
            default -> figure = null;
        }
        return figure;
    }

    public void setPosition(String cell) {
        this.cell = cell;
    }

    public String getActiveColor() {
        return this.activeColor;
    }

    public void setActiveColor(String activeColor) {
        this.activeColor = activeColor;
    }

    public String getCastling() {
        return this.castling;
    }

    public void setCastling(String castling) {
        this.castling = castling;
    }

    public String getEnPassant() {
        return this.enPassant;
    }

    public void setEnPassant(String enPassant) {
        this.enPassant = enPassant;
    }

    public int getHalfmoveClock() {
        return this.halfmoveClock;
    }

    public void setHalfmoveClock(int halfmoveClock) {
        this.halfmoveClock = halfmoveClock;
    }

    public int getFullmoveNumber() {
        return this.fullmoveNumber;
    }

    public void setFullmoveNumber(int fullmoveNumber) {
        this.fullmoveNumber = fullmoveNumber;
    }
}
