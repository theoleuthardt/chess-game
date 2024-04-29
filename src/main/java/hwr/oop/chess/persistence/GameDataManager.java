package hwr.oop.chess.persistence;


public interface GameDataManager {
  void loadGame(int gameId);

  void saveGame(int gameId);

  void storeState(String key, String value);

  String loadState(String key);
}


