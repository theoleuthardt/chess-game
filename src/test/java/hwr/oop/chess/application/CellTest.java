package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.application.figures.Pawn;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class CellTest {
  @Test
  void positionIsInvalid_XZeroIsTooSmall() {
    assertThatThrownBy(() -> new Cell(0, 3))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Invalid Position");
  }

  @Test
  void positionIsInvalid_YZeroIsTooSmall() {
    assertThatThrownBy(() -> new Cell(3, 0))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Invalid Position");
  }

  @Test
  void positionIsInvalid_XNineIsTooBig() {
    assertThatThrownBy(() -> new Cell(9, 3))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Invalid Position");
  }

  @Test
  void positionIsInvalid_YNineIsTooBig() {
    assertThatThrownBy(() -> new Cell(3, 9))
        .isInstanceOf(RuntimeException.class)
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
    assertThat(position.x()).isEqualTo(1);
    assertThat(position.y()).isEqualTo(5);
  }

  @Test
  void positionConversion_EIsEqualToFive() {
    Cell position = new Cell('e', 2);
    assertThat(position.x()).isEqualTo(5);
    assertThat(position.y()).isEqualTo(2);
  }

  @Test
  void isInvalidCoordinate() {
    Cell cell = new Cell('a', 1);
    assertThat(cell.isInvalidCoordinate(19)).isTrue();
    assertThat(cell.isInvalidCoordinate(1)).isFalse();
  }

  @Test
  void cellsAreEqual() {
    Cell one = new Cell('e', 2);
    Cell two = new Cell('e', 2);
    assertThat(one.isEqualTo(two)).isTrue();
  }

  @Test
  void cellsAreNotEqual() {
    assertThat(new Cell('e', 2).isEqualTo(new Cell('e', 3))).isFalse();
    assertThat(new Cell('f', 2).isEqualTo(new Cell('e', 3))).isFalse();
    assertThat(new Cell('e', 2).isEqualTo(new Cell('a', 1))).isFalse();
  }

  @Test
  void cellConnectImpossible() {
    Cell cell = new Cell('c', 3);
    List<Cell> list =
        List.of(
            cell,
            new Cell('c', 1),
            new Cell('c', 3),
            new Cell('c', 5),
            new Cell('a', 3),
            new Cell('e', 3),
            new Cell('a', 1),
            new Cell('h', 8),
            new Cell('d', 1),
            new Cell('b', 1));

    for (Cell c : list) {
      assertThatThrownBy(() -> cell.connectTo(c)).isInstanceOf(IllegalArgumentException.class);
    }
    assertThatNoException().isThrownBy(() -> cell.connectTo(null));
  }

  @Test
  void cellToString() {
    Cell cell = new Cell('c', 3);
    assertThat(cell).hasToString("Cell_C3(-)");
    cell.setFigure(new Pawn(FigureColor.WHITE));
    assertThat(cell).hasToString("Cell_C3(P)");
  }

  @Test
  void findCellInDirection() {
    Cell cell = new Cell('c', 3);
    assertThatThrownBy(() -> cell.findCellInDirection(10, CellDirection.RIGHT))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void isFreeInDirection() {
    Cell cell = new Cell('c', 3);
    assertThat(cell.isFreeInDirection(10, CellDirection.RIGHT)).isFalse();
  }
}
