package hwr.oop.chess.application.figures;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import hwr.oop.chess.application.Position;

class FigureTest {
  @Test
  void createPawn() {
    Position position = new Position(2, 2);
    PawnFigure pawn = new PawnFigure(FigureColor.BLACK, 2,2);
    Assertions.assertThat(pawn.getColor()).isEqualTo(FigureColor.BLACK);
    Assertions.assertThat(pawn.getType()).isEqualTo(FigureType.PAWN);
    Assertions.assertThat(pawn.getPosition().x()).isEqualTo(position.x());
    Assertions.assertThat(pawn.getPosition().y()).isEqualTo(position.y());
  }
//
//  @Test
//  void movePawn_oneFieldCorrectDirection() {
//    Position from = new Position('d', 2);
//    Position to   = new Position('d', 3);
//
//    PawnFigure pawn = new PawnFigure(FigureColor.WHITE, 2,2);
//    Assertions.assertThat(pawn.canMoveTo(to)).isTrue();
//    Assertions.assertThat(pawn.getPosition()).isEqualTo(from);
//    pawn.moveTo(4,3);
//    Assertions.assertThat(pawn.getPosition()).isEqualTo(to);
//  }
//
//  @Test
//  void movePawn_oneFieldWrongDirection() {
//    Position from = new Position('b', 2);
//    Position to   = new Position('b', 1);
//
//    PawnFigure pawn = new PawnFigure(FigureColor.WHITE, 2,2);
//    Assertions.assertThat(pawn.canMoveTo(to)).isFalse();
//    Assertions.assertThat(pawn.getPosition()).isEqualTo(from);
//    pawn.moveTo(to.x(), to.y());
//    Assertions.assertThat(pawn.getPosition()).isEqualTo(from);
//  }
//
//  @Test
//  void movePawn_sameField() {
//    Position from = new Position('b', 2);
//    Position to   = new Position('b', 2);
//
//    PawnFigure pawn = new PawnFigure(FigureColor.WHITE, 2,2);
//    Assertions.assertThat(pawn.canMoveTo(to)).isFalse();
//    Assertions.assertThat(pawn.getPosition()).isEqualTo(from);
//    pawn.moveTo(to.x(), to.y());
//    Assertions.assertThat(pawn.getPosition()).isEqualTo(from);
//  }
//
//  @Test
//  void movePawn_twoFieldsOnStart() {
//    Position from = new Position('d', 2);
//    Position to   = new Position('d', 4);
//
//    PawnFigure pawn = new PawnFigure(FigureColor.WHITE, 4,2);
//    Assertions.assertThat(pawn.canMoveTo(to)).isTrue();
//    Assertions.assertThat(pawn.getPosition()).isEqualTo(from);
//    pawn.moveTo(to.x(), to.y());
//    Assertions.assertThat(pawn.getPosition()).isEqualTo(to);
//  }
//
//
//  @Test
//  void movePawn_twoFieldsOnlyOnStart() {
//    Position from = new Position('d', 2);
//    Position to   = new Position('d', 3);
//    Position then = new Position('d', 5);
//
//    PawnFigure pawn = new PawnFigure(FigureColor.WHITE, 4,2);
//    Assertions.assertThat(pawn.canMoveTo(to)).isTrue();
//    pawn.moveTo(to.x(), to.y());
//
//    Assertions.assertThat(pawn.canMoveTo(then)).isFalse();
//    pawn.moveTo(then.x(), then.y());
//    Assertions.assertThat(pawn.getPosition()).isEqualTo(to);
//  }
//
//  @Test
//  void movePawn_threeFields() {
//    Position from = new Position('b', 2);
//    Position to   = new Position('b', 5);
//
//    PawnFigure pawn = new PawnFigure(FigureColor.WHITE, 2,2);
//    Assertions.assertThat(pawn.canMoveTo(to)).isFalse();
//    Assertions.assertThat(pawn.getPosition()).isEqualTo(from);
//    pawn.moveTo(to.x(), to.y());
//    Assertions.assertThat(pawn.getPosition()).isEqualTo(from);
//  }
}
