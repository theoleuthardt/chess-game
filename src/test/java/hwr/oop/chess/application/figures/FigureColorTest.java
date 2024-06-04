package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.Coordinate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FigureColorTest {
  @Test
  void testEnumValues() { // Test if the enum contains the valid values
    assertThat(FigureColor.values()).hasSize(2); // Check if there are exactly 2 values
    assertThat(FigureColor.valueOf("WHITE")).isEqualTo(FigureColor.WHITE);
    assertThat(FigureColor.valueOf("BLACK")).isEqualTo(FigureColor.BLACK);
  }

  @Test
  void whiteFiguresHaveUppercaseSymbol() {
    Board board = new Board(true);
    List<Cell> cells =
        board.allCells().stream()
            .filter(cell -> List.of(Coordinate.ONE, Coordinate.TWO).contains(cell.y()))
            .toList();
    for (Cell cell : cells) {
      assertThat(cell.figure()).isNotNull();
      assertThat(cell.figure().color()).isEqualTo(FigureColor.WHITE);
      assertThat(cell.figure().symbol()).isUpperCase();
    }
  }

  @Test
  void blackFiguresHaveLowercaseSymbol() {
    Board board = new Board(true);
    List<Cell> cells =
        board.allCells().stream()
            .filter(cell -> List.of(Coordinate.SEVEN, Coordinate.EIGHT).contains(cell.y()))
            .toList();
    for (Cell cell : cells) {
      assertThat(cell.figure()).isNotNull();
      assertThat(cell.figure().color()).isEqualTo(FigureColor.BLACK);
      assertThat(cell.figure().symbol()).isLowerCase();
    }
  }
}
