package hwr.oop.chess.application.figures;

import hwr.oop.chess.cli.InvalidUserInputException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class FigureTypeTest {
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

  @Test
  void testInvalidValues() {
    String invalidFigure = "BOAT";
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> FigureType.fromString(invalidFigure));

    String expectedMessage = "The figure type '" + invalidFigure + "' is not valid.";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }
}
