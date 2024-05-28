package hwr.oop.chess.persistence;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.figures.*;

import java.util.*;

public class FenNotation {
  private final Board board;
  private FigureColor turn;
  private int halfMove;
  private int fullMove;

  private FenNotation(Board board) {
    this.board = board;
  }

  public static void parseFEN(Board board, String fenString) {
    List<String> parts = List.of(fenString.split(" "));
    if (parts.size() != 6) {
      throw new IllegalArgumentException(
          "This is an invalid FEN string, as it should have 6 parts!");
    }

    FenNotation fen = new FenNotation(board);
    fen.parsePiecePlacement(parts.getFirst());
    fen.parseTurn(parts.get(1));
    fen.parseCastlingForRook(parts.get(2));
    fen.parseCastlingForKing(parts.get(2));
    fen.parseEnPassant(parts.get(3));
    fen.parseHalfMove(parts.get(4));
    fen.parseFullMove(parts.getLast());

    board.initializeWith(fen.turn, fen.halfMove, fen.fullMove);
  }

  public static String generateFen(Board board) {
    FenNotation fen = new FenNotation(board);
    StringJoiner joiner = new StringJoiner(" ");

    joiner.add(fen.generatePiecePlacement());
    joiner.add(fen.generateTurn());
    joiner.add(fen.generateCastling());
    joiner.add(fen.generateEnPassant());
    joiner.add(fen.generateHalfMove());
    joiner.add(fen.generateFullMove());

    return joiner.toString();
  }

  private void parsePiecePlacement(String pieces) {
    List<Cell> allCells = board.allCells();
    List<String> rows = List.of(pieces.split("/"));
    for (String row : rows) {
      for (char c : row.toCharArray()) {
        if (Character.isDigit(c)) {
          int emptySpaces = Character.getNumericValue(c);
          for (int i = 0; i < emptySpaces; i++) {
            Cell cell = allCells.removeFirst();
            cell.setFigure(null);
          }
          continue;
        }

        Cell cell = allCells.removeFirst();
        cell.setFigure(Figure.fromChar(c));
      }
    }
  }

  private String generatePiecePlacement() {
    StringJoiner rows = new StringJoiner("/");
    StringBuilder row = new StringBuilder();
    int emptyCount = 0;

    List<Cell> allCells = board.allCells();
    // Sort only Y position
    allCells.sort(
        (c1, c2) -> {
          if (c1.y() != c2.y()) {
            return Integer.compare(c1.y(), c2.y());
          }
         return 0;
        });

    for (Cell cell : allCells) {
      if (cell.isFree()) {
        emptyCount++;
      }

      if (emptyCount > 0 && (cell.isOccupied() || !cell.hasRightCell())) {
        row.append(emptyCount);
        emptyCount = 0;
      }

      if (cell.isOccupied()) {
        row.append(cell.figure().symbol());
      }

      if (!cell.hasRightCell()) {
        rows.add(row.toString());
        row.setLength(0);
      }
    }
    return String.join("/", Arrays.asList(rows.toString().split("/")).reversed());
  }

  private void parseTurn(String turn) {
    this.turn = turn.charAt(0) == 'w' ? FigureColor.WHITE : FigureColor.BLACK;
  }

  private String generateTurn() {
    return board.turn().equals(FigureColor.WHITE) ? "w" : "b";
  }

  private void parseCastlingForRook(String castling) {
    for (char c : List.of('Q', 'K', 'q', 'k')) {
      if (castling.indexOf(c) > -1) {
        continue;
      }
      FigureColor color = Character.isUpperCase(c) ? FigureColor.WHITE : FigureColor.BLACK;
      int x = Character.toLowerCase(c) == 'q' ? 1 : 8;
      int y = Character.isLowerCase(c) ? 8 : 1;

      Cell rookCell = board.findCell(x, y);
      if (rookCell.isOccupiedBy(color, FigureType.ROOK)) {
        ((Rook) rookCell.figure()).figureMoved();
      }
    }
  }

  private void parseCastlingForKing(String castling) {
    if (!castling.contains("Q") && !castling.contains("K")) {
      Cell kingCell = board.findKing(FigureColor.WHITE);
      ((King) kingCell.figure()).figureMoved();
    }

    if (!castling.contains("q") && !castling.contains("k")) {
      Cell kingCell = board.findKing(FigureColor.BLACK);
      ((King) kingCell.figure()).figureMoved();
    }

    if (this.isKingNotOnStartField(FigureColor.WHITE) && (castling.contains("K") || castling.contains("Q"))){
      throw new IllegalArgumentException(
          "Cannot load position because it is invalid.");
    }

    if (this.isKingNotOnStartField(FigureColor.BLACK) && (castling.contains("k") || castling.contains("q"))){
      throw new IllegalArgumentException(
          "Cannot load position because it is invalid.");
    }
  }

  private String generateCastling() {
    StringBuilder castling = new StringBuilder();
    for (char c : List.of('K', 'Q', 'k', 'q')) {
      FigureColor color = Character.isUpperCase(c) ? FigureColor.WHITE : FigureColor.BLACK;
      Cell kingCell = board.findKing(color);
      King king = (King) kingCell.figure();
      if (king.hasMoved()) {
        continue;
      }

      int x = Character.toLowerCase(c) == 'q' ? 1 : 8;
      int y = Character.isLowerCase(c) ? 8 : 1;
      Cell rookCell = board.findCell(x, y);
      if (rookCell.isOccupiedBy(color, FigureType.ROOK) && !((Rook) rookCell.figure()).hasMoved()) {
        castling.append(c);
      }
    }
    return castling.isEmpty() ? "-" : castling.toString();
  }

  private boolean isKingNotOnStartField(FigureColor color) {
    Cell kingCell = board.findKing(color);

    if (color == FigureColor.WHITE) {
      return kingCell.x() != 5 || kingCell.y() != 1;
    } else {
      return kingCell.x() != 5 || kingCell.y() != 8;
    }
  }

  private void parseEnPassant(String enPassantStr) {
    if (!enPassantStr.equals("-")) {
      board.findCell(enPassantStr).setIsEnPassant(true);
    }
  }

  private String generateEnPassant() {
    for (Cell cell : board.allCells()) {
      if (cell.isEnPassant()) {
        return cell.toCoordinates().toLowerCase();
      }
    }
    return "-";
  }

  private void parseHalfMove(String halfMove) {
    this.halfMove = Integer.parseInt(halfMove);
  }

  private String generateHalfMove() {
    return String.valueOf(board.halfMove());
  }

  private void parseFullMove(String fullMove) {
    this.fullMove = Integer.parseInt(fullMove);
  }

  private String generateFullMove() {
    return String.valueOf(board.fullMove());
  }

  public static boolean isCharValid(char c) {
    return switch (Character.toLowerCase(c)) {
      case 'b', 'k', 'n', 'p', 'q', 'r' -> true;
      default -> false;
    };
  }
}
