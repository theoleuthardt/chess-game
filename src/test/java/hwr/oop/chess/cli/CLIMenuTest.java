package hwr.oop.chess.cli;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.application.figures.FigureType;
import hwr.oop.chess.persistence.FenNotation;
import hwr.oop.chess.persistence.NoPersistence;
import hwr.oop.chess.persistence.State;
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
  private final NoPersistence persistence = new NoPersistence();
  private final CLIAdapter cli = new CLIAdapter(new PrintStream(outputStream), persistence);
  private final int gameWithDefaultFigures = NoPersistence.GameIdType.DEFAULT_POSITIONS.ordinal();
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
  void showErrorOnUnsupportedCommand() {
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
    assertThatNoException().isThrownBy(persistence::loadGame);
    assertThatNoException().isThrownBy(persistence::saveGame);
    assertThatNoException().isThrownBy(() -> persistence.loadState(State.WINNER));
    assertThatNoException().isThrownBy(() -> persistence.storeState(State.WINNER, "abc"));
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
  @ValueSource(
      strings = {
        "create <ID> xyz",
        "on <ID> show-board xyz",
        "on <ID> show-stats xyz",
        "on <ID> show-moveable xyz",
        "on <ID> show-fen xyz",
        "on <ID> draw offer xyz",
        "on <ID> resign xyz",
        "on <ID> rematch xyz",
      })
  void runCommandsWithTooManyArguments(String arguments) {
    String command =
        arguments.replace("<ID>", "" + NoPersistence.GameIdType.DEFAULT_POSITIONS.ordinal());
    Main.mainWithCli(command.split(" "), cli);

    assertThat(outputStream.toString())
        .contains("Your command '" + command + "' has 1 argument(s) more than needed.");
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
    assertThat(persistence.gameId()).isEqualTo(123);
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
    realCLIFromArguments("on " + gameWithDefaultFigures + " show-board");
    assertThat(outputStream.toString()).contains("Here is game ").contains("A B C D E F G H");
    assertThat(cli.game()).isNotNull();
  }

  @Test
  void showBoardMoveable() {
    realCLIFromArguments("on " + gameWithDefaultFigures + " show-moveable");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;104m ACTION \033[0m Showing the figures which can be moved by the WHITE player.
            \033[37m8 | \033[0mr n b q k b n r
            \033[37m7 | \033[0mp p p p p p p p
            \033[37m6 | \033[0m- - - - - - - -
            \033[37m5 | \033[0m- - - - - - - -
            \033[37m4 | \033[0m- - - - - - - -
            \033[37m3 | \033[0m- - - - - - - -
            \033[37m2 | \033[0m\033[30;1;104mP\033[0m \033[30;1;104mP\033[0m \033[30;1;104mP\033[0m \033[30;1;104mP\033[0m \033[30;1;104mP\033[0m \033[30;1;104mP\033[0m \033[30;1;104mP\033[0m \033[30;1;104mP\033[0m
            \033[37m1 | \033[0mR \033[30;1;104mN\033[0m B Q K B \033[30;1;104mN\033[0m R
            \033[37m  \\________________\033[0m
            \033[37m    A B C D E F G H\033[0m
            """);
    assertThat(cli.game()).isNotNull();
  }

  @Test
  void startNewGame() {
    realCLIFromArguments("create 1");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;104m ACTION \033[0m Here is your new game '1'. Good luck and have fun.
            \033[37m8 | \033[0mr n b q k b n r
            \033[37m7 | \033[0mp p p p p p p p
            \033[37m6 | \033[0m- - - - - - - -
            \033[37m5 | \033[0m- - - - - - - -
            \033[37m4 | \033[0m- - - - - - - -
            \033[37m3 | \033[0m- - - - - - - -
            \033[37m2 | \033[0mP P P P P P P P
            \033[37m1 | \033[0mR N B Q K B N R
            \033[37m  \\________________\033[0m
            \033[37m    A B C D E F G H\033[0m
            """);
    assertThat(cli.game()).isNotNull();
    assertThat(cli.game().board().findCell("a1").figure().type()).isEqualTo(FigureType.ROOK);
    assertThat(cli.game().fenHistory().getLast())
        .isEqualTo("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 0");
  }

  @Test
  void moveWithEnoughCoordinates() {
    realCLIFromArguments("on " + gameWithDefaultFigures + " move a2 a4");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;104m ACTION \033[0m Move WHITE PAWN from A2 to A4.
            \033[37m8 | \033[0mr n b q k b n r
            \033[37m7 | \033[0mp p p p p p p p
            \033[37m6 | \033[0m- - - - - - - -
            \033[37m5 | \033[0m- - - - - - - -
            \033[37m4 | \033[0m\033[30;1;104mP\033[0m - - - - - - -
            \033[37m3 | \033[0m- - - - - - - -
            \033[37m2 | \033[0m\033[30;1;104m-\033[0m P P P P P P P
            \033[37m1 | \033[0mR N B Q K B N R
            \033[37m  \\________________\033[0m
            \033[37m    A B C D E F G H\033[0m
            """);
  }

  @Test
  void moveWithoutEnoughCoordinates() {
    realCLIFromArguments("on " + gameWithDefaultFigures + " move a1");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;103m ERROR \033[0m You must provide a coordinate for this command.
            \033[37m8 | \033[0mr n b q k b n r
            \033[37m7 | \033[0mp p p p p p p p
            \033[37m6 | \033[0m- - - - - - - -
            \033[37m5 | \033[0m- - - - - - - -
            \033[37m4 | \033[0m- - - - - - - -
            \033[37m3 | \033[0m- - - - - - - -
            \033[37m2 | \033[0mP P P P P P P P
            \033[37m1 | \033[0m\033[30;1;104mR\033[0m N B Q K B N R
            \033[37m  \\________________\033[0m
            \033[37m    A B C D E F G H\033[0m
            """);
  }

  @Test
  void showMovesOfPawn() {
    realCLIFromArguments("on " + gameWithDefaultFigures + " show-moves b2");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;104m ACTION \033[0m Showing the cells where the PAWN on B2 can move to.
            \033[37m8 | \033[0mr n b q k b n r
            \033[37m7 | \033[0mp p p p p p p p
            \033[37m6 | \033[0m- - - - - - - -
            \033[37m5 | \033[0m- - - - - - - -
            \033[37m4 | \033[0m- \033[30;1;104m-\033[0m - - - - - -
            \033[37m3 | \033[0m- \033[30;1;104m-\033[0m - - - - - -
            \033[37m2 | \033[0mP P P P P P P P
            \033[37m1 | \033[0mR N B Q K B N R
            \033[37m  \\________________\033[0m
            \033[37m    A B C D E F G H\033[0m
            """);
  }

  @Test
  void moveToTheStartField() {
    realCLIFromArguments("on " + gameWithDefaultFigures + " move a2 a2");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;104m ACTION \033[0m Move WHITE PAWN from A2 to A2.
            \033[30;1;103m ERROR \033[0m The figure can't move to that cell
            \033[37m8 | \033[0mr n b q k b n r
            \033[37m7 | \033[0mp p p p p p p p
            \033[37m6 | \033[0m- - - - - - - -
            \033[37m5 | \033[0m- - - - - - - -
            \033[37m4 | \033[0m- - - - - - - -
            \033[37m3 | \033[0m- - - - - - - -
            \033[37m2 | \033[0m\033[30;1;104mP\033[0m P P P P P P P
            \033[37m1 | \033[0mR N B Q K B N R
            \033[37m  \\________________\033[0m
            \033[37m    A B C D E F G H\033[0m
            """);
  }

  @Test
  void moveEmptyField() {
    realCLIFromArguments("on " + gameWithDefaultFigures + " move a3 a4");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;103m ERROR \033[0m On the cell A3 there is no figure!
            \033[37m8 | \033[0mr n b q k b n r
            \033[37m7 | \033[0mp p p p p p p p
            \033[37m6 | \033[0m- - - - - - - -
            \033[37m5 | \033[0m- - - - - - - -
            \033[37m4 | \033[0m- - - - - - - -
            \033[37m3 | \033[0m\033[30;1;104m-\033[0m - - - - - - -
            \033[37m2 | \033[0mP P P P P P P P
            \033[37m1 | \033[0mR N B Q K B N R
            \033[37m  \\________________\033[0m
            \033[37m    A B C D E F G H\033[0m
            """);
  }

  @Test
  void moveWrongColor() {
    realCLIFromArguments("on " + gameWithDefaultFigures + " move c7 c6");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;104m ACTION \033[0m Move BLACK PAWN from C7 to C6.
            \033[30;1;103m ERROR \033[0m It is not your turn! Try to move a figure of color WHITE.
            \033[37m8 | \033[0mr n b q k b n r
            \033[37m7 | \033[0mp p \033[30;1;104mp\033[0m p p p p p
            \033[37m6 | \033[0m- - \033[30;1;104m-\033[0m - - - - -
            \033[37m5 | \033[0m- - - - - - - -
            \033[37m4 | \033[0m- - - - - - - -
            \033[37m3 | \033[0m- - - - - - - -
            \033[37m2 | \033[0mP P P P P P P P
            \033[37m1 | \033[0mR N B Q K B N R
            \033[37m  \\________________\033[0m
            \033[37m    A B C D E F G H\033[0m
            """);
  }

  @Test
  void promotionWithoutPawn() {
    realCLIFromArguments("on " + NoPersistence.GameIdType.PAWN_PROMOTION.ordinal() + " promote c3");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;103m ERROR \033[0m On the cell C3 there is no pawn!
            \033[37m8 | \033[0m- - P P - - - -
            \033[37m7 | \033[0m- - - - - - - -
            \033[37m6 | \033[0m- - - - - - - -
            \033[37m5 | \033[0m- - - - - - - -
            \033[37m4 | \033[0m- - - - - - - k
            \033[37m3 | \033[0m- - \033[30;1;104m-\033[0m - - - - -
            \033[37m2 | \033[0mP P - - - - - -
            \033[37m1 | \033[0m- - - - - - - K
            \033[37m  \\________________\033[0m
            \033[37m    A B C D E F G H\033[0m
            """);
  }

  @Test
  void promotionPawnIsNotOnEnd() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.PAWN_PROMOTION.ordinal() + " promote b2 rook");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;104m ACTION \033[0m Promote WHITE PAWN on B2 to become a ROOK.
            \033[30;1;103m ERROR \033[0m The pawn is not allowed to be promoted as it has not reached the end.
            \033[37m8 | \033[0m- - P P - - - -
            \033[37m7 | \033[0m- - - - - - - -
            \033[37m6 | \033[0m- - - - - - - -
            \033[37m5 | \033[0m- - - - - - - -
            \033[37m4 | \033[0m- - - - - - - k
            \033[37m3 | \033[0m- - - - - - - -
            \033[37m2 | \033[0mP \033[30;1;104mP\033[0m - - - - - -
            \033[37m1 | \033[0m- - - - - - - K
            \033[37m  \\________________\033[0m
            \033[37m    A B C D E F G H\033[0m
            """);
  }

  @Test
  void promotionPawnIsOnEnd() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.PAWN_PROMOTION.ordinal() + " promote c8 rook");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;104m ACTION \033[0m Promote WHITE PAWN on C8 to become a ROOK.
            \033[37m8 | \033[0m- - \033[30;1;104mR\033[0m P - - - -
            \033[37m7 | \033[0m- - - - - - - -
            \033[37m6 | \033[0m- - - - - - - -
            \033[37m5 | \033[0m- - - - - - - -
            \033[37m4 | \033[0m- - - - - - - k
            \033[37m3 | \033[0m- - - - - - - -
            \033[37m2 | \033[0mP P - - - - - -
            \033[37m1 | \033[0m- - - - - - - K
            \033[37m  \\________________\033[0m
            \033[37m    A B C D E F G H\033[0m
            """);
  }

  @Test
  void promotionPawnIsOnEndButInvalidType() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.PAWN_PROMOTION.ordinal() + " promote c8 king");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;104m ACTION \033[0m Promote WHITE PAWN on C8 to become a KING.
            \033[30;1;103m ERROR \033[0m The pawn cannot become a KING.
            """);
  }

  @Test
  void whiteKingIsInCheck() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.WHITE_CHECK_POSSIBLE.ordinal() + " move f2 f1");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;104m ACTION \033[0m Move BLACK QUEEN from F2 to F1.
            \033[30;1;103m ERROR \033[0m The WHITE king is in check.
            \033[37m8 | \033[0m- - - - - - - -
            \033[37m7 | \033[0m- - - - - - - -
            \033[37m6 | \033[0mk - - - - - - -
            \033[37m5 | \033[0m- - - - - - - -
            \033[37m4 | \033[0m- - - - - - - -
            \033[37m3 | \033[0m- - - - - - - -
            \033[37m2 | \033[0mP P - - - \033[30;1;104m-\033[0m - -
            \033[37m1 | \033[0m- K - - - \033[30;1;104mq\033[0m - -
            \033[37m  \\________________\033[0m
            \033[37m    A B C D E F G H\033[0m
            """);
  }

  @Test
  void whiteKingIsInCheckMate() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.WHITE_CHECKMATE_POSSIBLE.ordinal() + " move b4 a4");
    assertThat(cli.game().players().get(FigureColor.WHITE).score()).isEqualTo(0.0);
    assertThat(cli.game().players().get(FigureColor.BLACK).score()).isEqualTo(1.0);
    assertThat(persistence.loadState(State.WINNER)).isEqualTo(FigureColor.BLACK.name());
    assertThat(persistence.wasSaved()).isTrue();
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;104m ACTION \033[0m Move BLACK ROOK from B4 to A4.
            \033[30;1;103m ERROR \033[0m The WHITE king is in checkmate. The game is over.
            \033[37m8 | \033[0mK - - - - - - -
            \033[37m7 | \033[0m- - - - - - - -
            \033[37m6 | \033[0m- - - - - - - -
            \033[37m5 | \033[0m- r - - - - - -
            \033[37m4 | \033[0m\033[30;1;104mr\033[0m \033[30;1;104m-\033[0m - - - - - -
            \033[37m3 | \033[0m- - - - - - - -
            \033[37m2 | \033[0m- - - - - - - -
            \033[37m1 | \033[0m- - - - - - - k
            \033[37m  \\________________\033[0m
            \033[37m    A B C D E F G H\033[0m
            """);
  }

  @Test
  void whiteKingIsInStalemate() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.WHITE_STALEMATE_POSSIBLE.ordinal() + " move h7 c7");
    assertThat(cli.game().players().get(FigureColor.WHITE).score()).isEqualTo(0.5);
    assertThat(cli.game().players().get(FigureColor.BLACK).score()).isEqualTo(0.5);
    assertThat(persistence.loadState(State.WINNER)).isEqualTo("draw");
    assertThat(persistence.wasSaved()).isTrue();
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;104m ACTION \033[0m Move BLACK QUEEN from H7 to C7.
            \033[30;1;103m ERROR \033[0m The WHITE king is in stalemate. The game is over.
            """);
  }

  @Test
  void gameIsOverWhiteWon() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.GAME_IS_OVER_WHITE_WINS.ordinal() + " move h7 c7");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;103m ERROR \033[0m The WHITE player has already won the game.
            """);
  }

  @Test
  void gameIsOverDraw() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.GAME_IS_OVER_DRAW.ordinal() + " move h7 c7");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;103m ERROR \033[0m The game ended with a draw. Both players got half a point.
            """);
  }

  @Test
  void invalidArgument() {
    realCLIFromArguments("invalid");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;103m ERROR \033[0m The command 'invalid' is not supported. Try 'help' to see available commands.
            """);
  }

  @Test
  void onGameWithoutAction() {
    realCLIFromArguments("on " + gameWithDefaultFigures);
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;103m ERROR \033[0m You must specify the action you would like to perform on the board
            """)
        .containsIgnoringWhitespaces(
            """
            \033[37m8 | \033[0mr n b q k b n r
            \033[37m7 | \033[0mp p p p p p p p
            \033[37m6 | \033[0m- - - - - - - -
            \033[37m5 | \033[0m- - - - - - - -
            \033[37m4 | \033[0m- - - - - - - -
            \033[37m3 | \033[0m- - - - - - - -
            \033[37m2 | \033[0mP P P P P P P P
            \033[37m1 | \033[0mR N B Q K B N R
            \033[37m  \\________________\033[0m
            \033[37m    A B C D E F G H\033[0m
            """);
  }

  @Test
  void onGameWithInvalidAction() {
    realCLIFromArguments("on " + gameWithDefaultFigures + " invalid");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            "\033[30;1;103m ERROR \033[0m The command 'chess on <ID> invalid' is not supported. Try 'help' to see available commands.")
        .containsIgnoringWhitespaces(
            """
            \033[37m8 | \033[0mr n b q k b n r
            \033[37m7 | \033[0mp p p p p p p p
            \033[37m6 | \033[0m- - - - - - - -
            \033[37m5 | \033[0m- - - - - - - -
            \033[37m4 | \033[0m- - - - - - - -
            \033[37m3 | \033[0m- - - - - - - -
            \033[37m2 | \033[0mP P P P P P P P
            \033[37m1 | \033[0mR N B Q K B N R
            \033[37m  \\________________\033[0m
            \033[37m    A B C D E F G H\033[0m
            """);
  }

  @Test
  void resignGame() {
    realCLIFromArguments("on " + gameWithDefaultFigures + " resign");
    assertThat(cli.game().players().get(FigureColor.WHITE).score()).isEqualTo(0.0);
    assertThat(cli.game().players().get(FigureColor.BLACK).score()).isEqualTo(1.0);
    assertThat(cli.game().players().get(FigureColor.WHITE).elo()).isLessThan(1200);
    assertThat(cli.game().players().get(FigureColor.BLACK).elo()).isGreaterThan(1200);
    assertThat(persistence.loadState(State.WINNER)).isEqualTo(FigureColor.BLACK.name());
    assertThat(persistence.wasSaved()).isTrue();
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;104m ACTION \033[0m You resigned the game. Your opponent has won.
            """);
  }

  @Test
  void performRematchOnlyIfGameEnded() {
    realCLIFromArguments("on " + gameWithDefaultFigures + " rematch");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;103m ERROR \033[0m You can only request a rematch after the game is over.
            """);
  }

  @Test
  void drawGameDeclineBeforeRequest() {
    realCLIFromArguments("on " + gameWithDefaultFigures + " draw decline");
    assertThat(cli.game().players().get(FigureColor.WHITE).score()).isEqualTo(0.0);
    assertThat(cli.game().players().get(FigureColor.BLACK).score()).isEqualTo(0.0);
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;103m ERROR \033[0m There is no draw offer to decline.
            """);
  }

  @Test
  void drawGameAcceptBeforeRequest() {
    realCLIFromArguments("on " + gameWithDefaultFigures + " draw accept");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;103m ERROR \033[0m There is no draw offer to accept.
            """);
  }

  @Test
  void drawGameRequest() {
    realCLIFromArguments("on " + gameWithDefaultFigures + " draw offer");
    System.out.println(persistence);
    assertThat(persistence.loadState(State.IS_DRAW_OFFERED)).isEqualTo("1");
    assertThat(persistence.wasSaved()).isTrue();
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;104m ACTION \033[0m You offered a draw. Your opponent can accept or decline this.
            """);
  }

  @Test
  void drawGameRequestMustBeAnswered() {
    realCLIFromArguments("on " + NoPersistence.GameIdType.DRAW_OFFERED.ordinal() + " move a2 a4");
    assertThat(persistence.wasSaved()).isFalse();
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;103m ERROR \033[0m You must first accept or decline the draw offer.
            """);
  }

  @Test
  void drawGameRequestAccepted() {
    realCLIFromArguments("on " + NoPersistence.GameIdType.DRAW_OFFERED.ordinal() + " draw accept");
    assertThat(cli.game().players().get(FigureColor.WHITE).score()).isEqualTo(0.5);
    assertThat(cli.game().players().get(FigureColor.BLACK).score()).isEqualTo(0.5);
    assertThat(persistence.wasSaved()).isTrue();
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;104m ACTION \033[0m The draw offer has been accepted.
            """);
  }

  @Test
  void drawGameRequestDeclined() {
    realCLIFromArguments("on " + NoPersistence.GameIdType.DRAW_OFFERED.ordinal() + " draw decline");
    assertThat(cli.game().players().get(FigureColor.WHITE).score()).isEqualTo(0.0);
    assertThat(cli.game().players().get(FigureColor.BLACK).score()).isEqualTo(0.0);
    assertThat(persistence.loadState(State.IS_DRAW_OFFERED)).isEqualTo("0");
    assertThat(persistence.wasSaved()).isTrue();
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;104m ACTION \033[0m The draw offer has been declined.
            """);
  }

  @Test
  void drawGameRequestWhileRequestAlreadyExists() {
    realCLIFromArguments("on " + NoPersistence.GameIdType.DRAW_OFFERED.ordinal() + " draw offer");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;103m ERROR \033[0m There is already a draw request.
            """);
  }

  @Test
  void rematchDoesNotForgetScore() {
    realCLIFromArguments("on " + NoPersistence.GameIdType.GAME_IS_OVER_DRAW.ordinal() + " rematch");
    assertThat(cli.game().players().get(FigureColor.WHITE).score()).isEqualTo(1);
    assertThat(cli.game().players().get(FigureColor.BLACK).score()).isEqualTo(1);
    assertThat(persistence.wasSaved()).isTrue();
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;104m ACTION \033[0m You have started a new game with the same players.
            \033[37m8 | \033[0mr n b q k b n r
            \033[37m7 | \033[0mp p p p p p p p
            \033[37m6 | \033[0m- - - - - - - -
            \033[37m5 | \033[0m- - - - - - - -
            \033[37m4 | \033[0m- - - - - - - -
            \033[37m3 | \033[0m- - - - - - - -
            \033[37m2 | \033[0mP P P P P P P P
            \033[37m1 | \033[0mR N B Q K B N R
            \033[37m  \\________________\033[0m
            \033[37m    A B C D E F G H\033[0m
            """);
  }

  @Test
  void statsOfRunningGame() {
    realCLIFromArguments("on " + gameWithDefaultFigures + " show-stats");
    assertThat(persistence.wasSaved()).isFalse();
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;104mGame Stats:\033[0m
            """,
            """
            - Elo > Black:    \033[37m1200 points\033[0m
            - Elo > White:    \033[37m1200 points\033[0m
            """,
            """
            - Game ID:          \033[37m
            """,
            """
            - Score > Black:    \033[37m0.0 points\033[0m
            - Score > White:    \033[37m0.0 points\033[0m
            - Status:           \033[37mGame in Progress\033[0m
            """);
  }

  @Test
  void statsOfDrawGame() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.GAME_IS_OVER_DRAW.ordinal() + " show-stats");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            - Status:           \033[37mGame Over (draw by mutual agreement)\033[0m
            """);
  }

  @Test
  void resignationOfGame() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.GAME_IS_OVER_RESIGNATION.ordinal() + " show-stats");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
                    - Status:           \033[37mGame Over (BLACK won -> Resignation)\033[0m
                    """);
  }

  @Test
  void showFenString() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.DEFAULT_POSITIONS.ordinal() + " show-fen");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
                    \033[30;1;104m ACTION \033[0m This is the current game as a FEN-String:
                    rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
                    """);
  }

  @Test
  void boardOfEndedGame() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.GAME_IS_OVER_DRAW.ordinal() + " show-board");
    assertThat(outputStream.toString()).contains("Here is game ").contains("A B C D E F G H");
  }

  @Test
  void testEndGameByDrawStalemate() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.GAME_IS_OVER_DRAW_STALEMATE.ordinal() + " show-stats");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            - Status:           \033[37mGame Over (draw by stalemate)\033[0m
            """);
  }

  @Test
  void testEndGameByDeadPosition() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.GAME_IS_OVER_DRAW_DEAD_POSITION.ordinal() + " show-stats");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            - Status:           \033[37mGame Over (draw by dead position)\033[0m
            """);
  }

  @Test
  void testEndGameByFiftyMoveRule() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.GAME_IS_OVER_FIFTY_MOVE_RULE.ordinal() + " show-stats");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
                    - Status:           \033[37mGame Over (draw by fifty move rule)\033[0m
                    """);
  }

  @Test
  void testEndGameByThreefoldRepetition() {
    realCLIFromArguments(
        "on "
            + NoPersistence.GameIdType.GAME_IS_OVER_THREEFOLD_REPETITION.ordinal()
            + " show-stats");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
                    - Status:           \033[37mGame Over (draw by threefold repetition)\033[0m
                    """);
  }

  @Test
  void statsOfWhiteHasWonGame() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.GAME_IS_OVER_WHITE_WINS.ordinal() + " show-stats");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            - Status:           \033[37mGame Over (WHITE won -> Checkmate)\033[0m
            """);
  }

  @Test
  void invalidSubcommandForDraw() {
    realCLIFromArguments("on " + gameWithDefaultFigures + " draw invalid");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            The command 'chess on <ID> draw invalid' is not supported.
            """);
  }

  @Test
  void noGameDoesNotHaveFenSaved() {
    persistence.setGameId(NoPersistence.GameIdType.NO_GAME.ordinal());
    assertThat(persistence.loadState(State.FEN_HISTORY)).isNull();
    assertThat(persistence.loadState(State.WINNER)).isNull();
  }

  @Test
  void pawnPromotionMustBeDone() {
    realCLIFromArguments("on " + NoPersistence.GameIdType.PAWN_PROMOTION.ordinal() + " move a1 a2");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
              \033[30;1;103m ERROR \033[0m You must first promote the pawn to a different figure
              """);
  }

  @Test
  void pawnPromotionHintIsShown() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.PAWN_PROMOTION_POSSIBLE.ordinal() + " move a7 a8");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
                    \033[30;1;103m ERROR \033[0m Please promote your pawn to a different figure. You can choose QUEEN, ROOK, BISHOP or KNIGHT.
                    """);
  }

  @Test
  void fiftyMoveRuleHintIsShown() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.FIFTY_MOVE_POSSIBLE.ordinal() + " move a8 b8");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
                    \033[30;1;103m ERROR \033[0m Fifty moves have been reached. The game ended with a draw
                    """);
  }

  @Test
  void deadPositionRuleHintIsShown() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.DEAD_POSITION_POSSIBLE.ordinal() + " move a8 b7");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;103m ERROR \033[0m There are too few figures to result in a checkmate. The game ended with a draw
            """);
  }

  @Test
  void threefoldRepetitionHintIsShown() {
    realCLIFromArguments(
        "on " + NoPersistence.GameIdType.THREEFOLD_REPETITION_POSSIBLE.ordinal() + " move b8 a8");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;103m ERROR \033[0m The game ended in a draw by threefold repetition.
            """);
  }

  @Test
  void showErrorOnInvalidSaveGameFile() {
    realCLIFromArguments("on " + NoPersistence.GameIdType.NO_GAME.ordinal() + " show-board");
    assertThat(outputStream.toString())
        .containsIgnoringWhitespaces(
            """
            \033[30;1;103m ERROR \033[0m Your save-file is invalid because it is missing:
            """,
            """
            Create a new game with 'chess create <ID>'.
            """);
  }
}
