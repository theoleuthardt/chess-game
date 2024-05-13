package hwr.oop.chess.cli;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.persistence.NoPersistence;
import hwr.oop.chess.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CLIMenuTest {
  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private final CLIAdapter cli = new CLIAdapter(new PrintStream(outputStream), new NoPersistence());
  private CLIMenu menu;

  void menuFromArguments(List<String> args) {
    cli.forGameId("123");
    cli.initializeGame(true);
    menu = new CLIMenu(cli);
    menu.withArguments(new LinkedList<>(args));
  }

  @Test
  void requireArgumentIsCoordinate_validCoordinateReturnsCell() {
    menuFromArguments(List.of("a1"));
    Board board = cli.game().board();
    assertThat(menu.argumentToCoordinate(board)).isEqualTo(board.findCell('a', 1));
  }

  @Test
  void requireArgumentIsCoordinate_invalidCoordinateThrowsException() {
    menuFromArguments(List.of("a9"));
    Board board = cli.game().board();
    assertThatThrownBy(() -> menu.argumentToCoordinate(board))
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("The XY-Coordinates must be between A-H and 1-8");
  }

  @Test
  void canPrintHelpMenu() {
    cli.handle(new LinkedList<>(List.of("abc")));
    assertThat(outputStream.toString().replaceAll("\\r", ""))
        .isEqualTo(
            """

                      \033[30;1;103m ERROR \033[0m The command 'abc' is not supported. Try 'help' to see available commands.

                      """);
  }

  @Test
  void usesGameIdOnCreation() {
    menuFromArguments(List.of("create", "123"));
    assertThat(cli.game()).isNotNull();
    Persistence persistence = cli.persistence();
    assertThat(persistence.loadState("figures")).isNull();
    assertThatNoException().isThrownBy(persistence::loadGame);
    assertThatNoException().isThrownBy(persistence::saveGame);
    assertThatNoException().isThrownBy(() -> persistence.loadState("figures"));
    assertThatNoException().isThrownBy(() -> persistence.storeState("figures", "abc"));
    assertThat(persistence.gameId()).isEqualTo(123);
  }
}
