package hwr.oop.chess.persistence;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.figures.Figure;
import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.application.figures.FigureType;
import hwr.oop.chess.application.figures.King;
import hwr.oop.chess.application.figures.Rook;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static hwr.oop.chess.persistence.FenNotation.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

class FenNotationTest {
  @Test
  void testInitialFigureFromFEN() {
    Board board = new Board(false);
    String fenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 0";
    parseFEN(board, fenString);

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

  @ParameterizedTest
  @ValueSource(
      strings = {
        "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 0 5",
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 0"
      })
  void parsingFenAndGeneratingItFromBoard_shouldNotChangeFenString(String initialFen) {
    Board board = new Board(false);
    parseFEN(board, initialFen);
    assertThat(generateFen(board)).isEqualTo(initialFen);
  }

  @Test
  void testGenerateFenByCastling() {
    Board board = new Board(false);
    String initialStatus = "r3k2r/1pp1pppp/8/pB3b2/5P2/4p3/PPP3PP/R3K2R b - - 2 5";
    parseFEN(board, initialStatus);
    assertThat(generateFen(board)).isEqualTo(initialStatus);
  }

  @Test
  void generateFenFailsWithoutEnoughParts() {
    Board board = new Board(false);
    String invalidFenString = "r3k2r/1pp1pppp/8/pB3b2/5P2/4p3/PPP3PP/R3K2R";
    assertThatThrownBy(() -> parseFEN(board, invalidFenString))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("This is an invalid FEN string!");
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

  @ParameterizedTest
  @ValueSource(
      strings = {
        "r3k2r/1pp1pppp/8/pB3b2/5P2/4p3/PPP3PP/R2K3R w KkQq - 2 10", // !whiteKingAtInitial
        "r2k3r/1pp1pppp/8/pB3b2/5P2/4p3/PPP3PP/R3K2R w KkQq - 2 10", // !blackKingAtInitial
        "r3k3/1pp1pppp/8/pB3b2/5P2/4p3/PPP3PP/R3K2R w KkQq - 2 10", // !blackRookAtInitialKingSide
        "4k2r/1pp1pppp/8/pB3b2/5P2/4p3/PPP3PP/R3K2R w KkQq - 2 10", // !blackRookAtInitialQueenSide
        "r3k2r/1pp1pppp/8/pB3b2/5P2/4p3/PPP3PP/R3K3 w KkQq - 2 10", // !whiteRookAtInitialKingSide
        "r3k2r/1pp1pppp/8/pB3b2/5P2/4p3/PPP3PP/4K2R w KkQq - 2 10", // !whiteRookAtInitialQueenSide
        "r3k2r/1pp1pppp/8/pB3b2/5P2/4p3/PPP3PP/4K2R w abc - 2 10", // wrong castling rights
      })
  void testInvalidCastlingFEN(String fen) {
    Board board = new Board(false);
    assertThatThrownBy(() -> parseFEN(board, fen))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("This is an invalid FEN string!");
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
        "8/8/8/8/8/8/8/8 w - - 0 1",
      })
  void testValidFEN(String fen) {
    assertThat(isValidFEN(fen)).isTrue();
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "rnbqkbnr/pppppppp/8/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0",
        "8/8/8/8/8/8/8/8 w - - 0",
        "rnbqkbnr/pppppppp/8/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - - 1",
        "r1bq1rk1/p5p1/1p3npp/2pPp3/2P1P3/2PBB3/R2Q1RK1 w - - 2 15",
        "r1bq1rk1/p5p1/1p3npp/2pPp3/2P1P3/3PBB3/P5PP/R2Q1RK1 w - - 2 15",
        "rnbqkbn3/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
        "8/1r7/8/8/8/8/8/8 w - - 0 1",
      })
  void testInvalidPartFEN(String fen) {
    assertThat(isValidFEN(fen)).isFalse();
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "rnbqkbnr/ppp1pppp/8/8/3pP3/8/PPP2PPP/RNBQKBNR w KQkq a6 0 2",
        "rnbqkbnr/ppp1pppp/8/8/3pP3/8/PPP2PPP/RNBQKBNR w KQkq b6 0 2",
        "rnbqkbnr/ppp1pppp/8/8/3pP3/8/PPP2PPP/RNBQKBNR w KQkq c6 0 2",
        "rnbqkbnr/ppp1pppp/8/8/3pP3/8/PPP2PPP/RNBQKBNR b KQkq d3 0 2",
        "rnbqkbnr/ppp1pppp/8/8/3pP3/8/PPP2PPP/RNBQKBNR b KQkq f3 0 2",
        "rnbqkbnr/ppp1pppp/8/8/3pP3/8/PPP2PPP/RNBQKBNR b KQkq g3 0 2",
      })
  void testInValidEnPassantFEN(String fen) {
    assertThat(isValidFEN(fen)).isFalse();
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
          "rnbqkbnr/p1pppppp/8/1p6/4P3/8/PPP2PPP/RNBQKBNR w KQkq b6 0 2",
          "rnbqkbnr/p2ppppp/8/2p5/4P3/8/PPP2PPP/RNBQKBNR w KQkq c6 0 2",
          "rnbqkbnr/pp2pppp/8/3p4/4P3/8/PPP2PPP/RNBQKBNR w KQkq d6 0 2",
          "rnbqkbnr/ppp1pppp/8/8/3pP3/8/PPP2PPP/RNBQKBNR b KQkq e3 0 2",
          "rnbqkbnr/ppp1pppp/8/8/3p1P2/8/PPPP2PP/RNBQKBNR b KQkq f3 0 2",
          "rnbqkbnr/ppp1pppp/8/8/3p2P1/8/PPP2PPP/RNBQKBNR b KQkq g3 0 2",
      })
  void testValidEnPassantFEN(String fen) {
    assertThat(isValidFEN(fen)).isTrue();
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 2",
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 5 1",
      })
  void testExtractFenKeyParts(String fen) {
    assertThat(extractFenKeyParts(fen))
        .isEqualTo("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR KQkq -");
  }
}
