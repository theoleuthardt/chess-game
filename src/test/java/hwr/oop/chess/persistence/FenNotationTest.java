package hwr.oop.chess.persistence;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.figures.FigureType;
import hwr.oop.chess.cli.CLIAdapter;
import hwr.oop.chess.cli.InvalidUserInputException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static hwr.oop.chess.persistence.FenNotation.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import static org.assertj.core.api.Assertions.assertThat;

class FenNotationTest {
  // @Test
  void testPlaceFigureFromFEN() {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    Board board = (new Board(new CLIAdapter(byteArrayOutputStream)));
    String fen = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2";
    placeFigureFromFEN(board, fen);
    board.printBoard();
    assertThat(byteArrayOutputStream.toString())
        .containsSequence(
            "r n b q k b n r",
            "p p - p p p p p",
            "- - - - - - - -",
            "- - p - - - - -",
            "- - - - P - - -",
            "- - - - - N - -",
            "P P P P - P P P",
            "R N B Q K B - R");
  }

  @Test
  void testInitialFigureFromFEN() {
    Board board = new Board(false);
    String fenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    placeFigureFromFEN(board, fenString);
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
    assertThat(charToFigureType('b').type()).isEqualTo(FigureType.BISHOP);
    assertThat(charToFigureType('k').type()).isEqualTo(FigureType.KING);
    assertThat(charToFigureType('n').type()).isEqualTo(FigureType.KNIGHT);
    assertThat(charToFigureType('p').type()).isEqualTo(FigureType.PAWN);
    assertThat(charToFigureType('q').type()).isEqualTo(FigureType.QUEEN);
    assertThat(charToFigureType('r').type()).isEqualTo(FigureType.ROOK);
    assertThat(charToFigureType('B').type()).isEqualTo(FigureType.BISHOP);
    assertThat(charToFigureType('K').type()).isEqualTo(FigureType.KING);
    assertThat(charToFigureType('N').type()).isEqualTo(FigureType.KNIGHT);
    assertThat(charToFigureType('P').type()).isEqualTo(FigureType.PAWN);
    assertThat(charToFigureType('Q').type()).isEqualTo(FigureType.QUEEN);
    assertThat(charToFigureType('R').type()).isEqualTo(FigureType.ROOK);
    assertThatThrownBy(() -> charToFigureType('x'))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid char for figure type!");
  }

  @Test
  void testGenerateFENFromBoard() {
    Board board = (new Board(new CLIAdapter(System.out)));
    String fen = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 0 0";
    placeFigureFromFEN(board, fen);
    board.printBoard();
    String generatedFEN = generateFENFromBoard(board);
    assertThat(generatedFEN).isEqualTo(fen);
  }

  @Test
  void testGenerateFENInitialState() {
    Board board = (new Board(new CLIAdapter(System.out)));
    String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 0";
    placeFigureFromFEN(board, fen);
    String generatedFEN = generateFENFromBoard(board);
    assertThat(generatedFEN).isEqualTo(fen);
  }

  @Test
  void testIsCharValid() {
    assertThat(isCharValid('a')).isFalse();
    assertThat(isCharValid('b')).isTrue(); // Bishop
    assertThat(isCharValid('c')).isFalse();
    assertThat(isCharValid('d')).isFalse();
  }
}
