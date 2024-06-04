package hwr.oop.chess.application.figures;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;

class FigureTest {

  @ParameterizedTest
  @EnumSource(
      value = FigureType.class,
      names = {"BISHOP", "KNIGHT", "QUEEN", "ROOK"})
  void getFigure(FigureType type) {
    Figure testFigure = Figure.fromTypeAndColor(type, FigureColor.BLACK);
    assertThat(testFigure.type()).isEqualTo(type);
  }

  @Test
  void getFigureException() {
    assertThatException()
        .isThrownBy(() -> Figure.fromTypeAndColor(FigureType.KING, FigureColor.BLACK))
        .withMessageContaining("This is not a valid FigureType");
  }
}
