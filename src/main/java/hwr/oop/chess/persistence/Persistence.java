package hwr.oop.chess.persistence;

public interface Persistence {
  void loadGame();

  void saveGame();

  void storeState(String key, String value);

  String loadState(String key);

  void setGameId(int gameId);

  int gameId();
}
