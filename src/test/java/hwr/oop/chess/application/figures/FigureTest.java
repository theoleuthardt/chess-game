package hwr.oop.chess.application.figures;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import hwr.oop.chess.application.Cell;

class FigureTest {
  @Test
  void createPawn() {
    Cell position = new Cell('b', 2);
    Pawn pawn = new Pawn(FigureColor.BLACK, position.x(), position.y());
    Assertions.assertThat(pawn.color()).isEqualTo(FigureColor.BLACK);
    Assertions.assertThat(pawn.type()).isEqualTo(FigureType.PAWN);
    Assertions.assertThat(pawn.cell()).isEqualTo(position);
  }

  @Test
  void movePawn_oneFieldCorrectDirection() {
    Cell from = new Cell('d', 2);
    Cell to   = new Cell('d', 3);

    Pawn pawn = new Pawn(FigureColor.WHITE, from);
    Assertions.assertThat(pawn.canMoveTo(to)).isTrue();
    Assertions.assertThat(pawn.cell()).isEqualTo(from);
    pawn.moveTo(to);
    Assertions.assertThat(pawn.cell()).isEqualTo(to);
  }

  @Test
  void movePawn_oneFieldWrongDirection() {
    Cell from = new Cell('b', 2);
    Cell to   = new Cell('b', 1);

    Pawn pawn = new Pawn(FigureColor.WHITE, from);
    Assertions.assertThat(pawn.canMoveTo(to)).isFalse();
    Assertions.assertThat(pawn.cell()).isEqualTo(from);
    pawn.moveTo(to);
    Assertions.assertThat(pawn.cell()).isEqualTo(from);
  }

  @Test
  void movePawn_sameField() {
    Cell from = new Cell('b', 2);
    Cell to   = new Cell('b', 2);

    Pawn pawn = new Pawn(FigureColor.WHITE, from);
    Assertions.assertThat(pawn.canMoveTo(to)).isFalse();
    Assertions.assertThat(pawn.cell()).isEqualTo(from);
    pawn.moveTo(to);
    Assertions.assertThat(pawn.cell()).isEqualTo(from);
  }

  @Test
  void movePawn_twoFieldsOnStart() {
    Cell from = new Cell('d', 2);
    Cell to   = new Cell('d', 4);

    Pawn pawn = new Pawn(FigureColor.WHITE, from);
    Assertions.assertThat(pawn.canMoveTo(to)).isTrue();
    Assertions.assertThat(pawn.cell()).isEqualTo(from);
    pawn.moveTo(to);
    Assertions.assertThat(pawn.cell()).isEqualTo(to);
  }


  @Test
  void movePawn_twoFieldsOnlyOnStart() {
    Cell from = new Cell('d', 2);
    Cell to   = new Cell('d', 3);
    Cell then = new Cell('d', 5);

    Pawn pawn = new Pawn(FigureColor.WHITE, from);
    Assertions.assertThat(pawn.canMoveTo(to)).isTrue();
    pawn.moveTo(to);

    Assertions.assertThat(pawn.canMoveTo(then)).isFalse();
    pawn.moveTo(then);
    Assertions.assertThat(pawn.cell()).isEqualTo(to);
  }

  @Test
  void movePawn_threeFields() {
    Cell from = new Cell('b', 2);
    Cell to   = new Cell('b', 5);

    Pawn pawn = new Pawn(FigureColor.WHITE, from);
    Assertions.assertThat(pawn.canMoveTo(to)).isFalse();
    Assertions.assertThat(pawn.cell()).isEqualTo(from);
    pawn.moveTo(to);
    Assertions.assertThat(pawn.cell()).isEqualTo(from);
  }
}
