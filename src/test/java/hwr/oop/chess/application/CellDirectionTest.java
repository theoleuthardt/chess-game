package hwr.oop.chess.application;

import hwr.oop.chess.cli.InvalidUserInputException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CellDirectionTest {
  @Test
  void testEnumValues() { // Test if the enum contains the valid values
    assertEquals(8, CellDirection.values().length);
    assertEquals(CellDirection.LEFT, CellDirection.valueOf("LEFT"));
    assertEquals(CellDirection.RIGHT, CellDirection.valueOf("RIGHT"));
    assertEquals(CellDirection.TOP, CellDirection.valueOf("TOP"));
    assertEquals(CellDirection.TOP_LEFT, CellDirection.valueOf("TOP_LEFT"));
    assertEquals(CellDirection.TOP_RIGHT, CellDirection.valueOf("TOP_RIGHT"));
    assertEquals(CellDirection.BOTTOM, CellDirection.valueOf("BOTTOM"));
    assertEquals(CellDirection.BOTTOM_LEFT, CellDirection.valueOf("BOTTOM_LEFT"));
    assertEquals(CellDirection.BOTTOM_RIGHT, CellDirection.valueOf("BOTTOM_RIGHT"));
  }

  @Test
  void testInvalidValues() {
    String invalidDirection = "BOAT";
    InvalidUserInputException exception =
        assertThrows(
            InvalidUserInputException.class,
            () -> CellDirection.fromStringToEnum(invalidDirection));

    String expectedMessage = "The cell direction '" + invalidDirection + "' is not valid.";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }
}
