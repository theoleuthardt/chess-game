package hwr.oop.chess.persistence;

import hwr.oop.chess.cli.InvalidUserInputException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFilePersistenceTest {
  private static final int TEST_GAME_ID = 1;
  private CSVFilePersistence persistence;
  private File tempDir;

  @BeforeEach
  void setUp() throws IOException {
    persistence = new CSVFilePersistence();
    persistence.setGameId(TEST_GAME_ID);
    tempDir = Files.createTempDirectory("readonly_dir").toFile();
  }

  @AfterEach
  void tearDown() {
    File file = new File(tempDir, "game_" + TEST_GAME_ID + ".csv");
    if (file.exists()) {
      assertTrue(file.delete(), "Test file should be deleted after test.");
    }
    tempDir.setWritable(true);
    tempDir.delete();
  }

  @Test
  void testSaveGame() {
    persistence.storeState("key1", "value1");
    persistence.storeState("key2", "value2");

    persistence.saveGame();

    File file = new File("game_" + TEST_GAME_ID + ".csv");
    assertTrue(file.exists(), "The file should be created after saving the game.");

    // Further validation can include reading the file and checking its contents
  }

  @Test
  void testLoadGame() {
    // to create a CSV file for the test
    try (FileWriter writer = new FileWriter("game_" + TEST_GAME_ID + ".csv")) {
      writer.write("key1,value1\n");
      writer.write("key2,value2\n");
    } catch (IOException e) {
      fail("Failed to set up test file.");
    }

    persistence.loadGame();

    assertEquals("value1", persistence.loadState("key1"));
    assertEquals("value2", persistence.loadState("key2"));
  }

  @Test
  void testSetInvalidGameId() {
    InvalidUserInputException exception =
        assertThrows(InvalidUserInputException.class, () -> persistence.setGameId(0));

    assertEquals("The game ID must be a positive integer (1 or larger).", exception.getMessage());
  }

  @Test
  void testLoadGameNonExistentFile() {
    persistence.setGameId(9999);
    InvalidUserInputException exception =
        assertThrows(InvalidUserInputException.class, () -> persistence.loadGame());

    assertTrue(exception.getMessage().contains("could not be started"));
  }

  @Test
  void testSaveGameIOException() throws NoSuchFieldException {
    // to make the temporary directory read-only to simulate an IOException
    tempDir.setWritable(false);

    // to change the private 'gameData' field in CSVFilePersistence
    Field fileNameField = CSVFilePersistence.class.getDeclaredField("gameData");
    fileNameField.setAccessible(true);

    CSVFilePersistence testPersistence =
        new CSVFilePersistence() {
          @Override
          public void saveGame() {
            try (FileWriter writer = new FileWriter(new File(tempDir, "game_" + gameId + ".csv"))) {
              for (Map.Entry<String, String> entry : gameData.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
              }
            } catch (IOException e) {
              throw new InvalidUserInputException(
                  "The Game #"
                      + gameId
                      + " could not be saved. Please verify that the current folder is not protected. (Error: "
                      + e.getMessage()
                      + ")");
            }
          }
        };
    testPersistence.setGameId(TEST_GAME_ID);
    testPersistence.storeState("key1", "value1");

    InvalidUserInputException exception =
        assertThrows(InvalidUserInputException.class, testPersistence::saveGame);

    assertTrue(exception.getMessage().contains("could not be saved"));

    // to clean up
    tempDir.setWritable(true);
  }
}
