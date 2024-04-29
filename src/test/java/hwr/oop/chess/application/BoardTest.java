package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.Figure;
import hwr.oop.chess.application.figures.FigureType;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito; // TODO learn about Mockito for TEST

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.IntStream;

class BoardTest {
  private Board board;
  Logger logger = Logger.getLogger(BoardTest.class.getName());

  @BeforeEach
  void setUp() {
    // Initialize the board
    board = Mockito.spy(new Board(true));
  }

  @Test
  void initialBoard_withBlackFiguresOnTop() {
    ArrayList<Cell> cells = board.allCells();

    // row of black figures
    String topRow = "rnbqkbnr";
    for(int x = 1; x <= 8; x++) {
      Cell cell = board.findCell(x, 8);
      char correctSymbol = topRow.charAt(x - 1);
      assertThat(cell.figure().symbol()).isEqualTo(correctSymbol);
    }

    // row of pawns
    for(int x = 1; x <= 8; x++) {
      Cell cell = board.findCell(x, 7);
      assertThat(cell.figure().symbol()).isEqualTo('p');
    }
  }

  @Test
  void initialBoard_withWhiteFiguresOnBottom() {
    // row of white figures
    String bottomRow = "RNBQKBNR";
    for(int x = 1; x <= 8; x++) {
      Cell cell = board.findCell(x, 1);
      char correctSymbol = bottomRow.charAt(x - 1);
      assertThat(cell.figure()).isNotNull();
      assertThat(cell.figure().symbol()).isEqualTo(correctSymbol);
    }

    // row of pawns
    for(int x = 1; x <= 8; x++) {
      Cell cell = board.findCell(x, 2);
      assertThat(cell.figure().symbol()).isEqualTo('P');
    }
  }

  @Test
  void initialBoard_onlyMiddleIsFree() {
    ArrayList<Cell> cells = board.allCells();
    assertThat(cells).hasSize(64);
    for(Cell cell : cells) {
      if(cell.y() != 1 && cell.y() != 2 && cell.y() != 7 && cell.y() != 8) {
        assertThat(cell.figure()).isNull();
      } else {
        assertThat(cell.figure()).isNotNull();
      }
    }
  }

  @Test
  void initialBoard_withoutFiguresIsEmpty() {
    Board boardNoFigure = new Board(false);
    ArrayList<Cell> cells = boardNoFigure.allCells();
    assertThat(cells).hasSize(64);
    for(Cell cell : cells) {
      assertThat(cell.figure()).isNull();
    }
  }

  @Test
  void outerCells_leftCellColumnHasNoLeftCell() {
    for (int y : IntStream.range(1, 9).toArray()) {
      Cell cell = board.findCell(1, y);
      assertThat(cell).isNotNull();
      assertThat(cell.leftCell()).isNull();
      assertThat(cell.topLeftCell()).isNull();
      assertThat(cell.bottomLeftCell()).isNull();
      assertThat(cell.rightCell()).isNotNull();
    }
  }

  @Test
  void outerCells_rightCellColumnHasNoRightCell() {
    for (int y : IntStream.range(1, 9).toArray()) {
      Cell cell = board.findCell(8, y);
      assertThat(cell).isNotNull();
      assertThat(cell.rightCell()).isNull();
      assertThat(cell.topRightCell()).isNull();
      assertThat(cell.bottomRightCell()).isNull();
      assertThat(cell.leftCell()).isNotNull();
    }
  }

  @Test
  void outerCells_topCellRowHasNoTopCell() {
    for (int x : IntStream.range(1, 9).toArray()) {
      Cell cell = board.findCell(x, 8);
      assertThat(cell).isNotNull();
      assertThat(cell.topCell()).isNull();
      assertThat(cell.topLeftCell()).isNull();
      assertThat(cell.topRightCell()).isNull();
      assertThat(cell.bottomCell()).isNotNull();
    }
  }
  @Test
  void outerCells_bottomCellRowHasNoBottomCell() {
    for (int x : IntStream.range(1, 9).toArray()) {
      Cell cell = board.findCell(x, 1);
      assertThat(cell).isNotNull();
      assertThat(cell.bottomCell()).isNull();
      assertThat(cell.bottomLeftCell()).isNull();
      assertThat(cell.bottomRightCell()).isNull();
      assertThat(cell.topCell()).isNotNull();
    }
  }

  @ParameterizedTest
  @EnumSource(CellDirection.class)
  void innerCells_hasNeighbourCellInEveryDirection(CellDirection direction) {
    for (int x : IntStream.range(2, 8).toArray()) {
      for (int y : IntStream.range(2, 8).toArray()) {
        Cell cell = board.findCell(x, y);
        assertThat(cell).isNotNull();
        assertThat(cell.cellInDirection(direction)).isNotNull();
      }
    }
  }

  @Test
  void firstCellIsNotNull() {
    assertThat(board.firstCell()).isNotNull();
  }

  @Test
  void cellsOutsideOfBoardDoNotExist() {
    assertThat(board.findCell(-1, -1)).isNull();
    assertThat(board.findCell(1, -1)).isNull();
    assertThat(board.findCell(-1, 1)).isNull();
    assertThat(board.findCell(9, 1)).isNull();
    assertThat(board.findCell(1, 9)).isNull();
  }

  private CellDirection getOppositeDirection(CellDirection direction) {
    return switch(direction) {
      case TOP -> CellDirection.BOTTOM;
      case TOP_LEFT -> CellDirection.BOTTOM_RIGHT;
      case TOP_RIGHT -> CellDirection.BOTTOM_LEFT;
      case BOTTOM -> CellDirection.TOP;
      case BOTTOM_LEFT -> CellDirection.TOP_RIGHT;
      case BOTTOM_RIGHT -> CellDirection.TOP_LEFT;
      case LEFT -> CellDirection.RIGHT;
      case RIGHT -> CellDirection.LEFT;
      default -> throw new IllegalArgumentException("You must pass a valid direction");
    };
  }

  @ParameterizedTest
  @EnumSource(CellDirection.class)
  void testBoardCells(CellDirection direction) {
    CellDirection returnDirection = getOppositeDirection(direction);
    ArrayList<Cell> cells = board.allCells();

    for (Cell currentCell : cells) {
      if (currentCell.cellInDirection(direction) != null) {
        Cell neighbourCell = currentCell.cellInDirection(direction);
        Cell startingCell = neighbourCell.cellInDirection(returnDirection);
        assertThat(startingCell).isEqualTo(currentCell);
      }
    }
  }

  @Test
  void testMoveFigureInvalidCoordinates() {
    Board board = new Board(false);
    assertThatThrownBy(() -> board.moveFigure(0, 1, 8, 8))
            .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> board.moveFigure(4, 1, 9, 9))
            .isInstanceOf(IllegalArgumentException.class);
  }

  /*
    Spasski–Fischer 0:1
    Reykjavík, 20. Juli 1972
  */
  //    @Test
  void testChessGameSpasskiFischer() {
    // TODO upgrade test
    board.moveFigure('d', 2, 'd', 4); // 1. d4
    board.moveFigure('g', 8, 'f', 6); // 1... Nf6
    board.moveFigure('c', 2, 'c', 4); // 2. c4
    board.moveFigure('e', 7, 'e', 6); // 2... e6
    board.moveFigure('b', 1, 'c', 3); // 3. Nc3
    board.moveFigure('b', 4, 'b', 4); // 3... Bb4
    board.moveFigure('g', 1, 'f', 3); // 4. Nf3
    board.moveFigure('c', 7, 'c', 5); // 4... c5
    board.moveFigure('e', 2, 'e', 4); // 5. e4
    board.moveFigure('g', 8, 'c', 6); // 5... Nc6
    board.moveFigure('d', 2, 'd', 3); // 6. Bd3
    board.moveFigure('b', 8, 'c', 3); // 6... Bxc3+
    board.moveFigure('b', 5, 'c', 4); // 7. bxc3
    board.moveFigure('d', 7, 'd', 6); // 7... d6
    board.moveFigure('e', 4, 'e', 5); // 8. e5
    board.moveFigure('g', 8, 'e', 7); // 8... Ne7
    board.moveFigure('d', 1, 'h', 5); // 9. d5
    board.moveFigure('f', 6, 'g', 6); // 9... h6
    board.moveFigure('f', 3, 'f', 4); // 10. f4
    board.moveFigure('g', 6, 'g', 5); // 10... Ng6
    board.moveFigure('g', 1, 'f', 2); // 11. fxe5
    board.moveFigure('g', 5, 'g', 6); // 11... fxg6
    board.moveFigure('f', 2, 'e', 3); // 12. fxe5
    board.moveFigure('e', 7, 'e', 5); // 12... dxe5
    board.moveFigure('c', 1, 'e', 3); // 13. Le3
    board.moveFigure('b', 6, 'b', 6); // 13... b6
    board.moveFigure('o', 0, 'o', 0); // 14. O-O
    board.moveFigure('o', 7, 'o', 0); // 14... O-O
    board.moveFigure('a', 2, 'a', 4); // 15. a4
    board.moveFigure('c', 8, 'd', 7); // 15... Bd7
    board.moveFigure('a', 1, 'b', 2); // 16. Rb1
    board.moveFigure('a', 8, 'b', 8); // 16... Rb8
    board.moveFigure('h', 1, 'f', 2); // 17. Rbf2
    board.moveFigure('d', 8, 'e', 7); // 17... Qe7
    board.moveFigure('d', 2, 'd', 4); // 18. Bd4
    board.moveFigure('f', 8, 'f', 8); // 18... Rbf8
    board.moveFigure('b', 2, 'd', 2); // 19. Rfd1
    board.moveFigure('d', 7, 'e', 8); // 19... Qe8
    board.moveFigure('a', 1, 'e', 1); // 20. Bc2
    board.moveFigure('g', 7, 'g', 5); // 20... g5
    board.moveFigure('c', 2, 'd', 1); // 21. Qd3
    board.moveFigure('e', 8, 'g', 6); // 21... Ng6
    board.moveFigure('f', 2, 'f', 8); // 22. Be1
    board.moveFigure('d', 8, 'g', 6); // 22... Qg6
    board.moveFigure('d', 1, 'd', 2); // 23. Qd2
    board.moveFigure('f', 6, 'h', 5); // 23... Nh5
    board.moveFigure('d', 4, 'f', 8); // 24. Rxf8+
    board.moveFigure('g', 6, 'f', 4); // 24... Rxf8
    board.moveFigure('d', 2, 'f', 4); // 25. Qxf8+
    board.moveFigure('f', 8, 'f', 4); // 25... Kxf8
    board.moveFigure('e', 3, 'f', 4); // 26. Be3
    board.moveFigure('g', 4, 'f', 4); // 26... Ngf4
    board.moveFigure('d', 1, 'f', 2); // 27. Bxf4
    board.moveFigure('f', 4, 'd', 5); // 27... Nxf4
    board.moveFigure('d', 3, 'd', 6); // 28. Qd6
    board.moveFigure('h', 5, 'f', 4); // 28... Qxf4
    board.moveFigure('f', 2, 'f', 4); // 29. Qxf4
    board.moveFigure('f', 7, 'f', 4); // 29... exf4
    board.moveFigure('f', 1, 'f', 2); // 30. Bf2
    board.moveFigure('f', 4, 'g', 4); // 30... Qxf4
    board.moveFigure('h', 3, 'g', 4); // 31. Nh4
    board.moveFigure('h', 7, 'h', 3); // 31... Qh3+
    board.moveFigure('f', 2, 'g', 2); // 32. Kg2
    board.moveFigure('h', 3, 'h', 4); // 32... Qg4#
  }

  //    @Test
  void testChessGame() {
    // TODO upgrade test
    board.moveFigure('e', 2, 'e', 4); // 1. e4
    board.moveFigure('e', 7, 'e', 5); // 1... e5
    board.moveFigure('g', 1, 'f', 3); // 2. Nf3
    board.moveFigure('b', 8, 'c', 6); // 2... Nc6
    board.moveFigure('f', 1, 'c', 4); // 3. Bc4
    board.moveFigure('f', 8, 'b', 4); // 3... Nf6
    board.moveFigure('o', 0, 'e', 1); // 4. O-O
    board.moveFigure('g', 7, 'e', 7); // 4... Be7
    board.moveFigure('h', 0, 'd', 1); // 5. Rd1
    board.moveFigure('b', 4, 'f', 6); // 5... d6
    board.moveFigure('c', 2, 'c', 3); // 6. c3
    board.moveFigure('f', 6, 'b', 7); // 6... a6
    board.moveFigure('f', 1, 'e', 1); // 7. Re1
    board.moveFigure('b', 7, 'd', 7); // 7... b5
    board.moveFigure('b', 1, 'c', 3); // 8. Bc3
    board.moveFigure('c', 7, 'c', 6); // 8... Nc6
    board.moveFigure('d', 1, 'f', 3); // 9. Nf3
    board.moveFigure('c', 6, 'e', 7); // 9... Be7
    board.moveFigure('f', 1, 'e', 3); // 10. Ne3
    board.moveFigure('d', 7, 'd', 5); // 10... d5
    board.moveFigure('e', 4, 'd', 5); // 11. exd5
    board.moveFigure('d', 8, 'd', 5); // 11... Nxd5
    board.moveFigure('c', 1, 'e', 3); // 12. Qe3
    board.moveFigure('e', 8, 'e', 8); // 12... Re8
    board.moveFigure('d', 2, 'd', 4); // 13. d4
    board.moveFigure('c', 6, 'c', 5); // 13... c6
    board.moveFigure('b', 2, 'c', 3); // 14. Bc3
    board.moveFigure('b', 8, 'e', 7); // 14... Be7
    board.moveFigure('c', 3, 'd', 2); // 15. Bd2
    board.moveFigure('c', 7, 'c', 6); // 15... c6
    board.moveFigure('b', 5, 'c', 6); // 16. dxc6
    board.moveFigure('c', 8, 'c', 6); // 16... Rxc6
    board.moveFigure('a', 2, 'b', 3); // 17. Qb3
    board.moveFigure('e', 7, 'e', 6); // 17... Be6
    board.moveFigure('b', 3, 'c', 4); // 18. Qc4
    board.moveFigure('b', 7, 'b', 5); // 18... Rb8
    board.moveFigure('b', 1, 'd', 3); // 19. Bd3
    board.moveFigure('a', 7, 'a', 5); // 19... Na5
    board.moveFigure('c', 3, 'c', 4); // 20. Qc4
    board.moveFigure('b', 5, 'b', 4); // 20... Nc4
    board.moveFigure('a', 1, 'd', 1); // 21. Qd1
    board.moveFigure('a', 8, 'b', 8); // 21... Rc8
    board.moveFigure('a', 3, 'a', 4); // 22. a4
    board.moveFigure('b', 4, 'a', 4); // 22... Qa5
    board.moveFigure('b', 3, 'b', 4); // 23. Qb4
    board.moveFigure('b', 8, 'a', 8); // 23... Ra8
    board.moveFigure('a', 4, 'b', 5); // 24. a5
    board.moveFigure('a', 8, 'a', 4); // 24... Na4
    board.moveFigure('b', 5, 'b', 6); // 25. Ba4
    board.moveFigure('a', 4, 'c', 6); // 25... Rc6
    board.moveFigure('a', 1, 'a', 7); // 26. Ra7
    board.moveFigure('a', 4, 'a', 7); // 26... Ra7
    board.moveFigure('c', 4, 'b', 3); // 27. Rc6
    board.moveFigure('a', 7, 'a', 6); // 27... Ra6
    board.moveFigure('b', 3, 'b', 4); // 28. Rb7
    board.moveFigure('a', 6, 'b', 5); // 28... Ra5
    board.moveFigure('b', 4, 'b', 5); // 29. Rb5
    board.moveFigure('a', 5, 'b', 6); // 29... Ra6
    board.moveFigure('a', 7, 'a', 5); // 30. Ra5
    board.moveFigure('a', 6, 'a', 5); // 30... Ra5
    board.moveFigure('d', 3, 'd', 4); // 31. Qd4
    board.moveFigure('b', 6, 'c', 7); // 31... Rc7
    board.moveFigure('d', 4, 'd', 5); // 32. Qd5
    board.moveFigure('a', 5, 'b', 4); // 32... Qb4
    board.moveFigure('d', 1, 'd', 3); // 33. Qd3
    board.moveFigure('b', 7, 'b', 6); // 33... Qb6
    board.moveFigure('f', 3, 'f', 4); // 34. Qf4
    board.moveFigure('b', 6, 'b', 7); // 34... Qb7
    board.moveFigure('e', 3, 'e', 5); // 35. Qe5
    board.moveFigure('f', 7, 'f', 4); // 35... f6
    board.moveFigure('g', 2, 'f', 1); // 36. Nf1
    board.moveFigure('h', 7, 'h', 5); // 36... Kh7
    board.moveFigure('a', 2, 'a', 3); // 37. Qa3
    board.moveFigure('f', 4, 'f', 5); // 37... Qc7
    board.moveFigure('f', 1, 'f', 2); // 38. Nf2
    board.moveFigure('f', 5, 'f', 4); // 38... f4
    board.moveFigure('g', 1, 'f', 1); // 39. Nf1
    board.moveFigure('e', 6, 'f', 5); // 39... exf4
    board.moveFigure('f', 2, 'f', 4); // 40. Nf4
    board.moveFigure('h', 5, 'f', 5); // 40... Qf7
    board.moveFigure('e', 5, 'f', 5); // 41. e5
    board.moveFigure('h', 6, 'e', 7); // 41... Qe7
    board.moveFigure('h', 1, 'f', 1); // 42. Nf1
    board.moveFigure('e', 7, 'e', 8); // 42... Qe8
    board.moveFigure('f', 4, 'f', 3); // 43. Nf3
    board.moveFigure('f', 8, 'g', 7); // 43... Qg7
    board.moveFigure('g', 1, 'f', 1); // 44. Nf1
    board.moveFigure('d', 8, 'e', 7); // 44... Qe7
    board.moveFigure('h', 2, 'h', 3); // 45. Nh3
    board.moveFigure('e', 8, 'g', 8); // 45... Kg8
    board.moveFigure('h', 3, 'g', 4); // 46. Ng4
    board.moveFigure('f', 5, 'g', 4); // 46... hg4
    board.moveFigure('f', 1, 'g', 2); // 47. Ng2
    board.moveFigure('g', 7, 'h', 4); // 47... Qh4
    board.moveFigure('h', 3, 'g', 4); // 48. Ng4
    board.moveFigure('f', 5, 'g', 4); // 48... hg4
    board.moveFigure('g', 2, 'h', 3); // 49. Nh3
    board.moveFigure('g', 4, 'h', 3); // 49... Qg4#
  }
}
