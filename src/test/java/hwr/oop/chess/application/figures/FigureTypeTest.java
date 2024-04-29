package hwr.oop.chess.application.figures;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FigureTypeTest {
  @Test
  void testEnumValues() { // Test if the enum contains the valid values
    assertEquals(6, FigureType.values().length); // Check if there are exactly 6 values
    assertEquals(FigureType.KING, FigureType.valueOf("KING"));
    assertEquals(FigureType.QUEEN, FigureType.valueOf("QUEEN"));
    assertEquals(FigureType.ROOK, FigureType.valueOf("ROOK"));
    assertEquals(FigureType.BISHOP, FigureType.valueOf("BISHOP"));
    assertEquals(FigureType.KNIGHT, FigureType.valueOf("KNIGHT"));
    assertEquals(FigureType.PAWN, FigureType.valueOf("PAWN"));
  }
}
