package hwr.oop.chess.cli;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// The use of System.out is allowed here as the only place in the repo!
@java.lang.SuppressWarnings("java:S106")
public class Main {
  protected CLIAdapter cli = new CLIAdapter(System.out);

  public static void main(String[] args) {
    new Main(args);
  }

  public Main(String[] args) {
    cli.println("");
    if(args.length == 0) {
      printCommands();
      return;
    }

    cli.println("Arguments: " + Arrays.asList(args));
  }


  public void printCommands() {
    Map<String, String> commandAndExplanation = new HashMap<>();
    commandAndExplanation.put("cli create <ID>", "Create a new game of chess");
    commandAndExplanation.put("cli show-board <ID>", "Show the board");
    commandAndExplanation.put("cli on <ID> move <FROM> to <TO>", "Move a figure");

    cli.println("Chess Commands:");
    commandAndExplanation.forEach((k, v) -> cli.println(String.format("%-40s", "- " + k + ": ") + v));
    cli.println("");
  }
}
