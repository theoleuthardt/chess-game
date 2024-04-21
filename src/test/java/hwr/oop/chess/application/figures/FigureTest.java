package hwr.oop.chess.application.figures;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import hwr.oop.chess.application.Position;

class FigureTest {
  @Test
  void createPawn() {
    Position position = new Position('b', 2);
    PawnFigure pawn = new PawnFigure(FigureColor.BLACK, position);
    Assertions.assertThat(pawn.color()).isEqualTo(FigureColor.BLACK);
    Assertions.assertThat(pawn.type()).isEqualTo(FigureType.PAWN);
    Assertions.assertThat(pawn.position()).isEqualTo(position);
  }

  @Test
  void movePawn_oneFieldCorrectDirection() {
    Position from = new Position('d', 2);
    Position to   = new Position('d', 3);

    PawnFigure pawn = new PawnFigure(FigureColor.WHITE, from);
    Assertions.assertThat(pawn.canMoveTo(to)).isTrue();
    Assertions.assertThat(pawn.position()).isEqualTo(from);
    pawn.moveTo(to);
    Assertions.assertThat(pawn.position()).isEqualTo(to);
  }

  @Test
  void movePawn_oneFieldWrongDirection() {
    Position from = new Position('b', 2);
    Position to   = new Position('b', 1);

    PawnFigure pawn = new PawnFigure(FigureColor.WHITE, from);
    Assertions.assertThat(pawn.canMoveTo(to)).isFalse();
    Assertions.assertThat(pawn.position()).isEqualTo(from);
    pawn.moveTo(to);
    Assertions.assertThat(pawn.position()).isEqualTo(from);
  }

  @Test
  void movePawn_sameField() {
    Position from = new Position('b', 2);
    Position to   = new Position('b', 2);

    PawnFigure pawn = new PawnFigure(FigureColor.WHITE, from);
    Assertions.assertThat(pawn.canMoveTo(to)).isFalse();
    Assertions.assertThat(pawn.position()).isEqualTo(from);
    pawn.moveTo(to);
    Assertions.assertThat(pawn.position()).isEqualTo(from);
  }

  @Test
  void movePawn_twoFieldsOnStart() {
    Position from = new Position('d', 2);
    Position to   = new Position('d', 4);

    PawnFigure pawn = new PawnFigure(FigureColor.WHITE, from);
    Assertions.assertThat(pawn.canMoveTo(to)).isTrue();
    Assertions.assertThat(pawn.position()).isEqualTo(from);
    pawn.moveTo(to);
    Assertions.assertThat(pawn.position()).isEqualTo(to);
  }


  @Test
  void movePawn_twoFieldsOnlyOnStart() {
    Position from = new Position('d', 2);
    Position to   = new Position('d', 3);
    Position then = new Position('d', 5);

    PawnFigure pawn = new PawnFigure(FigureColor.WHITE, from);
    Assertions.assertThat(pawn.canMoveTo(to)).isTrue();
    pawn.moveTo(to);

    Assertions.assertThat(pawn.canMoveTo(then)).isFalse();
    pawn.moveTo(then);
    Assertions.assertThat(pawn.position()).isEqualTo(to);
  }

  @Test
  void movePawn_threeFields() {
    Position from = new Position('b', 2);
    Position to   = new Position('b', 5);

    PawnFigure pawn = new PawnFigure(FigureColor.WHITE, from);
    Assertions.assertThat(pawn.canMoveTo(to)).isFalse();
    Assertions.assertThat(pawn.position()).isEqualTo(from);
    pawn.moveTo(to);
    Assertions.assertThat(pawn.position()).isEqualTo(from);
  }
}
