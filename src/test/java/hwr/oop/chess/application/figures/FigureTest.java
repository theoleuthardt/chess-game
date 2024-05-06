package hwr.oop.chess.application.figures;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FigureTest {

  // TODO Fix Test
  @ParameterizedTest
  @EnumSource(
      value = FigureType.class,
      names = {"BISHOP", "KNIGHT", "QUEEN", "ROOK"})
  void getFigure(FigureType type) {
    Pawn pawn = new Pawn(FigureColor.BLACK);
    Figure testFigure = pawn.getFigureFromTypeAndColor(type, FigureColor.BLACK);
    Assertions.assertThat(testFigure.type()).isEqualTo(type);
  }

  @Test
  void getFigureException() {
    Pawn pawn = new Pawn(FigureColor.BLACK);
    RuntimeException exception =
        assertThrows(
            RuntimeException.class,
            () -> pawn.getFigureFromTypeAndColor(FigureType.KING, FigureColor.BLACK));

    String expectedMessage = "This is not a valid FigureType";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }
}
