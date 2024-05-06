package hwr.oop.chess.application.figures;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class FigureTest {

  // TOOO Fix Test
  @ParameterizedTest
  @EnumSource(
      value = FigureType.class,
      types = {"BISHOP", "KNIGHT", "QUEEN", "ROOK"})
  void getFigure(FigureType type) {
    Pawn pawn = new Pawn(FigureColor.BLACK);
    Figure testFigure = pawn.getFigureFromTypeAndColor(type, FigureColor.BLACK);
    Assertions.assertThat(testFigure.type()).isEqualTo(type);
  }

  @Test
  void getFigureException() {
    Pawn pawn = new Pawn(FigureColor.BLACK);
    Exception exception =
        org.junit.jupiter.api.Assertions.assertThrows(
            RuntimeException.class,
            () -> {
              pawn.getFigureFromTypeAndColor(FigureType.KING, FigureColor.BLACK);
            });

    String expectedMessage = "This is not a valid FigureType";
    String actualMessage = exception.getMessage();

    org.junit.jupiter.api.Assertions.assertTrue(actualMessage.contains(expectedMessage));
  }
}
