package hwr.oop.chess.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
