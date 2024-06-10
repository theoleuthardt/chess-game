package hwr.oop.chess.persistence;

public interface Persistence {
  void loadGame();

  void saveGame();

  void storeState(State key, String value);

  String loadState(State key);

  void setGameId(int gameId);

  int gameId();
}
