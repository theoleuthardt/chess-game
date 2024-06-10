package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.application.figures.Pawn;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class CellTest {

  @Test
  void positionIsValid() {
    Cell position = new Cell(Coordinate.SIX, Coordinate.FIVE);
    assertSoftly(
        softly -> {
          softly.assertThat(position.x()).isEqualTo(Coordinate.SIX);
          softly.assertThat(position.y()).isEqualTo(Coordinate.FIVE);
        });
  }

  @Test
  void positionConversion_AIsEqualToOne() {
    Cell position = new Cell(Coordinate.ONE, Coordinate.FIVE);
    assertThat(position.x()).isEqualTo(Coordinate.ONE);
    assertThat(position.y()).isEqualTo(Coordinate.FIVE);
  }

  @Test
  void positionConversion_EIsEqualToFive() {
    Cell position = new Cell(Coordinate.FIVE, Coordinate.TWO);
    assertThat(position.x()).isEqualTo(Coordinate.FIVE);
    assertThat(position.y()).isEqualTo(Coordinate.TWO);
  }

  @Test
  void cellsAreEqual() {
    Cell one = new Cell(Coordinate.FIVE, Coordinate.TWO);
    Cell two = new Cell(Coordinate.FIVE, Coordinate.TWO);
    assertThat(one.isEqualTo(two)).isTrue();
  }

  @Test
  void cellsAreNotEqual() {
    assertThat(
            new Cell(Coordinate.FIVE, Coordinate.TWO)
                .isEqualTo(new Cell(Coordinate.FIVE, Coordinate.THREE)))
        .isFalse();
    assertThat(
            new Cell(Coordinate.SIX, Coordinate.TWO)
                .isEqualTo(new Cell(Coordinate.FIVE, Coordinate.THREE)))
        .isFalse();
    assertThat(
            new Cell(Coordinate.ONE, Coordinate.THREE)
                .isEqualTo(new Cell(Coordinate.ONE, Coordinate.ONE)))
        .isFalse();
  }

  @Test
  void cellConnectImpossible() {
    Cell cell = new Cell(Coordinate.THREE, Coordinate.THREE);
    List<Cell> list =
        List.of(
            cell,
            new Cell(Coordinate.THREE, Coordinate.ONE),
            new Cell(Coordinate.THREE, Coordinate.THREE),
            new Cell(Coordinate.THREE, Coordinate.FIVE),
            new Cell(Coordinate.ONE, Coordinate.THREE),
            new Cell(Coordinate.FIVE, Coordinate.THREE),
            new Cell(Coordinate.ONE, Coordinate.ONE),
            new Cell(Coordinate.EIGHT, Coordinate.EIGHT),
            new Cell(Coordinate.FOUR, Coordinate.ONE),
            new Cell(Coordinate.TWO, Coordinate.ONE));

    for (Cell c : list) {
      assertThatThrownBy(() -> cell.connectTo(c)).isInstanceOf(IllegalArgumentException.class);
    }
    assertThatNoException().isThrownBy(() -> cell.connectTo(null));
  }

  @Test
  void cellToStringIsEmpty() {
    Cell cell = new Cell(Coordinate.THREE, Coordinate.THREE);
    assertThat(cell).hasToString("Cell_C3(-)");
  }

  @Test
  void cellToStringWithFigure() {
    Cell cell = new Cell(Coordinate.THREE, Coordinate.THREE);
    cell.setFigure(new Pawn(FigureColor.WHITE));
    assertThat(cell).hasToString("Cell_C3(P)");
  }

  @Test
  void findCellInDirection() {
    Cell cell = new Cell(Coordinate.THREE, Coordinate.THREE);
    assertThatThrownBy(() -> cell.findCellInDirection(10, CellDirection.RIGHT))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void isFreeInDirection() {
    Cell cell = new Cell(Coordinate.THREE, Coordinate.THREE);
    assertThat(cell.isFreeInDirection(10, CellDirection.RIGHT)).isFalse();
  }

  @Test
  void isBlackCell() {
    Cell cell = new Cell(Coordinate.ONE, Coordinate.ONE);
    assertThat(cell.isWhiteCell()).isFalse();
  }

  @Test
  void isWhiteCell() {
    Cell cell1 = new Cell(Coordinate.TWO, Coordinate.ONE);
    Cell cell2 = new Cell(Coordinate.ONE, Coordinate.TWO);
    assertThat(cell1.isWhiteCell()).isTrue();
    assertThat(cell2.isWhiteCell()).isTrue();
  }
}
