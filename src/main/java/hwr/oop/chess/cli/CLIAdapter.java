package hwr.oop.chess.cli;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.ChessGame;
import hwr.oop.chess.application.figures.Figure;
import hwr.oop.chess.application.figures.FigureType;
import hwr.oop.chess.application.figures.Pawn;
import hwr.oop.chess.persistence.GameDataManager;
import hwr.oop.chess.persistence.NoPersistence;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class CLIAdapter {
  private static final String RESET_TEXT = "\033[0m";

  private final PrintStream printStream;
  private final GameDataManager persistence;
  private final List<Cell> cellHighlight = new ArrayList<>();
  private int gameId = -1;
  private ChessGame game;

  public CLIAdapter(OutputStream outputStream) {
    this(outputStream, new NoPersistence());
  }

  public CLIAdapter(OutputStream outputStream, GameDataManager persistence) {
    this.printStream = new PrintStream(outputStream);
    this.persistence = persistence;
  }

  public void println() {
    printStream.println();
  }

  public void println(String message) {
    printStream.println(message);
  }

  public void print(String message) {
    printStream.print(message);
  }

  public void printYellow(String message) {
    printStream.print("\033[30;1;103m" + message + RESET_TEXT);
  }

  public void printBlue(String message) {
    printStream.print("\033[30;1;104m" + message + RESET_TEXT);
  }

  public void printGray(String message) {
    printStream.print("\033[37m" + message + RESET_TEXT);
  }

  public void printlnGray(String message) {
    printGray(message);
    println();
  }

  public void printlnError(InvalidUserInputException e) {
    printlnError(e.getMessage());
    if (gameId >= 0 && game != null && game.board() != null) {
      game.board().printBoard(cellHighlight);
    }
  }

  public void printlnError(String message) {
    printYellow(" ERROR ");
    printStream.println(" " + message);
  }

  public void printlnAction(String message) {
    printBlue(" ACTION ");
    printStream.println(" " + message);
  }

  public GameDataManager persistence() {
    return persistence;
  }

  public void forGameId(String gameNumber) {
    gameId = Integer.parseInt(gameNumber);
    persistence.loadGame(gameId);
  }

  public int gameId() {
    return gameId;
  }

  public ChessGame createGame(String gameNumber) {
    gameId = Integer.parseInt(gameNumber);
    game = new ChessGame(this);
    printlnAction("Here is your new game '" + gameId + "'. Good luck and have fun.");
    game.board().printBoard();
    game.saveGame(gameId);
    return game;
  }

  public void showBoard() {
    game = new ChessGame(this);
    printlnAction("Here is game '#" + gameId + "'");
    game.board().printBoard();
  }

  public Cell requireArgumentIsCoordinate(Board board, String coordinate) {
    Cell cell = board.findCell(coordinate);
    if (cell != null) {
      cellHighlight.add(cell);
      return cell;
    }
    throw new InvalidUserInputException(
        "The XY-Coordinates must be between A-H and 1-8 (eg. c3, a7). Please correct '"
            + coordinate
            + "'.");
  }

  public Figure requireArgumentIsCellWithFigure(Board board, String coordinate) {
    Cell cell = requireArgumentIsCoordinate(board, coordinate);
    return requireCellHasFigure(cell);
  }

  public FigureType requireArgumentIsFigureType(String figure) {
    return FigureType.fromString(figure);
  }

  public Figure requireCellHasFigure(Cell cell) {
    if (cell.figure() != null) {
      return cell.figure();
    }
    throw new InvalidUserInputException(
        "On the cell " + cell.toCoordinates() + " there is no figure!");
  }

  public void performActionOnBoard(String command, List<String> arguments) {
    game = new ChessGame(this);
    switch (command) {
      case "move" -> performActionOnBoardMoveFigure(arguments);
      case "promote" -> performActionOnBoardPromotePawn(arguments);
      case "show-moves" -> performActionOnBoardShowMoves(arguments);
      default -> throw new InvalidUserInputException("Invalid command: " + command);
    }
    game.saveGame(gameId);
  }

  private void performActionOnBoardMoveFigure(List<String> arguments) {
    Board board = game.board();
    Cell from = requireArgumentIsCoordinate(board, arguments.removeFirst());
    Figure figure = requireCellHasFigure(from);
    Cell to = requireArgumentIsCoordinate(board, arguments.removeFirst());

    printlnAction(
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
    board.printBoard(cellHighlight);
  }

  private void performActionOnBoardPromotePawn(List<String> arguments) {
    Board board = game.board();
    Cell from = requireArgumentIsCoordinate(board, arguments.removeFirst());
    Figure figure = requireCellHasFigure(from);
    if (figure.type() != FigureType.PAWN) {
      throw new InvalidUserInputException(
          "On the cell " + from.toCoordinates() + " there is no pawn!");
    }

    FigureType promoteToType = requireArgumentIsFigureType(arguments.removeFirst());
    printlnAction(
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
    board.printBoard(cellHighlight);
  }

  private void performActionOnBoardShowMoves(List<String> arguments) {
    Board board = game.board();
    Cell from = requireArgumentIsCoordinate(board, arguments.removeFirst());
    Figure figure = requireCellHasFigure(from);
    printlnAction(
        "Showing the cells where the "
            + figure.type().name()
            + " on "
            + from.toCoordinates()
            + " can move to.");
    board.printBoard(figure.getAvailableCells(from));
  }
}
