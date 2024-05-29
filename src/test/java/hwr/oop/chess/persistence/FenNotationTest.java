package hwr.oop.chess.persistence;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.figures.Figure;
import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.application.figures.FigureType;

import hwr.oop.chess.application.figures.King;
import hwr.oop.chess.application.figures.Rook;
import java.util.List;
import org.junit.jupiter.api.Test;

import static hwr.oop.chess.persistence.FenNotation.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

class FenNotationTest {
  @Test
  void testInitialFigureFromFEN() {
    Board board = new Board(false);
    String fenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 0";
    FenNotation.parseFEN(board, fenString);
    assertThat(board.findCell("a1").figure().type()).isEqualTo(FigureType.ROOK);
    assertThat(board.findCell("b1").figure().type()).isEqualTo(FigureType.KNIGHT);
    assertThat(board.findCell("c1").figure().type()).isEqualTo(FigureType.BISHOP);
    assertThat(board.findCell("d1").figure().type()).isEqualTo(FigureType.QUEEN);
    assertThat(board.findCell("e1").figure().type()).isEqualTo(FigureType.KING);
    assertThat(board.findCell("f1").figure().type()).isEqualTo(FigureType.BISHOP);
    assertThat(board.findCell("g1").figure().type()).isEqualTo(FigureType.KNIGHT);
    assertThat(board.findCell("h1").figure().type()).isEqualTo(FigureType.ROOK);
  }

  @Test
  void testCharToFigureType() {
    assertThat(Figure.fromChar('b').type()).isEqualTo(FigureType.BISHOP);
    assertThat(Figure.fromChar('k').type()).isEqualTo(FigureType.KING);
    assertThat(Figure.fromChar('n').type()).isEqualTo(FigureType.KNIGHT);
    assertThat(Figure.fromChar('p').type()).isEqualTo(FigureType.PAWN);
    assertThat(Figure.fromChar('q').type()).isEqualTo(FigureType.QUEEN);
    assertThat(Figure.fromChar('r').type()).isEqualTo(FigureType.ROOK);
    assertThat(Figure.fromChar('B').type()).isEqualTo(FigureType.BISHOP);
    assertThat(Figure.fromChar('K').type()).isEqualTo(FigureType.KING);
    assertThat(Figure.fromChar('N').type()).isEqualTo(FigureType.KNIGHT);
    assertThat(Figure.fromChar('P').type()).isEqualTo(FigureType.PAWN);
    assertThat(Figure.fromChar('Q').type()).isEqualTo(FigureType.QUEEN);
    assertThat(Figure.fromChar('R').type()).isEqualTo(FigureType.ROOK);
    assertThatThrownBy(() -> Figure.fromChar('x'))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid char for figure type!");
  }

  @Test
  void testGenerateFENFromBoard() {
    Board board = new Board(false);
    String fen = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 0 5";

    FenNotation.parseFEN(board, fen);
    String generatedFEN = FenNotation.generateFen(board);
    assertThat(generatedFEN).startsWith(fen);
  }

  @Test
  void testGenerateFENInitialState() {
    Board board = new Board(false);
    String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 0";

    FenNotation.parseFEN(board, fen);
    String generatedFEN = FenNotation.generateFen(board);
    assertThat(generatedFEN).isEqualTo(fen);
  }

  @Test
  void testIsCharValid() {
    assertThat(isCharValid('a')).isFalse();
    assertThat(isCharValid('b')).isTrue(); // Bishop
    assertThat(isCharValid('c')).isFalse();
    assertThat(isCharValid('d')).isFalse();
  }

  @Test
  void testGenerateFenByCastling() {
    Board board = new Board(false);
    String initialStatus = "r3k2r/1pp1pppp/8/pB3b2/5P2/4p3/PPP3PP/R3K2R b - - 2 5";
    FenNotation.parseFEN(board, initialStatus);
    assertThat(FenNotation.generateFen(board)).isEqualTo(initialStatus);
  }

  @Test
  void testParseFEN() {
    Board board = new Board(false);
    String fenString = "r3k2r/1pp1pppp/8/pB3b2/5P2/4p3/PPP3PP/R3K2R b - - 2 5";

    List<String> parts = List.of(fenString.split(" "));
    String firstPart = parts.getFirst();
    assertThatThrownBy(() -> parseFEN(board, firstPart))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("This is an invalid FEN string, as it should have 6 parts!");

    parseFEN(board, fenString);
    assertThat(generateFen(board)).isEqualTo(fenString);
  }

  @Test
  void testParseCastling() {
    Board board = new Board(false);

    String disableCastling = "r3k2r/1pp1pppp/8/pB3b2/5P2/4p3/PPP3PP/R3K2R b - - 2 5";
    parseFEN(board, disableCastling);
    for (String position : List.of("a1", "a8", "h1", "h8")) {
      assertThat(((Rook) board.findCell(position).figure()).hasMoved()).isTrue();
    }
    for (String position : List.of("e1", "e8")) {
      assertThat(((King) board.findCell(position).figure()).hasMoved()).isTrue();
    }
    assertThat(generateFen(board)).isEqualTo(disableCastling);
  }

  @Test
  void testAvailableCastling() {
    Board board = new Board(false);

    String availableCastling = "r3k2r/1pp1pppp/8/pB3b2/5P2/4p3/PPP3PP/R3K2R b KQkq - 2 5";
    parseFEN(board, availableCastling);
    for (String position : List.of("a1", "a8", "h1", "h8")) {
      assertThat(((Rook) board.findCell(position).figure()).hasMoved()).isFalse();
    }
    for (String position : List.of("e1", "e8")) {
      assertThat(((King) board.findCell(position).figure()).hasMoved()).isFalse();
    }
    assertThat(generateFen(board)).isEqualTo(availableCastling);
  }

  @Test
  void testAvailableQueenCastling() {
    Board board = new Board(false);
    String availableQueenCastling = "r3k2r/1pp1pppp/8/p1B2b2/5P2/4p3/PPP3PP/R3K2R b Qq - 2 5";
    parseFEN(board, availableQueenCastling);
    for (String position : List.of("e1", "e8")) {
      assertThat(((King) board.findCell(position).figure()).hasMoved()).isFalse();
    }
    for (String position : List.of("a1", "a8")) {
      assertThat(((Rook) board.findCell(position).figure()).hasMoved()).isFalse();
    }
    for (String position : List.of("h1", "h8")) {
      assertThat(((Rook) board.findCell(position).figure()).hasMoved()).isTrue();
    }
    assertThat(generateFen(board)).isEqualTo(availableQueenCastling);
  }

  @Test
  void testAvailableKingCastling() {
    Board board = new Board(false);
    String availableQueenCastling = "r3k2r/1pp1pppp/8/p1B2b2/5P2/4p3/PPP3PP/R3K2R b Kk - 2 5";
    parseFEN(board, availableQueenCastling);
    for (String position : List.of("e1", "e8")) {
      assertThat(((King) board.findCell(position).figure()).hasMoved()).isFalse();
    }
    for (String position : List.of("a1", "a8")) {
      assertThat(((Rook) board.findCell(position).figure()).hasMoved()).isTrue();
    }
    for (String position : List.of("h1", "h8")) {
      assertThat(((Rook) board.findCell(position).figure()).hasMoved()).isFalse();
    }
    assertThat(generateFen(board)).isEqualTo(availableQueenCastling);
  }

  @Test
  void testCastlingNoKingSideRook() {
    Board board = new Board(false);

    String noKingSideRook = "r3k3/1p2pppr/2p5/pB3b1p/5P2/4p2P/PPP3PR/R3K3 w Qq - 2 8";
    parseFEN(board, noKingSideRook);
    for (String position : List.of("e1", "e8")) {
      assertThat(((King) board.findCell(position).figure()).hasMoved()).isFalse();
    }
    for (String position : List.of("a1", "a8")) {
      assertThat(((Rook) board.findCell(position).figure()).hasMoved()).isFalse();
    }
    assertThat(generateFen(board)).isEqualTo(noKingSideRook);
  }

  @Test
  void testCastlingKingIsNotStartPosition() {
    Board board = new Board(false);
    String kingIsNotStartPosition = "r2k3r/1pp1pppp/8/pB3b2/5P2/4p3/PPP3PP/R2K3R w - - 2 10";
    parseFEN(board, kingIsNotStartPosition);
    for (FigureColor color : List.of(FigureColor.WHITE, FigureColor.BLACK)) {
      assertThat(((King) board.findKing(color).figure()).hasMoved()).isTrue();
    }
    assertThat(generateFen(board)).isEqualTo(kingIsNotStartPosition);
  }

  @Test
  void testInValidFenString() {
    Board board = new Board(false);
    String whiteKingIsNotStartPosition =
        "r3k2r/1pp1pppp/8/pB3b2/5P2/4p3/PPP3PP/R2K3R w KkQq - 2 10";
    assertThatThrownBy(() -> parseFEN(board, whiteKingIsNotStartPosition))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Cannot load position because it is invalid.");

    String blackKingIsNotStartPosition =
        "r2k3r/1pp1pppp/8/pB3b2/5P2/4p3/PPP3PP/R3K2R w KkQq - 2 10";
    assertThatThrownBy(() -> parseFEN(board, blackKingIsNotStartPosition))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Cannot load position because it is invalid.");
  }
}
