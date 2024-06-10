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

  @Test
  void testGeneratePgn() {
    cli.forGameId("123");
    cli.initializeGame(true);

    moveFigureAndSave("d2", "d4", cli); // 1. d4
    moveFigureAndSave("g8", "f6", cli); // 1... nf6
    moveFigureAndSave("c2", "c4", cli); // 2. c4
    moveFigureAndSave("e7", "e6", cli); // 2... e6
    moveFigureAndSave("b1", "c3", cli); // 3. Nc3
    moveFigureAndSave("f8", "b4", cli); // 3... bb4
    moveFigureAndSave("g1", "f3", cli); // 4. Nf3
    moveFigureAndSave("c7", "c5", cli); // 4... c5
    moveFigureAndSave("e2", "e3", cli); // 5. e3
    moveFigureAndSave("b8", "c6", cli); // 5... nc6
    moveFigureAndSave("f1", "d3", cli); // 6. Bd3
    moveFigureAndSave("b4", "c3", cli); // 6... bxc3+
    moveFigureAndSave("b2", "c3", cli); // 7. bxc3
    moveFigureAndSave("d7", "d6", cli); // 7... d6
    moveFigureAndSave("e3", "e4", cli); // 8. e4
    moveFigureAndSave("e6", "e5", cli); // 8... e5
    moveFigureAndSave("d4", "d5", cli); // 9. d5
    moveFigureAndSave("c6", "e7", cli); // 9... ne7
    moveFigureAndSave("f3", "h4", cli); // 10. Nh4
    moveFigureAndSave("h7", "h6", cli); // 10... h6
    moveFigureAndSave("f2", "f4", cli); // 11. f4
    moveFigureAndSave("e7", "g6", cli); // 11... ng6
    moveFigureAndSave("h4", "g6", cli); // 12. Nxg6
    moveFigureAndSave("f7", "g6", cli); // 12... fxg6
    moveFigureAndSave("f4", "e5", cli); // 13. fxe5
    moveFigureAndSave("d6", "e5", cli); // 13... dxe5
    moveFigureAndSave("c1", "e3", cli); // 14. Be3
    moveFigureAndSave("b7", "b6", cli); // 14... b6
    moveFigureAndSave("e1", "g1", cli); // 15. O-O White King Castling
    moveFigureAndSave("e8", "g8", cli); // 15. O-O Black King Castling
    assertThat(String.join(",", cli.game().pgnHistory()))
        .isEqualTo("d4,nf6,c4,e6,Nc3,bb4,Nf3,c5,e3,nc6,Bd3,bxc3,bxc3,d6,e4,e5,d5,ne7,Nh4,h6,f4,ng6,Nxg6,fxg6,fxe5,dxe5,Be3,b6,O-O,O-O");
  }
  // "d4,nf6,c4,e6,Nc3,bb4,Nf3,c5,e3,nc6,Bd3,bxc3+,bxc3,d6,e4,e5,d5,ne7,Nh4,h6,f4,ng6,Nxg6,fxg6,fxe5,dxe5,Be3,b6,O-O,O-O"
  void moveFigureAndSave(String from, String to, CLIAdapter cli) {
    cli.game().board().moveFigure(from, to);
    cli.game().saveGame();
  }
}
