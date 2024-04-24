package hwr.oop.chess.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class PositionTest {
  @Test
  void positionIsInvalid_XZeroIsTooSmall() {
    assertThatThrownBy(() -> new Cell(0, 3))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid Position");
  }

  @Test
  void positionIsInvalid_YZeroIsTooSmall() {
    assertThatThrownBy(() -> new Cell(3, 0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid Position");
  }

  @Test
  void positionIsInvalid_XNineIsTooBig() {
    assertThatThrownBy(() -> new Cell(9, 3))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid Position");
  }

  @Test
  void positionIsInvalid_YNineIsTooBig() {
    assertThatThrownBy(() -> new Cell(3, 9))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid Position");
  }

  @Test
  void positionIsValid() {
    Cell position = new Cell(6, 5);
    assertSoftly(
        softly -> {
          softly.assertThat(position.x()).isEqualTo(6);
          softly.assertThat(position.y()).isEqualTo(5);
        });
  }

  @Test
  void positionConversion_AIsEqualToOne() {
    Cell position = new Cell('a', 5);
    Assertions.assertThat(position.x()).isEqualTo(1);
    Assertions.assertThat(position.y()).isEqualTo(5);
  }

  @Test
  void positionConversion_EIsEqualToFive() {
    Cell position = new Cell('e', 2);
    Assertions.assertThat(position.x()).isEqualTo(5);
    Assertions.assertThat(position.y()).isEqualTo(2);
  }
}
