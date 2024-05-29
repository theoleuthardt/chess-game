package hwr.oop.chess.cli;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.figures.Figure;
import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.application.figures.FigureType;
import hwr.oop.chess.application.figures.Pawn;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class CLIMenu {
  private final CLIAdapter cli;
  private final CLIPrinter printer;
  private List<String> initialCommand;
  private List<String> remainingArguments;

  private final Map<String, String> commandAndExplanation =
      Map.ofEntries(
          new AbstractMap.SimpleEntry<>("chess create <ID>", "Create a new game of chess"),
          new AbstractMap.SimpleEntry<>("chess show <ID>", "Show the board"),
          new AbstractMap.SimpleEntry<>("chess on <ID> move <FROM> <TO>", "Move a figure"),
          new AbstractMap.SimpleEntry<>(
              "chess on <ID> show-moves <FROM>", "Show where the figure can go"),
          new AbstractMap.SimpleEntry<>("chess on <ID> promote <FROM> <TYPE>", "Promote a pawn"));

  private final Map<String, String> parameterTypes =
      Map.ofEntries(
          new AbstractMap.SimpleEntry<>("<ID>", "Game ID (e.g. 123)"),
          new AbstractMap.SimpleEntry<>("<FROM>", "XY-Coordinate of the cell (e.g. a1, c4, 22)"),
          new AbstractMap.SimpleEntry<>("<TYPE>", "Figure (e.g. knight, rook, queen)"));

  public CLIMenu(CLIAdapter cli) {
    this.cli = cli;
    this.printer = cli.printer();
  }

  public void withArguments(List<String> arguments) {
    initialCommand = arguments.stream().toList();
    remainingArguments = arguments;
  }

  public void handle(List<String> arguments) {
    initialCommand = arguments.stream().toList();
    if (arguments.isEmpty()) {
      arguments.add("help");
    }
    remainingArguments = arguments;

    printer.println();
    try {
      String command = arguments.removeFirst();
      if (List.of("create", "show", "on").contains(command)) {
        argumentToGameId();
      }

      switch (command) {
        case "help" -> printHelpMenu();
        case "create" -> startNewGame();
        case "show" -> showBoardStatus();
        case "on" -> performActionOnBoard();
        default ->
            throw new InvalidUserInputException(
                "The command '"
                    + command
                    + "' is not supported. Try 'help' to see available commands.");
      }
    } catch (InvalidUserInputException e) {
      printer.printlnError(e);
      if (cli.game() != null && cli.persistence().gameId() > 0) {
        cli.printBoard();
      }
    }
    printer.println();
  }

  private void printHelpMenu() {
    printer.printAsTable("Chess Commands:", 40, commandAndExplanation);
    printer.printAsTable("Parameter Types:", 10, parameterTypes);
  }

  private void startNewGame() {
    countOfRemainingArgumentsIs(0);
    printer.printlnAction(
        "Here is your new game '" + cli.persistence().gameId() + "'. Good luck and have fun.");
    cli.initializeGame(true).saveGame();
    cli.printBoard();
  }

  private void showBoardStatus() {
    countOfRemainingArgumentsIs(0);
    printer.printlnAction("Here is game '" + cli.persistence().gameId() + "'.");
    cli.initializeGame(false);
    cli.printBoard();
  }

  private void performActionOnBoard() {
    cli.initializeGame(false);
    if (remainingArguments.isEmpty()) {
      throw new InvalidUserInputException(
          "You must specify the action you would like to perform on the board '"
              + cli.persistence().gameId()
              + "'.");
    }
    String command = remainingArguments.removeFirst();
    switch (command) {
      case "move" -> performMoveFigureOnBoard();
      case "promote" -> performPromotePawnOnBoard();
      case "show-moves" -> showMovesOfFigure();
      default ->
          throw new InvalidUserInputException(
              "The command 'chess on <ID> "
                  + command
                  + "' is not supported. Try 'help' to see available commands.");
    }
  }

  private void performMoveFigureOnBoard() {
    Board board = cli.game().board();
    Cell from = argumentToCoordinate(board);
    Figure figure = mustBeCellWithFigure(from);
    Cell to = argumentToCoordinate(board);

    printer.printlnAction(
        "Move "
            + figure.color().name()
            + " "
            + figure.type().name()
            + " from "
            + from.toCoordinates()
            + " to "
            + to.toCoordinates()
            + ".");

    board.moveFigure(from, to);
    checkForCheck();
    cli.game().saveGame();
    cli.printBoard();
  }

  private void performPromotePawnOnBoard() {
    Board board = cli.game().board();
    Cell from = argumentToCoordinate(board);
    if (!from.isOccupiedBy(FigureType.PAWN)) {
      throw new InvalidUserInputException(
          "On the cell " + from.toCoordinates() + " there is no pawn!");
    }

    Figure figure = from.figure();
    FigureType promoteToType = argumentToFigureType();
    printer.printlnAction(
        "Promote "
            + figure.color().name()
            + " "
            + figure.type().name()
            + " on "
            + from.toCoordinates()
            + " to become a "
            + promoteToType.name()
            + ".");

    Pawn pawn = (Pawn) figure;
    pawn.promotePawn(from, promoteToType);
    cli.game().saveGame();
    cli.printBoard();
  }

  private void showMovesOfFigure() {
    Board board = cli.game().board();
    Cell from = argumentToCoordinate(board);
    Figure figure = mustBeCellWithFigure(from);
    printer.printlnAction(
        "Showing the cells where the "
            + figure.type().name()
            + " on "
            + from.toCoordinates()
            + " can move to.");
    cli.printer().setHighlightOnBoard(board.availableCellsWithoutCheckMoves(from));
    cli.printBoard();
  }

  private void checkForCheck() {
    if (cli.game().board().isCheck(FigureColor.WHITE)) {
      this.cli.printer().printlnError("The white king is in check!");
    }
    if (cli.game().board().isCheck(FigureColor.BLACK)) {
      this.cli.printer().printlnError("The black king is in check!");
    }
  }

  public Cell argumentToCoordinate(Board board) {
    if (remainingArguments.isEmpty()) {
      throw new InvalidUserInputException("You must provide a coordinate for this command.");
    }
    String coordinate = remainingArguments.removeFirst();
    Cell cell;
    try {
      cell = board.findCell(coordinate);
    } catch (IllegalArgumentException e) {
      throw new InvalidUserInputException(
          "The XY-Coordinates must be between A-H and 1-8 (eg. c3, a7). Please correct '"
              + coordinate
              + "'.");
    }
    printer.alsoHighlightOnBoard(cell);
    return cell;
  }

  public FigureType argumentToFigureType() {
    if (remainingArguments.isEmpty()) {
      throw new InvalidUserInputException("You must provide a figure type for this command.");
    }
    return FigureType.fromString(remainingArguments.removeFirst());
  }

  public void argumentToGameId() {
    if (remainingArguments.isEmpty()) {
      throw new InvalidUserInputException("You must provide a game id for this command.");
    }
    String argument = remainingArguments.removeFirst();
    if (!argument.chars().allMatch(Character::isDigit)) {
      throw new InvalidUserInputException(
          "You must provide a positive integer as the game id. Please check '"
              + argument
              + "' for typos.");
    }

    cli.persistence().setGameId(Integer.parseInt(argument));
  }

  public Figure mustBeCellWithFigure(Cell cell) {
    if (cell.isFree()) {
      throw new InvalidUserInputException(
          "On the cell " + cell.toCoordinates() + " there is no figure!");
    }
    return cell.figure();
  }

  public void countOfRemainingArgumentsIs(int shouldHaveCount) {
    int actuallyHasCount = remainingArguments.size();
    if (actuallyHasCount < shouldHaveCount) {
      throw new InvalidUserInputException(
          "Your command '"
              + String.join(" ", initialCommand)
              + "' is missing "
              + (shouldHaveCount - actuallyHasCount)
              + " argument(s)");
    }
    if (actuallyHasCount > shouldHaveCount) {
      throw new InvalidUserInputException(
          "Your command '"
              + String.join(" ", initialCommand)
              + "' has "
              + (actuallyHasCount - shouldHaveCount)
              + " argument(s) more than needed.");
    }
  }
}
