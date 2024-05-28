package hwr.oop.chess.persistence;

import hwr.oop.chess.cli.InvalidUserInputException;
import hwr.oop.chess.cli.Main;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.*;

class CSVFilePersistenceTest {
  Persistence persistence = new CSVFilePersistence();

  @Test
  void createGameAndLoadGame() throws IOException {
    Main.main(new String[] {"create", "9999"});

    Path csv = Paths.get("game_9999.csv");
    assertThat(Files.exists(csv)).isTrue();

    persistence.setGameId(9999);
    assertThat(persistence.gameId()).isEqualTo(9999);

    persistence.loadGame();
    assertThat(persistence.loadState("fen"))
        .isEqualTo("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 0");

    assertThat(Files.deleteIfExists(csv)).isTrue();

    assertThatThrownBy(() -> persistence.loadGame())
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("game_9999.csv");
    assertThat(persistence.loadState("fen")).isNull();
  }

  @Test
  void negativeGameId() {
    assertThatThrownBy(() -> persistence.setGameId(0))
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("The game ID must be a positive integer (1 or larger).");
    assertThatThrownBy(() -> persistence.setGameId(-1))
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("The game ID must be a positive integer (1 or larger).");
  }

  @Test
  void writeFileIsLocked() {
    try {
      Path csv = Paths.get("game_9999.csv");
      Files.deleteIfExists(csv);
      Files.createDirectories(csv);
      persistence.setGameId(9999);
      assertThatThrownBy(() -> persistence.saveGame())
          .isInstanceOf(InvalidUserInputException.class)
          .hasMessageContaining("game_9999.csv")
          .hasMessageContaining(
              "The Game #9999 could not be saved. Please verify that the current folder is not protected.");
      Files.deleteIfExists(csv);
    } catch (IOException e) {
      fail("Unexpected IOException: " + e.getMessage());
    }
  }
}
