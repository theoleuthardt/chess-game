package hwr.oop.chess.persistence;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.figures.FigureType;
import hwr.oop.chess.cli.CLIAdapter;
import org.junit.jupiter.api.Test;

import static hwr.oop.chess.persistence.FenNotation.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import static org.assertj.core.api.Assertions.assertThat;

class FenNotationTest {
//  @Test
  void testPlaceFigureFromFEN() {
    Board board = (new Board(new CLIAdapter(System.out)));
    String fen = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2";
    placeFigureFromFEN(board, fen);
    board.printBoard();
  }

  @Test
  void testInitialFigureFromFEN() {
    Board board = new Board(false);
    String fenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    placeFigureFromFEN(board, fenString);
    assertEquals(FigureType.ROOK, board.findCell(1, 1).figure().type());
    assertEquals(FigureType.KNIGHT, board.findCell(2, 1).figure().type());
    assertEquals(FigureType.BISHOP, board.findCell(3, 1).figure().type());
    assertEquals(FigureType.QUEEN, board.findCell(4, 1).figure().type());
    assertEquals(FigureType.KING, board.findCell(5, 1).figure().type());
    assertEquals(FigureType.BISHOP, board.findCell(6, 1).figure().type());
    assertEquals(FigureType.KNIGHT, board.findCell(7, 1).figure().type());
    assertEquals(FigureType.ROOK, board.findCell(8, 1).figure().type());
  }

  @Test
  void testCharToFigureType() {
    assertEquals(FigureType.BISHOP, charToFigureType('b').type());
    assertEquals(FigureType.KING, charToFigureType('k').type());
    assertEquals(FigureType.KNIGHT, charToFigureType('n').type());
    assertEquals(FigureType.PAWN, charToFigureType('p').type());
    assertEquals(FigureType.QUEEN, charToFigureType('q').type());
    assertEquals(FigureType.ROOK, charToFigureType('r').type());
    assertEquals(FigureType.BISHOP, charToFigureType('B').type());
    assertEquals(FigureType.KING, charToFigureType('K').type());
    assertEquals(FigureType.KNIGHT, charToFigureType('N').type());
    assertEquals(FigureType.PAWN, charToFigureType('P').type());
    assertEquals(FigureType.QUEEN, charToFigureType('Q').type());
    assertEquals(FigureType.ROOK, charToFigureType('R').type());
    assertNull(charToFigureType('x'));
    assertNull(charToFigureType('y'));
    assertNull(charToFigureType('z'));
  }

  //  @Test
  //  void testGenerateFENFromBoard() {
  //    Board board = (new Board(new CLIAdapter(System.out)));
  //    String fen = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R";
  //    placeFigureFromFEN(board, fen);
  //    board.printBoard();
  //    String generatedFEN = generateFENFromBoard(board);
  //    assertThat(generatedFEN).isEqualTo(fen);
  //  }
  //
  //  @Test
  //  void testGenerateFENInitialState() {
  //    Board board = (new Board(new CLIAdapter(System.out)));
  //    String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
  //    placeFigureFromFEN(board, fen);
  //    String generatedFEN = generateFENFromBoard(board);
  //    assertThat(generatedFEN).isEqualTo(fen);
  //  }

  @Test
  void testIsCharValid() {
    assertThat(isCharValid('a')).isFalse();
    assertThat(isCharValid('b')).isTrue(); // Bishop
    assertThat(isCharValid('c')).isFalse();
    assertThat(isCharValid('d')).isFalse();
  }
}
