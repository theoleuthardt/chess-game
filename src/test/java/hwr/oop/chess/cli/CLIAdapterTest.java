package hwr.oop.chess.cli;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.ChessGame;
import hwr.oop.chess.application.figures.FigureType;
import hwr.oop.chess.persistence.CsvGameRepository;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CLIAdapterTest {
  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private final CLIAdapter cliAdapter =
      new CLIAdapter(new PrintStream(outputStream), new CsvGameRepository());

  @BeforeEach
  void setUp() {
    outputStream.reset();
  }

  @Test
  void printlnTest() {
    cliAdapter.println("Hello");
    cliAdapter.println("World!");

    String message = isWindows() ? "Hello\r\nWorld!\r\n" : "Hello\nWorld!\n";
    assertThat(outputStream).hasToString(message);
  }

  @Test
  void printTest() {
    cliAdapter.print("Hello");
    cliAdapter.print("World!");

    assertThat(outputStream.toString()).contains("HelloWorld!");
  }

  @Test
  void emptyPrintln() {
    cliAdapter.println();
    String emptyPrintln = outputStream.toString();
    outputStream.reset();

    cliAdapter.println("");
    String printlnWithEmptyString = outputStream.toString();

    assertThat(emptyPrintln).isEqualTo(printlnWithEmptyString).contains("\n");
  }

  @Test
  void printColor_isYellow() {
    cliAdapter.printYellow("Yellow");
    assertThat(outputStream.toString()).startsWith("\033[30;1;103mYellow\033[0m");
  }

  @Test
  void printColor_isGray() {
    cliAdapter.printGray("Gray");
    assertThat(outputStream.toString()).startsWith("\033[37mGray\033[0m");
  }

  @Test
  void printColor_isBlue() {
    cliAdapter.printBlue("Blue");
    assertThat(outputStream.toString()).startsWith("\033[30;1;104mBlue\033[0m");
  }

  @Test
  void printlnError_passException() {
    InvalidUserInputException exception = new InvalidUserInputException("ExceptionTest");
    cliAdapter.printlnError(exception);
    assertThat(outputStream.toString()).contains("\033[30;1;103m ERROR \033[0m ExceptionTest");
  }

  @Test
  void printlnAction() {
    cliAdapter.printlnAction("ActionTest");
    assertThat(outputStream.toString()).contains("\033[30;1;104m ACTION \033[0m ActionTest");
  }

  @Test
  void hasPersistence() {
    assertThat(cliAdapter.persistence()).isNotNull();
  }

  @Test
  void canParseGameId() {
    cliAdapter.forGameId("123");
    assertThat(cliAdapter.gameId()).isEqualTo(123);
  }

  @Test
  void canCreateGameId() {
    cliAdapter.createGame("123");
    assertThat(cliAdapter.gameId()).isEqualTo(123);
    assertThat(outputStream.toString()).contains("A B C D E F G H");
  }

  @Test
  void canShowBoard() {
    cliAdapter.showBoard();
    assertThat(outputStream.toString()).contains("Here is game");
    assertThat(outputStream.toString()).contains("A B C D E F G H");
  }

  @Test
  void requireArgumentIsCoordinate_validCoordinateReturnsCell() {
    ChessGame game = cliAdapter.createGame("123");
    Board board = game.board();
    Cell validCell = cliAdapter.requireArgumentIsCoordinate(board, "a3");
    assertThat(validCell).isNotNull();
  }

  @Test
  void requireArgumentIsCoordinate_invalidCoordinateThrowsException() {
    ChessGame game = cliAdapter.createGame("123");
    Board board = game.board();
    assertThatThrownBy(() -> cliAdapter.requireArgumentIsCoordinate(board, "a9"))
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("The XY-Coordinates must be between A-H and 1-8");
  }

  @Test
  void requireArgumentIsCellWithFigure() {
    ChessGame game = cliAdapter.createGame("123");
    Board board = game.board();
    assertThat(cliAdapter.requireArgumentIsCellWithFigure(board, "a1")).isNotNull();
  }

  @Test
  void requireArgumentIsFigureType() {
    ChessGame game = cliAdapter.createGame("123");
    Board board = game.board();
    assertThat(cliAdapter.requireArgumentIsFigureType("queen")).isEqualTo(FigureType.QUEEN);
  }

  @Test
  void requireCellHasFigure_returnsFigureOnCell() {
    ChessGame game = cliAdapter.createGame("123");
    Board board = game.board();
    assertThat(cliAdapter.requireCellHasFigure(board.findCell("a1")).type())
        .isEqualTo(FigureType.ROOK);
  }

  @Test
  void requireCellHasFigure_throwsOnEmptyCell() {
    ChessGame game = cliAdapter.createGame("123");
    Board board = game.board();
    Cell cell = board.findCell("a5");
    assertThatThrownBy(() -> cliAdapter.requireCellHasFigure(cell))
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("there is no figure");
  }

  @ParameterizedTest
  @ValueSource(strings = {"move", "promote", "show-moves"})
  void commandsOnGame_doesSomething(String action) {
    List<String> arguments = new LinkedList<>();
    assertThatThrownBy(() -> cliAdapter.performActionOnBoard(action, arguments))
        .isInstanceOf(NoSuchElementException.class);
  }

  private boolean isWindows() {
    return System.getProperty("os.name").toLowerCase().contains("windows");
  }
}
