package hwr.oop.chess.application.figures;

import hwr.oop.chess.cli.InvalidUserInputException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class FigureTypeTest {
  @Test
  void testEnumValues() { // Test if the enum contains the valid values
    assertThat(FigureType.values()).hasSize(6); // Check if there are exactly 6 values
    assertThat(FigureType.valueOf("KING")).isEqualTo(FigureType.KING);
    assertThat(FigureType.valueOf("QUEEN")).isEqualTo(FigureType.QUEEN);
    assertThat(FigureType.valueOf("ROOK")).isEqualTo(FigureType.ROOK);
    assertThat(FigureType.valueOf("BISHOP")).isEqualTo(FigureType.BISHOP);
    assertThat(FigureType.valueOf("KNIGHT")).isEqualTo(FigureType.KNIGHT);
    assertThat(FigureType.valueOf("PAWN")).isEqualTo(FigureType.PAWN);
  }

  @Test
  void testInvalidValues() {
    assertThatThrownBy(() -> FigureType.fromString("BOAT"))
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("The figure type 'BOAT' is not valid.");
  }
}
