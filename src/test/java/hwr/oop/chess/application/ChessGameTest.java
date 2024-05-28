package hwr.oop.chess.application;

import hwr.oop.chess.cli.CLIAdapter;
import hwr.oop.chess.persistence.CSVFilePersistence;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.*;

class ChessGameTest {
  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  CSVFilePersistence csv = new CSVFilePersistence();
  private final CLIAdapter cli = new CLIAdapter(new PrintStream(outputStream), csv);

  @Test
  void saveGameTest() {
    ChessGame newGame = new ChessGame(csv, true);
    newGame.saveGame();
  }

  @Test
  void loadGameTest() {
    ChessGame newGame = new ChessGame(csv, false);
    csv.loadState("");
  }
}
