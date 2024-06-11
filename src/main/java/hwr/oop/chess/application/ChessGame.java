package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.application.figures.FigureType;
import hwr.oop.chess.cli.InvalidUserInputException;
import hwr.oop.chess.persistence.*;

import java.util.*;

public class ChessGame implements Game {
  private final Persistence persistence;
  private final Board board;
  private final Map<FigureColor, Player> players = new EnumMap<>(FigureColor.class);
  private final List<String> fenHistory = new ArrayList<>();
  private final List<String> pgnHistory = new ArrayList<>();
  private boolean isDrawOffered;
  private EndType endType = EndType.NOT_END;
  private final AlgebraicNotation algebraicNotation = new AlgebraicNotation();

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
    List<State> missingStates = missingStatesInPersistence();
    if (!missingStates.isEmpty()) {
      throw new InvalidUserInputException(
          "Your save-file is invalid because it is missing: "
              + missingStates
              + "! Create a new game with 'chess create <ID>'.");
    }
    endType = EndType.valueOf(persistence.loadState(State.END_TYPE));
    isDrawOffered = "1".equals(persistence.loadState(State.IS_DRAW_OFFERED));
    parseIntoList(fenHistory, persistence.loadState(State.FEN_HISTORY));
    parseIntoList(pgnHistory, persistence.loadState(State.PGN_HISTORY));

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

    FenNotation.parseFEN(board, fenHistory.getLast());
  }

  private List<State> missingStatesInPersistence() {
    List<State> missingStates =
        new ArrayList<>(
            List.of(
                State.END_TYPE,
                State.IS_DRAW_OFFERED,
                State.FEN_HISTORY,
                State.PGN_HISTORY,
                State.WHITE_SCORE,
                State.BLACK_SCORE,
                State.WHITE_ELO,
                State.BLACK_ELO,
                State.WHITE_GAME_COUNT,
                State.BLACK_GAME_COUNT));
    missingStates.removeIf(state -> persistence.loadState(state) != null);
    return missingStates;
  }

  private void parseIntoList(List<String> list, String listAsString) {
    if (listAsString.isEmpty()) {
      return;
    }
    Collections.addAll(list, listAsString.split(","));
  }

  private String fenHistoryOfBoard() {
    String currentFen = FenNotation.generateFen(board);
    if (fenHistory.isEmpty() || !currentFen.equals(fenHistory.getLast())) {
      fenHistory.add(currentFen);
    }
    return String.join(",", fenHistory);
  }

  @Override
  public String pgnHistoryOfMoves() {
    return String.join(",", pgnHistory);
  }

  @Override
  public void saveGame() {
    persistence.storeState(State.END_TYPE, endType.name());
    persistence.storeState(State.IS_DRAW_OFFERED, isDrawOffered ? "1" : "0");
    persistence.storeState(State.FEN_HISTORY, fenHistoryOfBoard());
    persistence.storeState(State.PGN_HISTORY, pgnHistoryOfMoves());

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
    pgnHistory.add(color == FigureColor.WHITE ? "1-0" : "0-1");

    Player winner = players.get(color);
    Player looser = players.get(color.ofOpponent());
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
    pgnHistory.add("1/2-1/2");

    Player white = players.get(FigureColor.WHITE);
    Player black = players.get(FigureColor.BLACK);
    double whiteElo = white.elo();
    double blackElo = black.elo();
    white.adjustScoreOnGameEnd(0.5, blackElo);
    black.adjustScoreOnGameEnd(0.5, whiteElo);
  }

  @Override
  public boolean isOver() {
    return isThreeFoldRepetition()
        || board.endType(board.turn()) != EndType.NOT_END
        || endType != EndType.NOT_END;
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
  public Game keepPlayersOf(Game oldGame) {
    players.put(FigureColor.WHITE, oldGame.player(FigureColor.WHITE));
    players.put(FigureColor.BLACK, oldGame.player(FigureColor.BLACK));
    return this;
  }

  @Override
  public Player player(FigureColor color) {
    return players.get(color);
  }

  @Override
  public List<String> fenHistory() {
    return this.fenHistory;
  }

  @Override
  public List<String> pgnHistory() {
    return this.pgnHistory;
  }

  @Override
  public boolean isThreeFoldRepetition() {
    String currentFen = FenNotation.generateFen(board);
    if (!fenHistory.isEmpty() && !fenHistory.getLast().equals(currentFen)) {
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

  @Override
  public void rememberAndPerformPawnPromotion(Cell startCell, FigureType toFigure) {
    algebraicNotation.recordPawnPromotion(board, startCell, toFigure);
    board.promotePawn(startCell, toFigure);
    pgnHistory.add(algebraicNotation.toString());
  }

  @Override
  public void rememberAndPerformMove(Cell from, Cell to) {
    algebraicNotation.recordAction(board, from, to);
    board.moveFigure(from, to);
    algebraicNotation.actionFinished();
    if (from.isFree()) {
      pgnHistory.add(algebraicNotation.toString());
    }
  }
}
