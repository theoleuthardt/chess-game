package hwr.oop.chess.cli;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.UUID;

import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.persistence.Player;

public class CLIAdapter {
  /*
  private final Scanner scanner;
  private final PrintStream printStream;

  public CLIAdapter(InputStream inputStream, OutputStream outputStream) {
    this.scanner = new Scanner(inputStream);
    this.printStream = new PrintStream(outputStream);
  }

  public void start() {
    final var nextLine = scanner.nextLine();
    if (nextLine.equals("1")) {
      createNewGameDialog();
    } else {
      printStream.println("Invalid input");
    }
  }

  private void createNewGameDialog() {
    printStream.println("Now creating a new game!");
    printStream.println("Type in Player 1 name!");
    final var nextLine = scanner.nextLine();
    Player player1 = new Player("", FigureColor.WHITE, 0);
    Player player2 = new Player("", FigureColor.BLACK, 0);

    printStream.println("Game created, ID: " + UUID.randomUUID());
  }

  private String forceNextLine() {
    while (true) {
      final String nextLine = scanner.nextLine();
      if (!nextLine.isBlank()) {
        return nextLine;
      }
    }
  }*/
}
