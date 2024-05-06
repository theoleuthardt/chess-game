package hwr.oop.chess.persistence;

import java.lang.reflect.Method;
import java.util.Scanner;

public class DynamicGameDataManager {

  private final Scanner scanner;

  public DynamicGameDataManager() {
    this.scanner = new Scanner(System.in);
  }

  public static void main(String[] args) throws Exception {
    DynamicGameDataManager manager = new DynamicGameDataManager();
    manager.loadGame();
    manager.saveGame();
  }

  public void loadGame() throws Exception {
    System.out.print("Enter the game ID to load: ");
    int gameId = scanner.nextInt();

    Class<?> gameDataManagerClass = Class.forName("GameDataManager");
    Method loadGameMethod = gameDataManagerClass.getDeclaredMethod("loadGame", int.class);

    Object gameDataManagerInstance = new CsvGameRepository(); // Using CsvGameRepository
    loadGameMethod.invoke(gameDataManagerInstance, gameId);
  }

  public void saveGame() throws Exception {
    System.out.print("Enter the game ID to save: ");
    int gameId = scanner.nextInt();

    Class<?> gameDataManagerClass = Class.forName("GameDataManager");
    Method saveGameMethod = gameDataManagerClass.getDeclaredMethod("saveGame", int.class);

    Object gameDataManagerInstance = new CsvGameRepository(); // Using CsvGameRepository
    saveGameMethod.invoke(gameDataManagerInstance, gameId);
  }
}

//  TODO Elo calculation test
