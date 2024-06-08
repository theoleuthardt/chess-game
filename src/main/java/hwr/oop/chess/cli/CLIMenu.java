package hwr.oop.chess.cli;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.ChessGame;
import hwr.oop.chess.application.EndType;
import hwr.oop.chess.application.figures.*;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CLIMenu {
  private final CLIAdapter cli;
  private final CLIPrinter printer;
  private List<String> initialCommand;
  private List<String> remainingArguments;

  private final Map<String, String> commandAndExplanation =
      Map.ofEntries(
          new AbstractMap.SimpleEntry<>("1  Basic Game commands", ""),
          new AbstractMap.SimpleEntry<>(
              "1: chess create <ID>", "Create a new, fresh game (stored in game_<ID>.csv)"),
          new AbstractMap.SimpleEntry<>(
              "1: chess on <ID> move <FROM> <TO>", "Move the figure on FROM to the cell TO"),
          new AbstractMap.SimpleEntry<>(
              "1: on <ID> promote <FROM> <TYPE>", "Promote the pawn on cell FROM"),
          new AbstractMap.SimpleEntry<>(
              "2  Show Status", "--------------------------------------------------"),
          new AbstractMap.SimpleEntry<>(
              "2: chess on <ID> show-board", "Show the current state of the board"),
          new AbstractMap.SimpleEntry<>(
              "2: chess on <ID> show-moves <FROM>", "Show where the figure can move to"),
          new AbstractMap.SimpleEntry<>(
              "2: chess on <ID> show-moveable", "Show figures which can be moved"),
          new AbstractMap.SimpleEntry<>("2: chess on <ID> show-stats", "Show score of the players"),
          new AbstractMap.SimpleEntry<>(
              "3  End of Game", "--------------------------------------------------"),
          new AbstractMap.SimpleEntry<>(
              "3: chess on <ID> draw offer", "Offer draw to the other player"),
          new AbstractMap.SimpleEntry<>("3: chess on <ID> draw accept", "Accept the draw offer"),
          new AbstractMap.SimpleEntry<>("3: chess on <ID> draw decline", "Decline the draw offer"),
          new AbstractMap.SimpleEntry<>(
              "3: chess on <ID> resign", "End the game by accepting a loss"),
          new AbstractMap.SimpleEntry<>(
              "3: chess on <ID> rematch", "Start a new game without resetting your score"));

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
        case "on" -> performActionOnBoard();
        default ->
            throw new InvalidUserInputException(
                "The command '"
                    + command
                    + "' is not supported. Try 'help' to see available commands.");
      }
    } catch (InvalidUserInputException e) {
      printer.printlnError(e);
      if (cli.game() != null) {
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

    if (cli.game().isOver() && !command.equals("rematch")) {
      printImportantGameStatus();
      if (!command.equals("show-stats") && !command.equals("show-board")) {
        return;
      }
    }

    if (cli.game().isDrawOffered() && !command.equals("draw")) {
      throw new InvalidUserInputException("You must first accept or decline the draw offer.");
    }

    if (cli.game().board().isPawnPromotionPossible() && !command.equals("promote")) {
      throw new InvalidUserInputException("You must first promote the pawn to a different figure");
    }

    switch (command) {
      case "move" -> performMoveFigureOnBoard();
      case "promote" -> performPromotePawnOnBoard();

      case "show-stats" -> printStats();
      case "show-board" -> showBoardStatus();
      case "show-moves" -> showMovesOfFigure();
      case "show-moveable" -> showMoveableFigures();

      case "draw" -> performDraw();
      case "resign" -> performResign();
      case "rematch" -> performRematch();
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
    printImportantGameStatus();
    handleAutomaticGameEnd();
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

  private void showMoveableFigures() {
    countOfRemainingArgumentsIs(0);
    Board board = cli.game().board();
    printer.printlnAction(
        "Showing the figures which can be moved by the " + board.turn().name() + " player.");
    for (Cell cell : board.cellsWithColor(board.turn())) {
      if (!board.availableCellsWithoutCheckMoves(cell).isEmpty()) {
        cli.printer().alsoHighlightOnBoard(cell);
      }
    }
    cli.printBoard();
  }

  private void performDraw() {
    countOfRemainingArgumentsIs(1);
    String command = remainingArguments.removeFirst();
    switch (command) {
      case "offer" -> {
        if (cli.game().isDrawOffered()) {
          throw new InvalidUserInputException("There is already a draw request.");
        }
        cli.game().offerDraw();
        cli.game().saveGame();
        printer.printlnAction("You offered a draw. Your opponent can accept or decline this.");
      }
      case "accept" -> {
        if (!cli.game().isDrawOffered()) {
          throw new InvalidUserInputException("There is no draw offer to accept.");
        }
        printer.printlnAction("The draw offer has been accepted.");
        cli.game().endsWithDraw();
        cli.game().saveGame();
      }

      case "decline" -> {
        if (!cli.game().isDrawOffered()) {
          throw new InvalidUserInputException("There is no draw offer to decline.");
        }
        printer.printlnAction("The draw offer has been declined.");
        cli.game().denyDrawOffer();
        cli.game().saveGame();
      }
      default ->
          throw new InvalidUserInputException(
              "The command 'chess on <ID> draw " + command + "' is not supported.");
    }
  }

  private void performResign() {
    countOfRemainingArgumentsIs(0);
    printer.printlnAction("You resigned the game. Your opponent has won.");
    cli.game().playerHasWon(cli.game().board().turn().opposite());
    cli.game().saveGame();
  }

  private void handleAutomaticGameEnd() {
    ChessGame game = cli.game();
    Board board = game.board();

    for (FigureColor color : FigureColor.values()) {
      switch (board.endType(color)){
        case EndType.STALEMATE, EndType.DEAD_POSITION ->  game.endsWithDraw();
        case EndType.CHECKMATE ->  game.playerHasWon(color.opposite());
        default -> {
          // The game continues
        }
      }
    }
  }

  private void printImportantGameStatus() {
    Board board = cli.game().board();
    for (FigureColor color : FigureColor.values()) {
      if (board.isStalemate(color)) {
        printer.printlnError("The " + color.name() + " king is in stalemate. The game is over.");
      } else if (board.isCheckmate(color)) {
        printer.printlnError("The " + color.name() + " king is in checkmate. The game is over.");
      } else if (board.isCheck(color)) {
        printer.printlnError("The " + color.name() + " king is in check.");
      }
    }

    if (cli.game().board().isPawnPromotionPossible()) {
      printer.printlnError(
          "Please promote your pawn to a different figure. You can choose QUEEN, ROOK, BISHOP or KNIGHT.");
    }

    if (cli.game().isOver()) {
      String winner = cli.persistence().loadState("winner");
      if (winner == null || winner.equals("draw")) {
        printer.printlnError("The game ended with a draw. Both players got half a point.");
      } else {
        printer.printlnError("The " + winner + " player has already won the game.");
      }
    }
  }

  private void printStats() {
    countOfRemainingArgumentsIs(0);
    ChessGame game = cli.game();

    Map<String, String> stats = new HashMap<>();

    stats.put("Game ID", "" + cli.persistence().gameId());
    String gameStatus = "Game in Progress";
    if (game.isOver()) {
      String winner = cli.persistence().loadState("winner");
      gameStatus = "Game Over (" + (winner.equals("draw") ? "draw)" : winner + " won)");
    }
    stats.put("Status", gameStatus);

    stats.put("Score > White", game.players().get(FigureColor.WHITE).score() + " points");
    stats.put("Score > Black", game.players().get(FigureColor.BLACK).score() + " points");

    printer.printAsTable("Game Stats:", 20, stats);
  }

  private void performRematch() {
    countOfRemainingArgumentsIs(0);
    if (!cli.game().isOver()) {
      throw new InvalidUserInputException("You can only request a rematch after the game is over.");
    }
    printer.printlnAction("You have started a new game with the same players.");
    ChessGame oldGame = cli.game();
    cli.initializeGame(true).keepPlayersOf(oldGame).saveGame();
    cli.printBoard();
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
