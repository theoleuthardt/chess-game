package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.persistence.FenNotation;
import hwr.oop.chess.persistence.Persistence;
import hwr.oop.chess.persistence.Player;

import java.util.EnumMap;
import java.util.Map;

public class ChessGame {
  private final Persistence persistence;
  private final Board board;
  private boolean isOver;
  private boolean isDrawOffered;
  private final Map<FigureColor, Player> players = new EnumMap<>(FigureColor.class);

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
    isOver = false;
    isDrawOffered = false;
    board.addFiguresToBoard();
    players.put(FigureColor.WHITE, new Player("0"));
    players.put(FigureColor.BLACK, new Player("0"));
  }

  private void loadGame() {
    persistence.loadGame();
    isOver = "1".equals(persistence.loadState("isOver"));
    isDrawOffered = "1".equals(persistence.loadState("isDrawOffered"));
    FenNotation.parseFEN(board, persistence.loadState("fen"));
    players.put(FigureColor.WHITE, new Player(persistence.loadState("whiteScore")));
    players.put(FigureColor.BLACK, new Player(persistence.loadState("blackScore")));
  }

  public void saveGame() {
    persistence.storeState("isOver", isOver ? "1" : "0");
    persistence.storeState("isDrawOffered", isDrawOffered ? "1" : "0");
    persistence.storeState("fen", FenNotation.generateFen(board));
    persistence.storeState(
        "whiteScore", String.valueOf(players.get(FigureColor.WHITE).doubleOfScore()));
    persistence.storeState(
        "blackScore", String.valueOf(players.get(FigureColor.BLACK).doubleOfScore()));
    persistence.saveGame();
  }

  public Board board() {
    return board;
  }

  public void playerHasWon(FigureColor color) {
    isOver = true;
    players.get(color).fullPointOnWin();
    persistence.storeState("winner", color.name());
  }

  public void endsWithDraw() {
    isOver = true;
    isDrawOffered = false;
    persistence.storeState("winner", "draw");
    players.get(FigureColor.WHITE).halfPointOnDraw();
    players.get(FigureColor.BLACK).halfPointOnDraw();
  }

  public Map<FigureColor, Player> players() {
    return players;
  }

  public boolean isOver() {
    return isOver;
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
}
