package hwr.oop.chess.application;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CellDirectionTest {
  @Test
  void testEnumValues() { // Test if the enum contains the valid values
    assertThat(CellDirection.values()).hasSize(8);
    assertThat(CellDirection.valueOf("LEFT")).isEqualTo(CellDirection.LEFT);
    assertThat(CellDirection.valueOf("RIGHT")).isEqualTo(CellDirection.RIGHT);
    assertThat(CellDirection.valueOf("TOP")).isEqualTo(CellDirection.TOP);
    assertThat(CellDirection.valueOf("TOP_LEFT")).isEqualTo(CellDirection.TOP_LEFT);
    assertThat(CellDirection.valueOf("TOP_RIGHT")).isEqualTo(CellDirection.TOP_RIGHT);
    assertThat(CellDirection.valueOf("BOTTOM")).isEqualTo(CellDirection.BOTTOM);
    assertThat(CellDirection.valueOf("BOTTOM_LEFT")).isEqualTo(CellDirection.BOTTOM_LEFT);
    assertThat(CellDirection.valueOf("BOTTOM_RIGHT")).isEqualTo(CellDirection.BOTTOM_RIGHT);
  }
}
