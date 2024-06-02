package hwr.oop.chess.persistence;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static hwr.oop.chess.persistence.NoPersistence.GameIdType.*;

public class NoPersistence implements Persistence {
  public enum GameIdType {
    NO_GAME,
    PAWN_PROMOTION,
    DEFAULT_POSITIONS,
    WHITE_CHECK_POSSIBLE,
    WHITE_CHECKMATE_POSSIBLE,
    WHITE_STALEMATE_POSSIBLE,
    GAME_IS_OVER_WHITE_WINS,
    GAME_IS_OVER_DRAW,
    DRAW_OFFERED,
  }

  private final Map<String, String> gameData = new HashMap<>();
  private boolean wasSaved = false;

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
    gameData.put(key, value);
  }

  @Override
  public String loadState(String key) {
    if (gameData.containsKey(key)) {
      return gameData.get(key);
    }

    GameIdType type =
        Arrays.stream(GameIdType.values())
            .filter(x -> x.ordinal() == gameId)
            .findFirst()
            .orElse(NO_GAME);

    return switch (key) {
      case "fen" ->
          switch (type) {
            case PAWN_PROMOTION -> "2PP4/8/8/8/7k/8/PP6/7K w - - 0 1";
            case DEFAULT_POSITIONS, GAME_IS_OVER_WHITE_WINS, GAME_IS_OVER_DRAW, DRAW_OFFERED ->
                "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
            case WHITE_CHECK_POSSIBLE -> "8/8/k7/8/8/8/PP3q2/1K6 b - - 0 1";
            case WHITE_CHECKMATE_POSSIBLE -> "K7/8/8/1r6/1r6/8/8/7k b - - 0 1";
            case WHITE_STALEMATE_POSSIBLE -> "K7/7q/8/8/8/8/8/k7 b - - 0 1";
            default -> null;
          };

      case "isOver" ->
          switch (type) {
            case GAME_IS_OVER_WHITE_WINS, GAME_IS_OVER_DRAW -> "1";
            default -> "0";
          };

      case "winner" ->
          switch (type) {
            case GAME_IS_OVER_WHITE_WINS -> "WHITE";
            case GAME_IS_OVER_DRAW -> "draw";
            default -> null;
          };

      case "whiteScore", "blackScore" -> type == GAME_IS_OVER_DRAW ? "1" : "0";
      case "isDrawOffered" -> type == DRAW_OFFERED ? "1" : "0";
      default -> null;
    };
  }

  @Override
  public void loadGame() {
    // Some tests need a NoPersistence adapter which does nothing
  }

  @Override
  public void saveGame() {
    wasSaved = true;
  }

  public boolean wasSaved() {
    return wasSaved;
  }
}
