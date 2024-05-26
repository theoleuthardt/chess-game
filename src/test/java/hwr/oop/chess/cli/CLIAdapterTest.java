package hwr.oop.chess.cli;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import hwr.oop.chess.persistence.NoPersistence;
import org.junit.jupiter.api.Test;

class CLIAdapterTest {
  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private final CLIAdapter cli = new CLIAdapter(new PrintStream(outputStream), new NoPersistence());

  @Test
  void hasPersistence() {
    assertThat(cli.persistence()).isNotNull();
  }

  @Test
  void canParseGameId() {
    cli.forGameId("123");
    assertThat(cli.persistence().gameId()).isEqualTo(123);
  }

  @Test
  void canCreateGame() {
    cli.forGameId("123");
    assertThat(cli.initializeGame(true)).isNotNull();
  }

  @Test
  void canShowBoardByGame() {
    cli.forGameId("123");
    cli.initializeGame(true);
    cli.printBoard();
    assertThat(outputStream.toString())
        .contains(
            "8 | ",
            "7 | ",
            "6 | ",
            "5 | ",
            "4 | ",
            "3 | ",
            "2 | ",
            "1 | ",
            "- - - - - - - -",
            "p p p p p p p p",
            "________________",
            "A B C D E F G H");
  }

  @Test
  void canShowBoardByParameter() {
    cli.forGameId("123");
    cli.initializeGame(true);
    cli.printBoard(cli.game().board());
    assertThat(outputStream.toString())
        .contains(
            "8 | ",
            "7 | ",
            "6 | ",
            "5 | ",
            "4 | ",
            "3 | ",
            "2 | ",
            "1 | ",
            "- - - - - - - -",
            "p p p p p p p p",
            "________________",
            "A B C D E F G H");
  }
}
