package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FigureColorTest {
    @Test
    void testEnumValues() { // Test if the enum contains the valid values
        assertEquals(2, FigureColor.values().length); // Check if there are exactly 2 values
        assertEquals(FigureColor.WHITE, FigureColor.valueOf("WHITE"));
        assertEquals(FigureColor.BLACK, FigureColor.valueOf("BLACK"));
    }

    @Test
    void whiteFiguresHaveUppercaseSymbol() {
        Board board = new Board(true);
        List<Cell> cells = board.allCells().stream().filter(cell -> List.of(1, 2).contains(cell.y())).toList();
        for (Cell cell : cells) {
            assertThat(cell.figure()).isNotNull();
            assertThat(cell.figure().color()).isEqualTo(FigureColor.WHITE);
            assertThat(cell.figure().symbol()).isUpperCase();
        }
    }

    @Test
    void blackFiguresHaveLowercaseSymbol() {
        Board board = new Board(true);
        List<Cell> cells = board.allCells().stream().filter(cell -> List.of(7, 8).contains(cell.y())).toList();
        for (Cell cell : cells) {
            assertThat(cell.figure()).isNotNull();
            assertThat(cell.figure().color()).isEqualTo(FigureColor.BLACK);
            assertThat(cell.figure().symbol()).isLowerCase();
        }
    }
}
