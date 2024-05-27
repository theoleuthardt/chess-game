package hwr.oop.chess.persistence;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.figures.Figure;
import hwr.oop.chess.application.figures.FigureType;

import org.junit.jupiter.api.Test;

import static hwr.oop.chess.persistence.FenNotation.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

class FenNotationTest {
  @Test
  void testInitialFigureFromFEN() {
    Board board = new Board(false);
    String fenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    FenNotation.parseFENOnlyPiecePlacement(board, fenString);
    assertThat(board.findCell(1, 1).figure().type()).isEqualTo(FigureType.ROOK);
    assertThat(board.findCell(2, 1).figure().type()).isEqualTo(FigureType.KNIGHT);
    assertThat(board.findCell(3, 1).figure().type()).isEqualTo(FigureType.BISHOP);
    assertThat(board.findCell(4, 1).figure().type()).isEqualTo(FigureType.QUEEN);
    assertThat(board.findCell(5, 1).figure().type()).isEqualTo(FigureType.KING);
    assertThat(board.findCell(6, 1).figure().type()).isEqualTo(FigureType.BISHOP);
    assertThat(board.findCell(7, 1).figure().type()).isEqualTo(FigureType.KNIGHT);
    assertThat(board.findCell(8, 1).figure().type()).isEqualTo(FigureType.ROOK);
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
    String fen = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R";

    FenNotation.parseFENOnlyPiecePlacement(board, fen);
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
}
