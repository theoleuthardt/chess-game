package hwr.oop.chess.persistence;

import java.lang.reflect.Method;

public class DynamicGameDataManager {

  public void loadGame(int gameId) throws Exception {

    Class<?> gameDataManagerClass = Class.forName("GameDataManager");
    Method loadGameMethod = gameDataManagerClass.getDeclaredMethod("loadGame", int.class);

    Object gameDataManagerInstance = new CsvGameRepository(); // Using CsvGameRepository
    loadGameMethod.invoke(gameDataManagerInstance, gameId);
  }

  public void saveGame(int gameId) throws Exception {

    Class<?> gameDataManagerClass = Class.forName("GameDataManager");
    Method saveGameMethod = gameDataManagerClass.getDeclaredMethod("saveGame", int.class);

    Object gameDataManagerInstance = new CsvGameRepository(); // Using CsvGameRepository
    saveGameMethod.invoke(gameDataManagerInstance, gameId);
  }

  public static void main(String[] args) throws Exception {
    DynamicGameDataManager manager = new DynamicGameDataManager();
    int gameId =
        123; // this int should be passed by Main or CliAdapter as the user already put it into the
    // cli command
    manager.loadGame(gameId);
    manager.saveGame(gameId);
  }

  // Loose coupling for saving and loading game
  // public interface
}
