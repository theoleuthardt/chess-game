package hwr.oop.chess.application.figures;

import static org.assertj.core.api.Assertions.assertThat;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BishopTest {
    Board board;

    @BeforeEach
    void setUp() {
        // Initialize the board
        board = new Board(false);
    }

    @Test
    void bishop_hasTypeBishop() {
        Bishop bishop = new Bishop(FigureColor.WHITE);
        assertThat(bishop.type()).isEqualTo(FigureType.BISHOP);
    }

    @Test
    void moveBishop_diagonalIsValid() {
        Bishop bishop = new Bishop(FigureColor.WHITE);
        Cell from = board.findCell(4, 4);
        from.setFigure(bishop);

        for (int distance : List.of(1, 2, 3)) {
            assertThat(bishop.canMoveTo(from, board.findCell(4 - distance, 4 - distance))).isTrue();
            assertThat(bishop.canMoveTo(from, board.findCell(4 + distance, 4 - distance))).isTrue();
            assertThat(bishop.canMoveTo(from, board.findCell(4 - distance, 4 + distance))).isTrue();
            assertThat(bishop.canMoveTo(from, board.findCell(4 + distance, 4 + distance))).isTrue();
        }
    }


    @Test
    void moveBishop_straightIsInvalid() {
        Bishop bishop = new Bishop(FigureColor.BLACK);
        Cell from = board.findCell(4, 4);
        from.setFigure(bishop);

        for (int distance : List.of(1, 2, 3)) {
            assertThat(bishop.canMoveTo(from, board.findCell(4 - distance, 4))).isFalse();
            assertThat(bishop.canMoveTo(from, board.findCell(4 + distance, 4))).isFalse();
            assertThat(bishop.canMoveTo(from, board.findCell(4, 4 - distance))).isFalse();
            assertThat(bishop.canMoveTo(from, board.findCell(4, 4 + distance))).isFalse();
        }
    }

    @Test
    void moveBishop_cannotStayOnItsOwnField() {
        Bishop bishop = new Bishop(FigureColor.BLACK);
        Cell from = board.findCell(4, 4);
        from.setFigure(bishop);

        assertThat(bishop.canMoveTo(from, from)).isFalse();
    }

    @Test
    void moveBishop_cannotMoveInACurve() {
        Bishop bishop = new Bishop(FigureColor.BLACK);
        int x = 4;
        int y = 4;
        Cell from = board.findCell(x, y);
        from.setFigure(bishop);

        assertThat(bishop.canMoveTo(from, board.findCell(x + 1, y + 2))).isFalse();
        assertThat(bishop.canMoveTo(from, board.findCell(x - 1, y + 2))).isFalse();
        assertThat(bishop.canMoveTo(from, board.findCell(x - 2, y + 3))).isFalse();
    }
}
