package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.cli.InvalidUserInputException;
import hwr.oop.chess.persistence.FenNotation;
import hwr.oop.chess.persistence.Persistence;
import hwr.oop.chess.persistence.Player;

import java.util.*;

public class ChessGame {
  private final Persistence persistence;
  private final Board board;
  private boolean isDrawOffered;
  private final Map<FigureColor, Player> players = new EnumMap<>(FigureColor.class);
  private final List<String> fenHistory = new ArrayList<>();
  private EndType endType = EndType.NOT_END;
  private static final String STATE_FEN_HISTORY = "fen";
  private static final String STATE_WINNER = "winner";
  private static final String STATE_WHITE_SCORE = "whiteScore";
  private static final String STATE_BLACK_SCORE = "blackScore";
  private static final String STATE_END_TYPE = "endType";
  private static final String STATE_IS_DRAW_OFFERED = "isDrawOffered";

  public ChessGame(Persistence persistence, boolean isNew) {
    this.persistence = persistence;
    board = new Board(false);
    if (isNew) {
      newGame();
    } else {
      loadGame();
    }
  }

  private void newGame() {
    isDrawOffered = false;
    board.addFiguresToBoard();
    players.put(FigureColor.WHITE, new Player("0"));
    players.put(FigureColor.BLACK, new Player("0"));
  }

  private void loadGame() {
    persistence.loadGame();
    List<String> missingStates =
        new ArrayList<>(
            List.of(
                STATE_END_TYPE,
                STATE_IS_DRAW_OFFERED,
                STATE_FEN_HISTORY,
                STATE_WHITE_SCORE,
                STATE_BLACK_SCORE));
    missingStates.removeIf(state -> persistence.loadState(state) != null);
    if (!missingStates.isEmpty()) {
      throw new InvalidUserInputException(
          "Your save-file is invalid because it is missing: "
              + missingStates
              + "! Create a new game with 'chess create <ID>'.");
    }
    endType = EndType.valueOf(persistence.loadState(STATE_END_TYPE));
    isDrawOffered = "1".equals(persistence.loadState(STATE_IS_DRAW_OFFERED));
    players.put(FigureColor.WHITE, new Player(persistence.loadState(STATE_WHITE_SCORE)));
    players.put(FigureColor.BLACK, new Player(persistence.loadState(STATE_BLACK_SCORE)));

    String currentFen = parseHistoryAndGetCurrentFen(persistence.loadState(STATE_FEN_HISTORY));
    FenNotation.parseFEN(board, currentFen);
  }

  private String parseHistoryAndGetCurrentFen(String history) {
    Collections.addAll(fenHistory, history.split(","));
    return fenHistory.removeLast();
  }

  private String historyOfMoves() {
    this.fenHistory.add(FenNotation.generateFen(board));
    return String.join(",", this.fenHistory);
  }

  public void saveGame() {
    persistence.storeState(STATE_END_TYPE, endType.name());
    persistence.storeState(STATE_IS_DRAW_OFFERED, isDrawOffered ? "1" : "0");
    persistence.storeState(STATE_FEN_HISTORY, historyOfMoves());
    persistence.storeState(
        STATE_WHITE_SCORE, String.valueOf(players.get(FigureColor.WHITE).doubleOfScore()));
    persistence.storeState(
        STATE_BLACK_SCORE, String.valueOf(players.get(FigureColor.BLACK).doubleOfScore()));
    persistence.saveGame();
  }

  public Board board() {
    return board;
  }

  public void playerHasWon(EndType type, FigureColor color) {
    endType = type;
    players.get(color).fullPointOnWin();
    persistence.storeState(STATE_WINNER, color.name());
  }

  public void endsWithDraw(EndType type) {
    endType = type;
    isDrawOffered = false;
    persistence.storeState(STATE_WINNER, "draw");
    players.get(FigureColor.WHITE).halfPointOnDraw();
    players.get(FigureColor.BLACK).halfPointOnDraw();
  }

  public Map<FigureColor, Player> players() {
    return players;
  }

  public boolean isOver() {
    return endType != EndType.NOT_END;
  }

  public boolean isDrawOffered() {
    return isDrawOffered;
  }

  public void offerDraw() {
    isDrawOffered = true;
  }

  public void denyDrawOffer() {
    isDrawOffered = false;
  }

  public ChessGame keepPlayersOf(ChessGame game) {
    players.put(FigureColor.WHITE, game.players().get(FigureColor.WHITE));
    players.put(FigureColor.BLACK, game.players().get(FigureColor.BLACK));
    return this;
  }

  public List<String> fenHistory() {
    return this.fenHistory;
  }
}
