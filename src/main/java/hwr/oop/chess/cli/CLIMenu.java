package hwr.oop.chess.cli;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.Game;
import hwr.oop.chess.application.EndType;
import hwr.oop.chess.application.figures.*;
import hwr.oop.chess.persistence.FenNotation;
import hwr.oop.chess.persistence.Player;
import hwr.oop.chess.persistence.PortableGameNotation;
import hwr.oop.chess.persistence.State;

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
              "2: chess on <ID> show-fen", "Show the current game as a FEN-String"),
          new AbstractMap.SimpleEntry<>(
              "2: chess on <ID> show-pgn", "Show the current game as PGN-String"),
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

    printImportantGameStatus();
    if (cli.game().isOver()
        && !List.of("rematch", "show-stats", "show-board", "show-fen", "show-pgn")
            .contains(command)) {
      return;
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
      case "show-fen" -> exportAsFenNotation();
      case "show-pgn" -> exportAsPgnNotation();

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

    cli.game().rememberAndPerformMove(from, to);
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

    cli.game().rememberAndPerformPawnPromotion(from, promoteToType);
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
    printer.setHighlightOnBoard(board.availableCellsWithoutCheckMoves(from));
    cli.printBoard();
  }

  private void showMoveableFigures() {
    countOfRemainingArgumentsIs(0);
    Board board = cli.game().board();
    printer.printlnAction(
        "Showing the figures which can be moved by the " + board.turn().name() + " player.");
    for (Cell cell : board.cellsWithColor(board.turn())) {
      if (!board.availableCellsWithoutCheckMoves(cell).isEmpty()) {
        printer.alsoHighlightOnBoard(cell);
      }
    }
    cli.printBoard();
  }

  private void performDraw() {
    countOfRemainingArgumentsIs(1);
    String command = remainingArguments.removeFirst();
    final Game game = cli.game();
    switch (command) {
      case "offer" -> {
        if (game.isDrawOffered()) {
          throw new InvalidUserInputException("There is already a draw request.");
        }
        game.offerDraw();
        game.saveGame();
        printer.printlnAction("You offered a draw. Your opponent can accept or decline this.");
      }
      case "accept" -> {
        if (!game.isDrawOffered()) {
          throw new InvalidUserInputException("There is no draw offer to accept.");
        }
        printer.printlnAction("The draw offer has been accepted.");
        game.endsWithDraw(EndType.MUTUAL_DRAW);
        game.saveGame();
      }

      case "decline" -> {
        if (!game.isDrawOffered()) {
          throw new InvalidUserInputException("There is no draw offer to decline.");
        }
        printer.printlnAction("The draw offer has been declined.");
        game.denyDrawOffer();
        game.saveGame();
      }
      default ->
          throw new InvalidUserInputException(
              "The command 'chess on <ID> draw " + command + "' is not supported.");
    }
  }

  private void performResign() {
    countOfRemainingArgumentsIs(0);
    printer.printlnAction("You resigned the game. Your opponent has won.");
    cli.game().playerHasWon(EndType.RESIGNATION, cli.game().board().turn().ofOpponent());
    cli.game().saveGame();
  }

  private void handleAutomaticGameEnd() {
    Game game = cli.game();
    if (game.isThreeFoldRepetition()) {
      game.endsWithDraw(EndType.THREE_FOLD_REPETITION);
      return;
    }

    Board board = game.board();
    for (FigureColor color : FigureColor.values()) {
      EndType endType = board.endType(color);
      switch (endType) {
        case EndType.STALEMATE, EndType.DEAD_POSITION -> game.endsWithDraw(endType);
        case EndType.CHECKMATE -> game.playerHasWon(endType, color.ofOpponent());
        default -> {
          // The game continues
        }
      }
    }
  }

  private void printImportantGameStatus() {
    Game game = cli.game();
    Board board = game.board();
    for (FigureColor color : FigureColor.values()) {
      if (board.isStalemate(color)) {
        printer.printlnError("The " + color.name() + " king is in stalemate. The game is over.");
        return;
      } else if (board.isCheckmate(color)) {
        printer.printlnError("The " + color.name() + " king is in checkmate. The game is over.");
        return;
      } else if (board.isCheck(color)) {
        printer.printlnError("The " + color.name() + " king is in check.");
      }
    }

    if (board.isFiftyMoveEnd()) {
      printer.printlnError("Fifty moves have been reached. The game ended with a draw");
    } else if (board.isDeadPosition()) {
      printer.printlnError(
          "There are too few figures to result in a checkmate. The game ended with a draw.");
    } else if (game.isThreeFoldRepetition()) {
      printer.printlnError("The game ended in a draw by threefold repetition.");
    } else if (board.isPawnPromotionPossible()) {
      printer.printlnError(
          "Please promote your pawn to a different figure. You can choose QUEEN, ROOK, BISHOP or KNIGHT.");
    } else if (game.isOver()) {
      String winner = cli.persistence().loadState(State.WINNER);
      if (winner == null || winner.equals("draw")) {
        printer.printlnError("The game ended with a draw. Both players got half a point.");
      } else {
        printer.printlnError("The " + winner + " player has already won the game.");
      }
    }
  }

  private void printStats() {
    countOfRemainingArgumentsIs(0);
    Game game = cli.game();

    Map<String, String> stats = new HashMap<>();

    stats.put("Game ID", "" + cli.persistence().gameId());
    String winnerColor = cli.persistence().loadState(State.WINNER);
    String gameStatus =
        switch (EndType.valueOf(cli.persistence().loadState(State.END_TYPE))) {
          case NOT_END -> "Game in Progress";

          case CHECKMATE -> winnerColor + " won -> Checkmate";
          case RESIGNATION -> winnerColor + " won -> Resignation";

          case MUTUAL_DRAW -> "draw by mutual agreement";
          case STALEMATE -> "draw by stalemate";
          case DEAD_POSITION -> "draw by dead position";
          case THREE_FOLD_REPETITION -> "draw by threefold repetition";
          case FIFTY_MOVE_RULE -> "draw by fifty move rule";
        };
    if (!gameStatus.equals("Game in Progress")) {
      gameStatus = "Game Over (" + gameStatus + ")";
    }
    stats.put("Status", gameStatus);

    Player whitePlayer = game.player(FigureColor.WHITE);
    Player blackPlayer = game.player(FigureColor.BLACK);
    stats.put("Score > White", whitePlayer.score() + " points (Elo: " + whitePlayer.elo() + ")");
    stats.put("Score > Black", blackPlayer.score() + " points (Elo: " + blackPlayer.elo() + ")");

    printer.printAsTable("Game Stats:", 20, stats);
  }

  private void performRematch() {
    countOfRemainingArgumentsIs(0);
    if (!cli.game().isOver()) {
      throw new InvalidUserInputException("You can only request a rematch after the game is over.");
    }
    printer.printlnAction("You have started a new game with the same players.");
    Game oldGame = cli.game();
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

  private void exportAsFenNotation() {
    countOfRemainingArgumentsIs(0);
    printer.printlnAction("This is the current game as a FEN-String:");
    printer.println(FenNotation.generateFen(cli.game().board()));
  }

  private void exportAsPgnNotation() {
    countOfRemainingArgumentsIs(0);
    printer.printlnAction("This is the current game as a PGN-String:");
    printer.println(PortableGameNotation.generatePgn(cli.game()));
  }
}
