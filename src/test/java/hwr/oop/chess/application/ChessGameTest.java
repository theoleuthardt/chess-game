package hwr.oop.chess.application;

import static hwr.oop.chess.persistence.FenNotation.extractFenKeyParts;
import static hwr.oop.chess.persistence.FenNotation.generateFen;
import static org.assertj.core.api.Assertions.assertThat;

import hwr.oop.chess.cli.CLIAdapter;
import hwr.oop.chess.persistence.NoPersistence;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

class ChessGameTest {
  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private final CLIAdapter cli = new CLIAdapter(new PrintStream(outputStream), new NoPersistence());

  @Test
  void testThreeFoldRepetition() {
    cli.forGameId("123");
    cli.initializeGame(true);

    // Same states after #8, #12, #16
    moveFigureAndSave("e2", "e4", cli); // #1
    moveFigureAndSave("e7", "e5", cli); // #2
    moveFigureAndSave("g1", "f3", cli); // #3
    moveFigureAndSave("b8", "c6", cli); // #4
    moveFigureAndSave("b1", "c3", cli); // #5
    moveFigureAndSave("g8", "f6", cli); // #6
    moveFigureAndSave("f3", "g1", cli); // #7
    moveFigureAndSave("f6", "g8", cli); // #8
    String fen1 = extractFenKeyParts(generateFen(cli.game().board()));
    moveFigureAndSave("g1", "f3", cli); // #9
    moveFigureAndSave("g8", "f6", cli); // #10
    moveFigureAndSave("f3", "g1", cli); // #11
    moveFigureAndSave("f6", "g8", cli); // #12
    String fen2 = extractFenKeyParts(generateFen(cli.game().board()));
    moveFigureAndSave("c3", "b1", cli); // #13
    moveFigureAndSave("c6", "b8", cli); // #14
    moveFigureAndSave("b1", "c3", cli); // #15
    moveFigureAndSave("b8", "c6", cli); // #16
    String fen3 = extractFenKeyParts(generateFen(cli.game().board()));

    assertThat(fen1).isEqualTo(fen2).isEqualTo(fen3);
    assertThat(cli.game().isThreeFoldRepetition()).isTrue();
  }

  void moveFigureAndSave(String from, String to, CLIAdapter cli) {
    cli.game().board().moveFigure(from, to);
    cli.game().saveGame();
  }
}
