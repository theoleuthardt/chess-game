package hwr.oop.chess.application;

import hwr.oop.chess.persistence.FenNotation;
import hwr.oop.chess.persistence.CSVFilePersistence;

import org.junit.jupiter.api.*;
import java.nio.file.*;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;

class ChessGameTest {

  private static final String TEST_DIRECTORY = "test-chess-files";
  private static final String TEST_FILE = "chess.csv";
  private static final Path TEST_PATH = Paths.get(TEST_DIRECTORY, TEST_FILE);
  private CSVFilePersistence persistence;

  @BeforeAll
  static void setUpDirectory() throws IOException {
    Files.createDirectories(Paths.get(TEST_DIRECTORY));
  }

  @BeforeEach
  void setUp() throws IOException {
    // Ensure the test file is deleted before each test
    Files.deleteIfExists(TEST_PATH);
    persistence = new CSVFilePersistence();
  }

  @AfterEach
  void tearDown() throws IOException {
    // Ensure the test file is deleted after each test
    Files.deleteIfExists(TEST_PATH);
  }

  @Test
  void testSaveGame() {
    ChessGame chessGame = new ChessGame(persistence, true);

    // Set up board state
    Board board = chessGame.board();
    String initialFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    FenNotation.parseFEN(board, initialFen);

    // Save game state
    chessGame.saveGame();

    // Create a new persistence instance to simulate loading the saved game state
    CSVFilePersistence newPersistence = new CSVFilePersistence();
    ChessGame savedChessGame = new ChessGame(newPersistence, false);

    // Verify the saved state
    Board savedBoard = savedChessGame.board();
    String savedFen = FenNotation.generateFen(savedBoard);
    assertThat(savedFen).isEqualTo(initialFen);
  }

  @Test
  void testLoadGame() throws IOException {
    // Write initial FEN to the file
    String initialFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    Files.writeString(TEST_PATH, initialFen);

    // Load game state
    ChessGame chessGame = new ChessGame(persistence, false);

    // Verify loaded state
    Board board = chessGame.board();
    String loadedFen = FenNotation.generateFen(board);
    assertThat(loadedFen).isEqualTo(initialFen);
  }
}
