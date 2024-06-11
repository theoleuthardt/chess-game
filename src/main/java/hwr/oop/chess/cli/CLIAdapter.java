package hwr.oop.chess.cli;

import hwr.oop.chess.application.ChessGame;
import hwr.oop.chess.application.Game;
import hwr.oop.chess.persistence.NoPersistence;
import hwr.oop.chess.persistence.Persistence;
import java.io.OutputStream;
import java.util.List;

public class CLIAdapter {
  private final CLIPrinter printer;
  private final Persistence persistence;
  private Game game;

  public CLIAdapter(OutputStream outputStream) {
    this(outputStream, new NoPersistence());
  }

  public CLIAdapter(OutputStream outputStream, Persistence persistence) {
    this.persistence = persistence;
    this.printer = new CLIPrinter(outputStream);
  }

  public void handle(List<String> arguments) {
    CLIMenu menu = new CLIMenu(this);
    menu.handle(arguments);
  }

  public CLIPrinter printer() {
    return printer;
  }

  public void printBoard() {
    printer.print(game.board());
  }

  public Persistence persistence() {
    return persistence;
  }

  public void forGameId(String gameNumber) {
    persistence.setGameId(Integer.parseInt(gameNumber));
  }

  public Game game() {
    return game;
  }

  public Game initializeGame(boolean isNew) {
    game = new ChessGame(persistence, isNew);
    return game;
  }
}
