package hwr.oop.chess.cli;

import hwr.oop.chess.persistence.CSVFilePersistence;
import java.util.LinkedList;
import java.util.List;

// The use of System.out is allowed here as the only place in the repo!
@java.lang.SuppressWarnings("java:S106")
public class Main {
  public static void mainWithCli(String[] args, CLIAdapter cli) {
    List<String> arguments = new LinkedList<>(List.of(args));
    cli.handle(arguments);
  }

  public static void main(String[] args) {
    mainWithCli(args, new CLIAdapter(System.out, new CSVFilePersistence()));
  }
}
