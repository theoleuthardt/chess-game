package hwr.oop.chess.persistence;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Scanner;

interface GameDataManager {
  void loadGame(int gameId);

  void saveGame(int gameId);
}

class CsvGameRepository implements GameDataManager {

  @Override
  public void loadGame(int gameId) {
    // Logic to load game data from a CSV file
    System.out.println("Loading game data from CSV file for game ID: " + gameId);
  }

  @Override
  public void saveGame(int gameId) {
    // Logic to save game data to a CSV file
    try {
      FileWriter writer = new FileWriter("game_" + gameId + ".csv");
      // Write game data to the CSV file
      // Example:
      writer.write("GameID,Player1,Player2,Move1,Move2,...\n");
      writer.close();
      System.out.println("Game data saved to CSV file for game ID: " + gameId);
    } catch (IOException e) {
      System.err.println("Error saving game data to CSV file: " + e.getMessage());
    }
  }
}

public class DynamicGameDataManager {

  private final Scanner scanner;

  public DynamicGameDataManager() {
    this.scanner = new Scanner(System.in);
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

  public static void main(String[] args) throws Exception {
    DynamicGameDataManager manager = new DynamicGameDataManager();
    manager.loadGame();
    manager.saveGame();
  }
}
