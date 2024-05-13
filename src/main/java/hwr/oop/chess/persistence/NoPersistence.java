package hwr.oop.chess.persistence;

public class NoPersistence implements Persistence {
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
    return null;
  }

  public void loadGame() {
    // Some tests need a NoPersistence adapter which does nothing
  }

  public void saveGame() {
    // Some tests need a NoPersistence adapter which does nothing
  }
}
