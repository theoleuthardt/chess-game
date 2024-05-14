package hwr.oop.chess.persistence;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.figures.*;

import java.util.List;

public class FenNotation {
  private FenNotation() {}

  public static void placeFigureFromFEN(Board board, String fenString) {
    int y = 8;
    int x = 1;

    for (char c : fenString.toCharArray()) {
      if (c == ' ') {
        break;
      }
      if (c == '/') {
        // Change Row
        y--;
        x = 1;
      } else {
        // Pass without figures
        if (Character.isDigit(c)) {
          int emptySpaces = Character.getNumericValue(c);
          x = x + emptySpaces;
        } else {
          // Place the figures
          board.findCell(x, y).setFigure(charToFigureType(c));
          x++;
        }
      }
    }
  }

  public static String generateFENFromBoard(Board board) {
    List<Cell> cells = board.allCells();
    StringBuilder fenString = new StringBuilder();

    int count = 0; // empty cells
    int x = 1;
    for (Cell cell : cells) {
      if (cell.figure() == null) {
        count++;
        if (x != 8) {
          x++;
        } else {
          // Add empty cells as number
          fenString.append(count);
          fenString.append('/');
          count = 0;
          x = 1;
        }
        continue;
      }
      // Cell has Figure
      if (count > 0) {
        // Add previous empty cells as number
        fenString.append(count);
        count = 0;
      }
      if (isCharValid(cell.figure().symbol())) {
        // Add Figure as symbol
        fenString.append(cell.figure().symbol());
      }
      if (x != 8) {
        x++;
      } else {
        fenString.append('/');
        x = 1;
      }
    }
    // Delete last '/'
    fenString.replace(fenString.length() - 1, fenString.length(), " ");

    // Add turn
    fenString.append(board.turn() == FigureColor.WHITE ? 'w' : 'b');
    fenString.append(" ");

    // Add castling
    if(board.canCastlingWhiteKing()){
      fenString.append('K');
    }
    if(board.canCastlingWhiteQueen()){
      fenString.append('Q');
    }
    if(board.canCastlingBlackKing()){
      fenString.append('k');
    }
    if(board.canCastlingBlackQueen()){
      fenString.append('q');
    }

    // Add rest
    fenString.append(" ");
    fenString.append(board.enPassant());
    fenString.append(" ");
    fenString.append(board.halfmoveClockBlack());
    fenString.append(" ");
    fenString.append(board.fullmoveNumber());

    return fenString.toString();
  }


  public static boolean isCharValid(char c) {
    c = Character.toLowerCase(c);
    return switch (c) {
      case 'b', 'k', 'n', 'p', 'q', 'r' -> true;
      default -> false;
    };
  }

  public static Figure charToFigureType(char ch) {
    return switch (ch) {
      case 'b' -> new Bishop(FigureColor.BLACK);
      case 'k' -> new King(FigureColor.BLACK);
      case 'n' -> new Knight(FigureColor.BLACK);
      case 'p' -> new Pawn(FigureColor.BLACK);
      case 'q' -> new Queen(FigureColor.BLACK);
      case 'r' -> new Rook(FigureColor.BLACK);
      case 'B' -> new Bishop(FigureColor.WHITE);
      case 'K' -> new King(FigureColor.WHITE);
      case 'N' -> new Knight(FigureColor.WHITE);
      case 'P' -> new Pawn(FigureColor.WHITE);
      case 'Q' -> new Queen(FigureColor.WHITE);
      case 'R' -> new Rook(FigureColor.WHITE);
      default -> throw new IllegalArgumentException("Invalid char for figure type!");
    };
  }
}
