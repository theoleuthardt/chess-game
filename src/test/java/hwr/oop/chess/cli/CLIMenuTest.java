package hwr.oop.chess.cli;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.figures.FigureType;
import hwr.oop.chess.persistence.FenNotation;
import hwr.oop.chess.persistence.NoPersistence;
import hwr.oop.chess.persistence.Persistence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CLIMenuTest {
  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private final CLIAdapter cli = new CLIAdapter(new PrintStream(outputStream), new NoPersistence());
  private CLIMenu menu;

  void menuFromArguments(List<String> args) {
    cli.forGameId("123");
    cli.initializeGame(true);
    menu = new CLIMenu(cli);
    menu.withArguments(new LinkedList<>(args));
  }

  void realCLIFromArguments(String args) {
    Main.mainWithCli(args.split(" "), cli);
  }

  @Test
  void requireArgumentIsCoordinate_validCoordinateReturnsCell() {
    menuFromArguments(List.of("a1"));
    Board board = cli.game().board();
    assertThat(menu.argumentToCoordinate(board)).isEqualTo(board.findCell('a', 1));
  }

  @Test
  void requireArgumentIsCoordinate_invalidCoordinateThrowsException() {
    menuFromArguments(List.of("a9"));
    Board board = cli.game().board();
    assertThatThrownBy(() -> menu.argumentToCoordinate(board))
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("The XY-Coordinates must be between A-H and 1-8");
  }

  @Test
  void canPrintHelpMenu() {
    cli.handle(new LinkedList<>(List.of("abc")));
    assertThat(outputStream.toString().replaceAll("\\r", ""))
        .isEqualTo(
            """

                      \033[30;1;103m ERROR \033[0m The command 'abc' is not supported. Try 'help' to see available commands.

                      """);
  }

  @Test
  void usesGameIdOnCreation() {
    menuFromArguments(List.of("create", "123"));
    assertThat(cli.game()).isNotNull();
    Persistence persistence = cli.persistence();
    assertThat(persistence.loadState("figures")).isNull();
    assertThatNoException().isThrownBy(persistence::loadGame);
    assertThatNoException().isThrownBy(persistence::saveGame);
    assertThatNoException().isThrownBy(() -> persistence.loadState("figures"));
    assertThatNoException().isThrownBy(() -> persistence.storeState("figures", "abc"));
    assertThat(persistence.gameId()).isEqualTo(123);
  }

  @Test
  void handleThrowsExceptionWhenInvalidCommandIsGiven() {
    menuFromArguments(List.of("invalidCommand"));
    menu.handle(new LinkedList<>(List.of("invalidCommand")));
    assertThat(outputStream.toString())
        .contains(
            "The command 'invalidCommand' is not supported. Try 'help' to see available commands.");
  }

  @Test
  void handlePrintsHelpMenuWhenNoArgumentsAreGiven() {
    menuFromArguments(new LinkedList<>());
    menu.handle(new LinkedList<>());
    assertThat(outputStream.toString()).contains("Chess Commands:").contains("Parameter Types:");
  }

  @Test
  void argumentToCoordinate_throwsExceptionWhenNoArgumentsAreLeft() {
    menuFromArguments(new LinkedList<>());
    Board board = cli.game().board();
    assertThatThrownBy(() -> menu.argumentToCoordinate(board))
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("You must provide a coordinate for this command.");
  }

  @Test
  void argumentToFigureType_throwsExceptionWhenNoArgumentsAreLeft() {
    menuFromArguments(new LinkedList<>());
    assertThatThrownBy(() -> menu.argumentToFigureType())
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("You must provide a figure type for this command.");
  }

  @Test
  void argumentToGameId_throwsExceptionWhenNoArgumentsAreLeft() {
    menuFromArguments(new LinkedList<>());
    assertThatThrownBy(() -> menu.argumentToGameId())
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("You must provide a game id for this command.");
  }

  @Test
  void argumentToGameId_throwsExceptionWhenArgumentIsNotANumber() {
    menuFromArguments(List.of("notANumber"));
    assertThatThrownBy(() -> menu.argumentToGameId())
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining(
            "You must provide a positive integer as the game id. Please check 'notANumber' for typos.");
  }

  @Test
  void mustBeCellWithFigure_throwsExceptionWhenCellIsFree() {
    menuFromArguments(List.of("a1"));
    Board board = cli.game().board();
    Cell cell = board.findCell('a', 1);
    cell.setFigure(null);
    assertThatThrownBy(() -> menu.mustBeCellWithFigure(cell))
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("On the cell A1 there is no figure!");
  }

  @Test
  void countOfRemainingArgumentsIs_throwsExceptionWhenTooFewArgumentsAreLeft() {
    menuFromArguments(List.of("a1"));
    assertThatThrownBy(() -> menu.countOfRemainingArgumentsIs(2))
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("Your command 'a1' is missing 1 argument(s)");
  }

  @Test
  void countOfRemainingArgumentsIs_doesNotThrowExceptionWhenCorrectAmountOfArgumentsAreLeft() {
    menuFromArguments(List.of("a1", "b2"));
    assertThatNoException().isThrownBy(() -> menu.countOfRemainingArgumentsIs(2));
  }

  @Test
  void countOfRemainingArgumentsIs_throwsExceptionWhenTooManyArgumentsAreLeft() {
    menuFromArguments(List.of("a1", "b2", "c3"));
    assertThatThrownBy(() -> menu.countOfRemainingArgumentsIs(1))
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("Your command 'a1 b2 c3' has 2 argument(s) more than needed.");
  }

  @ParameterizedTest
  @ValueSource(strings = {"create 7 xyz", "show 7 xyz"})
  void runCommandsWithTooManyArguments(String arguments) {
    Main.mainWithCli(arguments.split(" "), cli);
    assertThat(outputStream.toString())
        .contains("Your command '" + arguments + "' has 1 argument(s) more than needed.");
  }

  @Test
  void pawnPromotionHasActionText_failsIfPawnIsNotOnEnd() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.PAWN_PROMOTION.ordinal() + " promote a2 rook");
    assertThat(outputStream.toString())
        .contains("Promote WHITE PAWN on A2 to become a ROOK.")
        .contains("The pawn is not allowed to be promoted as it has not reached the end");
  }

  @Test
  void pawnPromotionHasActionText_successOnEndOfLine() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.PAWN_PROMOTION.ordinal() + " promote c8 rook");
    assertThat(outputStream.toString())
        .contains("Promote WHITE PAWN on C8 to become a ROOK.")
        .doesNotContain("The pawn is not allowed to be promoted as it has not reached the end");
    assertThat(cli.game().board().findCell("c8").figure().type()).isEqualTo(FigureType.ROOK);
    assertThat(FenNotation.generateFen(cli.game().board())).startsWith("2RP4/");
  }

  @Test
  void argumentToFigureType_returnsFigureType() {
    menuFromArguments(List.of("pawn"));
    assertThat(menu.argumentToFigureType()).isEqualTo(FigureType.PAWN);
  }

  @Test
  void argumentToGameIdSetsGameId() {
    menuFromArguments(List.of("123"));
    menu.argumentToGameId();
    assertThat(cli.persistence().gameId()).isEqualTo(123);
  }

  @Test
  void mustBeCellWithFigure_returnsFigure() {
    menuFromArguments(List.of("a1"));
    Board board = cli.game().board();
    Cell cell = board.findCell('a', 1);
    assertThat(menu.mustBeCellWithFigure(cell)).isNotNull().isEqualTo(cell.figure());
  }

  @Test
  void showBoardStatus() {
    realCLIFromArguments("show " + NoPersistence.GameIdType.DEFAULT_POSITIONS.ordinal());
    assertThat(outputStream.toString()).contains("Here is game ").contains("A B C D E F G H");
    assertThat(cli.game()).isNotNull();
  }

  @Test
  void startNewGame() {
    realCLIFromArguments("create 123");
    assertThat(outputStream.toString())
        .contains("Here is your new game")
        .contains("Good luck and have fun.")
        .contains("A B C D E F G H");
    assertThat(cli.game()).isNotNull();
    assertThat(cli.game().board().findCell("a1").figure().type()).isEqualTo(FigureType.ROOK);
  }
}
