package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.cli.InvalidUserInputException;
import hwr.oop.chess.persistence.FenNotation;
import hwr.oop.chess.persistence.Persistence;
import hwr.oop.chess.persistence.Player;
import hwr.oop.chess.persistence.State;

import java.util.*;

public class ChessGame implements Game {
  private final Persistence persistence;
  private final Board board;
  private boolean isDrawOffered;
  private final Map<FigureColor, Player> players = new EnumMap<>(FigureColor.class);
  private final List<String> fenHistory = new ArrayList<>();
  private EndType endType = EndType.NOT_END;

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
    players.put(FigureColor.WHITE, new Player(0, 1200, 0));
    players.put(FigureColor.BLACK, new Player(0, 1200, 0));
  }

  private void loadGame() {
    persistence.loadGame();
    List<State> missingStates =
        new ArrayList<>(
            List.of(
                State.END_TYPE,
                State.IS_DRAW_OFFERED,
                State.FEN_HISTORY,
                State.WHITE_SCORE,
                State.BLACK_SCORE,
                State.WHITE_ELO,
                State.BLACK_ELO,
                State.WHITE_GAME_COUNT,
                State.BLACK_GAME_COUNT));
    missingStates.removeIf(state -> persistence.loadState(state) != null);
    if (!missingStates.isEmpty()) {
      throw new InvalidUserInputException(
          "Your save-file is invalid because it is missing: "
              + missingStates
              + "! Create a new game with 'chess create <ID>'.");
    }
    endType = EndType.valueOf(persistence.loadState(State.END_TYPE));
    isDrawOffered = "1".equals(persistence.loadState(State.IS_DRAW_OFFERED));
    players.put(
        FigureColor.WHITE,
        new Player(
            persistence.loadState(State.WHITE_SCORE),
            persistence.loadState(State.WHITE_ELO),
            persistence.loadState(State.WHITE_GAME_COUNT)));
    players.put(
        FigureColor.BLACK,
        new Player(
            persistence.loadState(State.BLACK_SCORE),
            persistence.loadState(State.BLACK_ELO),
            persistence.loadState(State.BLACK_GAME_COUNT)));

    String currentFen = parseHistoryAndGetCurrentFen(persistence.loadState(State.FEN_HISTORY));
    FenNotation.parseFEN(board, currentFen);
  }

  private String parseHistoryAndGetCurrentFen(String history) {
    Collections.addAll(fenHistory, history.split(","));
    return fenHistory.getLast();
  }

  private String historyOfMoves() {
    this.fenHistory.add(FenNotation.generateFen(board));
    return String.join(",", this.fenHistory);
  }

  @Override
  public void saveGame() {
    persistence.storeState(State.END_TYPE, endType.name());
    persistence.storeState(State.IS_DRAW_OFFERED, isDrawOffered ? "1" : "0");
    persistence.storeState(State.FEN_HISTORY, historyOfMoves());

    Player whitePlayer = players.get(FigureColor.WHITE);
    persistence.storeState(State.WHITE_SCORE, String.valueOf(whitePlayer.score()));
    persistence.storeState(State.WHITE_ELO, String.valueOf(whitePlayer.elo()));
    persistence.storeState(State.WHITE_GAME_COUNT, String.valueOf(whitePlayer.gameCount()));

    Player blackPlayer = players.get(FigureColor.BLACK);
    persistence.storeState(State.BLACK_SCORE, String.valueOf(blackPlayer.score()));
    persistence.storeState(State.BLACK_ELO, String.valueOf(blackPlayer.elo()));
    persistence.storeState(State.BLACK_GAME_COUNT, String.valueOf(blackPlayer.gameCount()));
    persistence.saveGame();
  }

  @Override
  public Board board() {
    return board;
  }

  @Override
  public void playerHasWon(EndType type, FigureColor color) {
    endType = type;
    persistence.storeState(State.WINNER, color.name());

    Player winner = players.get(color);
    Player looser = players.get(color.opposite());
    double winnerElo = winner.elo();
    double looserElo = looser.elo();
    winner.adjustScoreOnGameEnd(1, looserElo);
    looser.adjustScoreOnGameEnd(0, winnerElo);
  }

  @Override
  public void endsWithDraw(EndType type) {
    endType = type;
    isDrawOffered = false;
    persistence.storeState(State.WINNER, "draw");

    Player white = players.get(FigureColor.WHITE);
    Player black = players.get(FigureColor.BLACK);
    double whiteElo = white.elo();
    double blackElo = black.elo();
    white.adjustScoreOnGameEnd(0.5, blackElo);
    black.adjustScoreOnGameEnd(0.5, whiteElo);
  }

  @Override
  public Map<FigureColor, Player> players() {
    return players;
  }

  @Override
  public boolean isOver() {
    return board.endType(board.turn()) != EndType.NOT_END || endType != EndType.NOT_END;
  }

  @Override
  public boolean isDrawOffered() {
    return isDrawOffered;
  }

  @Override
  public void offerDraw() {
    isDrawOffered = true;
  }

  @Override
  public void denyDrawOffer() {
    isDrawOffered = false;
  }

  @Override
  public Game keepPlayersOf(Game game) {
    players.put(FigureColor.WHITE, game.players().get(FigureColor.WHITE));
    players.put(FigureColor.BLACK, game.players().get(FigureColor.BLACK));
    return this;
  }

  @Override
  public List<String> fenHistory() {
    return this.fenHistory;
  }

  @Override
  public boolean isThreeFoldRepetition() {
    String currentFen = FenNotation.generateFen(board);
    if (!fenHistory.getLast().equals(currentFen)) {
      fenHistory.add(FenNotation.generateFen(board));
      boolean isThreeFoldRepetition = isThreeFoldRepetition();
      fenHistory.removeLast();
      return isThreeFoldRepetition;
    }

    Map<String, Integer> positionCount = new HashMap<>();
    for (String fenString : fenHistory) {
      String key = FenNotation.extractFenKeyParts(fenString);
      positionCount.put(key, positionCount.getOrDefault(key, 0) + 1);
    }
    return positionCount.values().stream().anyMatch(x -> x >= 3);
  }
}
