package hwr.oop.chess.persistence;

public class NoPersistence implements GameDataManager {

    public void storeState(String key, String value) {
        // Some tests need a NoPersistence adapter which does nothing
    }

    public String loadState(String key) {
        return null;
    }

    public void loadGame(int gameId) {
        // Some tests need a NoPersistence adapter which does nothing
    }

    public void saveGame(int gameId) {
        // Some tests need a NoPersistence adapter which does nothing
    }
}
