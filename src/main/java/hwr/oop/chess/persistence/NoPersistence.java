package hwr.oop.chess.persistence;

public class NoPersistence implements Persistence {
  public enum GameIdType {
    NO_GAME,
    PAWN_PROMOTION,
    DEFAULT_POSITIONS
  }

  private int gameId;

  public void setGameId(int gameId) {
    this.gameId = gameId;
  }

  public int gameId() {
    return gameId;
  }

  public void storeState(String key, String value) {
    // Some tests need a NoPersistence adapter which does nothing
  }

  public String loadState(String key) {
    if (gameId == GameIdType.PAWN_PROMOTION.ordinal() && key.equals("fen")) {
      return "2PP4/8/8/8/7k/8/PP6/7K w - - 0 1";
    }
    if (gameId == GameIdType.DEFAULT_POSITIONS.ordinal() && key.equals("fen")) {
      return "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    }
    return null;
  }

  public void loadGame() {
    // Some tests need a NoPersistence adapter which does nothing
  }

  public void saveGame() {
    // Some tests need a NoPersistence adapter which does nothing
  }
}
