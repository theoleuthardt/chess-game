package hwr.oop.chess.persistence;

import java.util.Arrays;

public class NoPersistence implements Persistence {
  public enum GameIdType {
    NO_GAME,
    PAWN_PROMOTION,
    DEFAULT_POSITIONS,
    WHITE_CHECK_POSSIBLE,
    BLACK_CHECK_POSSIBLE,
    WHITE_CHECKMATE_POSSIBLE,
    BLACK_CHECKMATE_POSSIBLE,
  }

  private int gameId;

  @Override
  public void setGameId(int gameId) {
    this.gameId = gameId;
  }

  @Override
  public int gameId() {
    return gameId;
  }

  @Override
  public void storeState(String key, String value) {
    // Some tests need a NoPersistence adapter which does nothing
  }

  @Override
  public String loadState(String key) {
    GameIdType type = Arrays.stream(GameIdType.values()).filter(x -> x.ordinal() == gameId).findFirst().orElse(null);
    if(key.equals("fen")) {
      return switch (type) {
        case GameIdType.PAWN_PROMOTION -> "2PP4/8/8/8/7k/8/PP6/7K w - - 0 1";
        case GameIdType.DEFAULT_POSITIONS -> "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        case GameIdType.WHITE_CHECK_POSSIBLE -> "8/8/k7/8/8/8/PP3q2/1K6 b - - 0 1";
        case GameIdType.BLACK_CHECK_POSSIBLE -> "8/8/K7/8/8/8/pp3Q2/1k6 w - - 0 1";
        case GameIdType.WHITE_CHECKMATE_POSSIBLE -> "K7/8/8/1r6/1r6/8/8/7k b - - 0 1";
        case GameIdType.BLACK_CHECKMATE_POSSIBLE -> "k7/8/8/1R6/1R6/8/8/7K w - - 0 1";
        case null, default -> null;
      };
    }
    return null;
  }

  @Override
  public void loadGame() {
    // Some tests need a NoPersistence adapter which does nothing
  }

  @Override
  public void saveGame() {
    // Some tests need a NoPersistence adapter which does nothing
  }
}
