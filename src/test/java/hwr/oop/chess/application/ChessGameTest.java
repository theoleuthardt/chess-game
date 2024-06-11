package hwr.oop.chess.application;

import static hwr.oop.chess.persistence.FenNotation.extractFenKeyParts;
import static hwr.oop.chess.persistence.FenNotation.generateFen;
import static org.assertj.core.api.Assertions.assertThat;

import hwr.oop.chess.application.figures.*;
import hwr.oop.chess.cli.CLIAdapter;
import hwr.oop.chess.persistence.FenNotation;
import hwr.oop.chess.persistence.NoPersistence;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import hwr.oop.chess.persistence.PortableGameNotation;
import org.junit.jupiter.api.Test;

class ChessGameTest {
  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private final CLIAdapter cli = new CLIAdapter(new PrintStream(outputStream), new NoPersistence());

  @Test
  void testThreeFoldRepetition() {
    cli.forGameId("123");
    cli.initializeGame(true);

    // Same states after #8, #12, #16
    moveFigureAndSave("e2", "e4"); // #1
    moveFigureAndSave("e7", "e5"); // #2
    moveFigureAndSave("g1", "f3"); // #3
    moveFigureAndSave("b8", "c6"); // #4
    moveFigureAndSave("b1", "c3"); // #5
    moveFigureAndSave("g8", "f6"); // #6
    moveFigureAndSave("f3", "g1"); // #7
    moveFigureAndSave("f6", "g8"); // #8
    String fen1 = extractFenKeyParts(generateFen(cli.game().board()));
    moveFigureAndSave("g1", "f3"); // #9
    moveFigureAndSave("g8", "f6"); // #10
    moveFigureAndSave("f3", "g1"); // #11
    moveFigureAndSave("f6", "g8"); // #12
    String fen2 = extractFenKeyParts(generateFen(cli.game().board()));
    moveFigureAndSave("c3", "b1"); // #13
    moveFigureAndSave("c6", "b8"); // #14
    moveFigureAndSave("b1", "c3"); // #15
    moveFigureAndSave("b8", "c6"); // #16
    String fen3 = extractFenKeyParts(generateFen(cli.game().board()));

    assertThat(fen1).isEqualTo(fen2).isEqualTo(fen3);
    assertThat(cli.game().isThreeFoldRepetition()).isTrue();
  }

  @Test
  void testGeneratePgn() {
    cli.forGameId("123");
    cli.initializeGame(true);

    moveFigureAndSave("d2", "d4"); // 1. d4
    moveFigureAndSave("g8", "f6"); // 1... nf6
    moveFigureAndSave("c2", "c4"); // 2. c4
    moveFigureAndSave("e7", "e6"); // 2... e6
    moveFigureAndSave("b1", "c3"); // 3. Nc3
    moveFigureAndSave("f8", "b4"); // 3... bb4
    moveFigureAndSave("g1", "f3"); // 4. Nf3
    moveFigureAndSave("c7", "c5"); // 4... c5
    moveFigureAndSave("e2", "e3"); // 5. e3
    moveFigureAndSave("b8", "c6"); // 5... nc6
    moveFigureAndSave("f1", "d3"); // 6. Bd3
    moveFigureAndSave("b4", "c3"); // 6... bxc3+
    moveFigureAndSave("b2", "c3"); // 7. bxc3
    moveFigureAndSave("d7", "d6"); // 7... d6
    moveFigureAndSave("e3", "e4"); // 8. e4
    moveFigureAndSave("e6", "e5"); // 8... e5
    moveFigureAndSave("d4", "d5"); // 9. d5
    moveFigureAndSave("c6", "e7"); // 9... ne7
    moveFigureAndSave("f3", "h4"); // 10. Nh4
    moveFigureAndSave("h7", "h6"); // 10... h6
    moveFigureAndSave("f2", "f4"); // 11. f4
    moveFigureAndSave("e7", "g6"); // 11... ng6
    moveFigureAndSave("h4", "g6"); // 12. Nxg6
    moveFigureAndSave("f7", "g6"); // 12... fxg6
    moveFigureAndSave("f4", "e5"); // 13. fxe5
    moveFigureAndSave("d6", "e5"); // 13... dxe5
    moveFigureAndSave("c1", "e3"); // 14. Be3
    moveFigureAndSave("b7", "b6"); // 14... b6
    moveFigureAndSave("e1", "g1"); // 15. O-O White King Castling
    moveFigureAndSave("e8", "g8"); // 15. O-O Black King Castling

    assertThat(cli.game().pgnHistoryOfMoves())
        .isEqualTo(
            "d4,Nf6,c4,e6,Nc3,Bb4,Nf3,c5,e3,Nc6,Bd3,Bxc3+,bxc3,d6,e4,e5,d5,Ne7,Nh4,h6,f4,Ng6,Nxg6,fxg6,fxe5,dxe5,Be3,b6,O-O,O-O");
    assertThat((new PortableGameNotation()).pgnFile(cli.game()))
        .isEqualTo(
            "1. d4 Nf6 2. c4 e6 3. Nc3 Bb4 4. Nf3 c5 5. e3 Nc6 6. Bd3 Bxc3+ 7. bxc3 d6 8. e4\ne5 9. d5 Ne7 10. Nh4 h6 11. f4 Ng6 12. Nxg6 fxg6 13. fxe5 dxe5 14. Be3 b6 15. O-O\nO-O *");
  }

  void moveFigureAndSave(String from, String to) {
    Board board = cli.game().board();
    cli.game().rememberAndPerformMove(board.findCell(from), board.findCell(to));
    cli.game().saveGame();
  }

  @Test
  void testPossibleRememberAndPerformPawnPromotion() {
    cli.forGameId("1");
    cli.initializeGame(true);
    Board board = cli.game().board();
    Pawn pawn = new Pawn(FigureColor.WHITE);
    Cell currentCell = board.findCell('a', 8);
    currentCell.setFigure(pawn);
    cli.game().rememberAndPerformPawnPromotion(currentCell, FigureType.QUEEN);
    assertThat(cli.game().algebraicNotation().toString()).hasToString("a8=Q");
  }

  @Test
  void testStoreWinnerWhitePGNHistory() {
    cli.forGameId("1");
    cli.initializeGame(true);
    cli.game().playerHasWon(EndType.CHECKMATE, FigureColor.WHITE);
    assertThat(cli.game().pgnHistory()).contains("1-0");
  }

  @Test
  void testStoreWinnerBlackPGNHistory() {
    cli.forGameId("1");
    cli.initializeGame(true);
    cli.game().playerHasWon(EndType.CHECKMATE, FigureColor.BLACK);
    assertThat(cli.game().pgnHistory()).contains("0-1");
  }

  @Test
  void testQueenCastlingAlgebraicNotation() {
    cli.forGameId("1");
    cli.initializeGame(true);
    Board board = cli.game().board();
    String queenCastling = "rnbqk1nr/pppp1ppp/8/4p3/8/8/PPPPPPPP/R3KBNR b KQkq - 1 1";
    FenNotation.parseFEN(board, queenCastling);
    moveFigureAndSave("a7", "a6");
    moveFigureAndSave("e1", "c1");
    assertThat(cli.game().algebraicNotation().toString()).contains("O-O-O");
  }

  @Test
  void testFileDisambiguation() {
    cli.forGameId("1");
    cli.initializeGame(true);
    Board board = cli.game().board();
    String file_disambiguation = "r1bqkbnr/pp1ppppp/8/2n5/4P3/5N2/PPP2PPP/RNBQKB1R w KQkq - 0 3";
    FenNotation.parseFEN(board, file_disambiguation);
    moveFigureAndSave("b1", "c3");
    assertThat(cli.game().algebraicNotation().toString())
        .hasToString("Nc3"); // TODO Nbc3 funktioniert nicht
  }

  @Test
  void testRankDisambiguation() {
    cli.forGameId("1");
    cli.initializeGame(true);
    Board board = cli.game().board();
    String rank_disambiguation = "rnbqk2r/ppppppbp/6p1/8/8/5N2/PPPPPPRP/RNBQKB1R w KQkq - 0 1";
    FenNotation.parseFEN(board, rank_disambiguation);
    moveFigureAndSave("g2", "g1");
    assertThat(cli.game().algebraicNotation().toString())
        .hasToString("Rg1"); // TODO Rgg1 funktioniert auch nicht
  }

  @Test
  void testRankAndFileDisambiguation() {
    cli.forGameId("1");
    cli.initializeGame(true);
    Board board = cli.game().board();
    String full_disambiguation = "7k/8/8/3R4/2R1R3/3R4/8/3K4 w - - 0 1";
    FenNotation.parseFEN(board, full_disambiguation);
    moveFigureAndSave("d3", "d4");
    assertThat(cli.game().algebraicNotation().toString()).hasToString("Rdd4");
  }
}
