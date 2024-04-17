package hwr.oop.chess.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PositionTest {
  @Test
  void positionIsInvalid_XZeroIsTooSmall() {
    Assertions.assertThatThrownBy(() -> new Position(0, 3))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid Position");
  }

  @Test
  void positionIsInvalid_YZeroIsTooSmall() {
    Assertions.assertThatThrownBy(() -> new Position(3, 0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid Position");
  }

  @Test
  void positionIsInvalid_XNineIsTooBig() {
    Assertions.assertThatThrownBy(() -> new Position(9, 3))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid Position");
  }

  @Test
  void positionIsInvalid_YNineIsTooBig() {
    Assertions.assertThatThrownBy(() -> new Position(3, 9))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid Position");
  }

  @Test
  void positionIsValid() {
    Position position = new Position(6, 5);
    Assertions.assertThat(position.x()).isEqualTo(6);
    Assertions.assertThat(position.y()).isEqualTo(5);
  }

  @Test
  void positionConversion_AIsEqualToOne() {
    Position position = new Position('a', 5);
    Assertions.assertThat(position.x()).isEqualTo(1);
    Assertions.assertThat(position.y()).isEqualTo(5);
  }

  @Test
  void positionConversion_EIsEqualToFive() {
    Position position = new Position('e', 2);
    Assertions.assertThat(position.x()).isEqualTo(5);
    Assertions.assertThat(position.y()).isEqualTo(2);
  }

}
