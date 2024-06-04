package hwr.oop.chess.persistence;

import hwr.oop.chess.cli.InvalidUserInputException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CSVFilePersistence implements Persistence {
  final Map<String, String> gameData = new HashMap<>();
  int gameId;

  @Override
  public void storeState(String key, String value) {
    gameData.put(key, value);
  }

  @Override
  public String loadState(String key) {
    return gameData.get(key);
  }

  private String fileName(int gameId) {
    return "game_" + gameId + ".csv";
  }

  @Override
  public void setGameId(int gameId) {
    if (gameId <= 0) {
      throw new InvalidUserInputException("The game ID must be a positive integer (1 or larger).");
    }
    this.gameId = gameId;
  }

  @Override
  public int gameId() {
    return gameId;
  }

  @Override
  public void loadGame() {
    gameData.clear();
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName(gameId)))) {
      String line;
      while ((line = reader.readLine()) != null) {
        int comma = line.indexOf(',');
        gameData.put(line.substring(0, comma), line.substring(comma + 1));
      }
    } catch (IOException e) {
      throw new InvalidUserInputException(
          "The Game #"
              + gameId
              + " could not be started. Please verify that the file '"
              + fileName(gameId)
              + "' exists. (Error: "
              + e.getMessage()
              + ")");
    }
  }

  @Override
  public void saveGame() {
    try (FileWriter writer = new FileWriter(fileName(gameId))) {
      for (Map.Entry<String, String> entry : gameData.entrySet()) {
        writer.write(entry.getKey() + "," + entry.getValue() + "\n");
      }
    } catch (IOException e) {
      throw new InvalidUserInputException(
          "The Game #"
              + gameId
              + " could not be saved. Please verify that the current folder is not protected. (Error: "
              + e.getMessage()
              + ")");
    }
  }
}
