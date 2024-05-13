package hwr.oop.chess.cli;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

class MainTest {
  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private final CLIAdapter cliAdapter = new CLIAdapter(new PrintStream(outputStream));

  public void setUp(String[] args) {
    Main.mainWithCli(args, cliAdapter);
  }

  @Test
  void testNoArguments() {
    setUp(new String[] {});
    assertThat(outputStream.toString())
        .contains(
            "- chess on <ID> move <FROM> <TO>:",
            "Move a figure",
            "- chess create <ID>:",
            "Create a new game of chess");
  }

  @Test
  void testPrintHelpCommands() {
    setUp(new String[] {});
    String noAttributesText = outputStream.toString();

    outputStream.reset();
    setUp(new String[] {"help"});
    String helpText = outputStream.toString();

    assertThat(noAttributesText).isEqualTo(helpText);
  }
}
