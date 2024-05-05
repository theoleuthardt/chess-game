package hwr.oop.chess.persistence;

import hwr.oop.chess.cli.InvalidUserInputException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CsvGameRepository implements GameDataManager {
    private final Map<String, String> gameData = new HashMap<>();

    public void storeState(String key, String value) {
        gameData.put(key, value);
    }

    public String loadState(String key) {
        return gameData.get(key);
    }

    private String fileName(int gameId) {
        return "game_" + gameId + ".csv";
    }

    public void loadGame(int gameId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName(gameId)))) {
            gameData.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                int comma = line.indexOf(',');
                gameData.put(line.substring(0, comma), line.substring(comma + 1));
            }
        } catch (IOException e) {
            throw new InvalidUserInputException("The Game #" + gameId + " could not be started. Please verify that the file '" + fileName(gameId)+ "' exists. (Error: " + e.getMessage() + ")");
        }
    }

    public void saveGame(int gameId) {
        try (FileWriter writer = new FileWriter(fileName(gameId))) {
            for (Map.Entry<String, String> entry : gameData.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            throw new InvalidUserInputException("The Game #" + gameId + " could not be saved. Please verify that the current folder is not protected. (Error: " + e.getMessage() + ")");
        }
    }
}
