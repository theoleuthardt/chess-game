package hwr.oop.chess.persistence;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PortableGameNotationTest {

  private List<String> addCounter(List<String> pgnHistory) {
    List<String> moves = new ArrayList<>();
    int moveCount = 0;
    int i = 0;
    for (String move : pgnHistory) {
      if (i++ % 2 == 0) {
        moves.add(++moveCount + ". " + move);
      } else {
        moves.add(move);
      }
    }
    return moves;
  }

  @Test
  void testAddCounterWithEmptyList() {
    List<String> pgnHistory = new ArrayList<>();
    List<String> result = addCounter(pgnHistory);
    assertTrue(result.isEmpty());
  }

  @Test
  void testAddCounterWithOneMove() {
    List<String> pgnHistory = List.of("e4");
    List<String> result = addCounter(pgnHistory);
    assertEquals(List.of("1. e4"), result);
  }

  @Test
  void testAddCounterWithTwoMoves() {
    List<String> pgnHistory = Arrays.asList("e4", "e5");
    List<String> result = addCounter(pgnHistory);
    assertEquals(Arrays.asList("1. e4", "e5"), result);
  }

  @Test
  void testAddCounterWithThreeMoves() {
    List<String> pgnHistory = Arrays.asList("e4", "e5", "Nf3");
    List<String> result = addCounter(pgnHistory);
    assertEquals(Arrays.asList("1. e4", "e5", "2. Nf3"), result);
  }

  @Test
  void testAddCounterWithFourMoves() {
    List<String> pgnHistory = Arrays.asList("e4", "e5", "Nf3", "Nc6");
    List<String> result = addCounter(pgnHistory);
    assertEquals(Arrays.asList("1. e4", "e5", "2. Nf3", "Nc6"), result);
  }

  @Test
  void testAddCounterWithFiveMoves() {
    List<String> pgnHistory = Arrays.asList("e4", "e5", "Nf3", "Nc6", "Bb5");
    List<String> result = addCounter(pgnHistory);
    assertEquals(Arrays.asList("1. e4", "e5", "2. Nf3", "Nc6", "3. Bb5"), result);
  }

  @Test
  void testAddCounterWithSixMoves() {
    List<String> pgnHistory = Arrays.asList("e4", "e5", "Nf3", "Nc6", "Bb5", "a6");
    List<String> result = addCounter(pgnHistory);
    assertEquals(Arrays.asList("1. e4", "e5", "2. Nf3", "Nc6", "3. Bb5", "a6"), result);
  }

  @Test
  void testAddCounterWithSevenMoves() {
    List<String> pgnHistory = Arrays.asList("e4", "e5", "Nf3", "Nc6", "Bb5", "a6", "Ba4");
    List<String> result = addCounter(pgnHistory);
    assertEquals(Arrays.asList("1. e4", "e5", "2. Nf3", "Nc6", "3. Bb5", "a6", "4. Ba4"), result);
  }

  @Test
  void testAddCounterWithEightMoves() {
    List<String> pgnHistory = Arrays.asList("e4", "e5", "Nf3", "Nc6", "Bb5", "a6", "Ba4", "Nf6");
    List<String> result = addCounter(pgnHistory);
    assertEquals(
        Arrays.asList("1. e4", "e5", "2. Nf3", "Nc6", "3. Bb5", "a6", "4. Ba4", "Nf6"), result);
  }

  @Test
  void testAddCounterWithNineMoves() {
    List<String> pgnHistory =
        Arrays.asList("e4", "e5", "Nf3", "Nc6", "Bb5", "a6", "Ba4", "Nf6", "O-O");
    List<String> result = addCounter(pgnHistory);
    assertEquals(
        Arrays.asList("1. e4", "e5", "2. Nf3", "Nc6", "3. Bb5", "a6", "4. Ba4", "Nf6", "5. O-O"),
        result);
  }
}
