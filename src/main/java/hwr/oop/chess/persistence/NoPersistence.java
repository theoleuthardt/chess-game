package hwr.oop.chess.persistence;

import hwr.oop.chess.application.EndType;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import static hwr.oop.chess.persistence.NoPersistence.GameIdType.*;

public class NoPersistence implements Persistence {
  public enum GameIdType {
    NO_GAME,
    PAWN_PROMOTION,
    PAWN_PROMOTION_POSSIBLE,
    DEFAULT_POSITIONS,
    WHITE_CHECK_POSSIBLE,
    WHITE_CHECKMATE_POSSIBLE,
    WHITE_STALEMATE_POSSIBLE,
    GAME_IS_OVER_RESIGNATION,
    GAME_IS_OVER_WHITE_WINS,
    GAME_IS_OVER_DRAW,
    GAME_IS_OVER_DRAW_STALEMATE,
    GAME_IS_OVER_DRAW_DEAD_POSITION,
    GAME_IS_OVER_THREEFOLD_REPETITION,
    DRAW_OFFERED,
    GAME_IS_OVER_FIFTY_MOVE_RULE,
    FIFTY_MOVE_POSSIBLE,
    THREEFOLD_REPETITION_POSSIBLE,
    DEAD_POSITION_POSSIBLE,
    PGN_HISTORY,
  }

  private final Map<State, String> gameData = new EnumMap<>(State.class);
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
  public void storeState(State key, String value) {
    gameData.put(key, value);
  }

  @Override
  public String loadState(State key) {
    if (gameData.containsKey(key)) {
      return gameData.get(key);
    }

    GameIdType type =
        Arrays.stream(GameIdType.values())
            .filter(x -> x.ordinal() == gameId)
            .findFirst()
            .orElse(NO_GAME);

    return switch (key) {
      case State.FEN_HISTORY ->
          switch (type) {
            case PAWN_PROMOTION -> "2PP4/8/8/8/7k/8/PP6/7K w - - 0 1";
            case PAWN_PROMOTION_POSSIBLE ->
                "1nbqkbnr/Pppppppp/8/8/8/8/1PPPPPPP/RNBQKBNR w KQk - 0 1";
            case DEFAULT_POSITIONS,
                    GAME_IS_OVER_WHITE_WINS,
                    GAME_IS_OVER_DRAW,
                    GAME_IS_OVER_RESIGNATION,
                    GAME_IS_OVER_FIFTY_MOVE_RULE,
                    GAME_IS_OVER_THREEFOLD_REPETITION,
                    PGN_HISTORY,
                    DRAW_OFFERED ->
                "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
            case GAME_IS_OVER_DRAW_STALEMATE -> "8/8/8/8/8/1K6/B7/k7 w - - 0 1";
            case GAME_IS_OVER_DRAW_DEAD_POSITION -> "8/8/3K4/8/8/8/8/4k3 w - - 0 1";
            case WHITE_CHECK_POSSIBLE -> "8/8/k7/8/8/8/PP3q2/1K6 b - - 0 1";
            case WHITE_CHECKMATE_POSSIBLE -> "K7/8/8/1r6/1r6/8/8/7k b - - 0 1";
            case WHITE_STALEMATE_POSSIBLE -> "K7/7q/8/8/8/8/8/k7 b - - 0 1";
            case DEAD_POSITION_POSSIBLE -> "K7/1q6/8/8/8/8/8/7k w - - 0 1";
            case FIFTY_MOVE_POSSIBLE -> "q7/8/8/8/8/8/8/3K2k1 b - - 99 1";
            case THREEFOLD_REPETITION_POSSIBLE ->
                "q7/8/8/8/8/8/8/3K2k1 w - - 50 1,1q6/8/8/8/8/8/8/3K2k1 b - - 50 1,q7/8/8/8/8/8/8/3K2k1 w - - 50 1,1q6/8/8/8/8/8/8/3K2k1 b - - 50 1";
            default -> null;
          };

      case State.END_TYPE ->
          switch (type) {
            case GAME_IS_OVER_WHITE_WINS -> EndType.CHECKMATE.name();
            case GAME_IS_OVER_DRAW -> EndType.MUTUAL_DRAW.name();
            case GAME_IS_OVER_DRAW_STALEMATE -> EndType.STALEMATE.name();
            case GAME_IS_OVER_DRAW_DEAD_POSITION -> EndType.DEAD_POSITION.name();
            case GAME_IS_OVER_RESIGNATION -> EndType.RESIGNATION.name();
            case GAME_IS_OVER_FIFTY_MOVE_RULE -> EndType.FIFTY_MOVE_RULE.name();
            case GAME_IS_OVER_THREEFOLD_REPETITION -> EndType.THREE_FOLD_REPETITION.name();
            default -> EndType.NOT_END.name();
          };

      case State.WINNER ->
          switch (type) {
            case GAME_IS_OVER_WHITE_WINS -> "WHITE";
            case GAME_IS_OVER_RESIGNATION -> "BLACK";
            default -> null;
          };

      case State.WHITE_ELO, State.BLACK_ELO -> "1200";
      case State.WHITE_GAME_COUNT, State.BLACK_GAME_COUNT -> "0";
      case State.WHITE_SCORE, State.BLACK_SCORE -> type == GAME_IS_OVER_DRAW ? "1" : "0";
      case State.IS_DRAW_OFFERED -> type == DRAW_OFFERED ? "1" : "0";
      case State.PGN_HISTORY -> type == PGN_HISTORY ? "a4,Na6,Ra3" : "";
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
