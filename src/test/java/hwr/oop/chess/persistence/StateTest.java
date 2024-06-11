package hwr.oop.chess.persistence;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StateTest {
  @Test
  void testState() {
    State[] states = State.values();
    assertEquals(11, states.length);
    assertArrayEquals(
        new State[] {
          State.FEN_HISTORY,
          State.WINNER,
          State.WHITE_SCORE,
          State.BLACK_SCORE,
          State.WHITE_ELO,
          State.BLACK_ELO,
          State.WHITE_GAME_COUNT,
          State.BLACK_GAME_COUNT,
          State.END_TYPE,
          State.IS_DRAW_OFFERED,
          State.PGN_HISTORY
        },
        states);
  }

  @Test
  void testEnumValueOfState() {
    assertEquals(State.FEN_HISTORY, State.valueOf("FEN_HISTORY"));
    assertEquals(State.WINNER, State.valueOf("WINNER"));
    assertEquals(State.WHITE_SCORE, State.valueOf("WHITE_SCORE"));
    assertEquals(State.BLACK_SCORE, State.valueOf("BLACK_SCORE"));
    assertEquals(State.WHITE_ELO, State.valueOf("WHITE_ELO"));
    assertEquals(State.BLACK_ELO, State.valueOf("BLACK_ELO"));
    assertEquals(State.WHITE_GAME_COUNT, State.valueOf("WHITE_GAME_COUNT"));
    assertEquals(State.BLACK_GAME_COUNT, State.valueOf("BLACK_GAME_COUNT"));
    assertEquals(State.END_TYPE, State.valueOf("END_TYPE"));
    assertEquals(State.IS_DRAW_OFFERED, State.valueOf("IS_DRAW_OFFERED"));
    assertEquals(State.PGN_HISTORY, State.valueOf("PGN_HISTORY"));
  }
}
