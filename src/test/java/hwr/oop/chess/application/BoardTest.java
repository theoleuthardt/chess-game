package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.*;
import hwr.oop.chess.cli.InvalidUserInputException;
import hwr.oop.chess.persistence.FenNotation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BoardTest {
  private Board board;

  @BeforeEach
  void setUp() {
    board = new Board(false);
  }

  void assertThatFigureOnCellHasSymbol(int x, int y, char symbol) {
    Cell cell = board.findCell(x, y);
    assertThat(cell.isFree()).isFalse();
    assertThat(cell.figure().symbol()).isEqualTo(symbol);
  }

  void assertThatAllFiguresAreInDefaultPosition() {
    for (int x = 1; x <= 8; x++) {
      // row of black figures
      assertThatFigureOnCellHasSymbol(x, 8, " rnbqkbnr ".charAt(x));
      assertThatFigureOnCellHasSymbol(x, 7, " pppppppp ".charAt(x));

      assertThat(board.findCell(x, 6).isFree()).isTrue();
      assertThat(board.findCell(x, 5).isFree()).isTrue();
      assertThat(board.findCell(x, 4).isFree()).isTrue();
      assertThat(board.findCell(x, 3).isFree()).isTrue();

      // row of white figures
      assertThatFigureOnCellHasSymbol(x, 2, " PPPPPPPP ".charAt(x));
      assertThatFigureOnCellHasSymbol(x, 1, " RNBQKBNR ".charAt(x));
    }
  }

  @Test
  void boardConstructorWithFigures() {
    board = new Board(true);
    assertThatAllFiguresAreInDefaultPosition();
  }

  @Test
  void boardAddFiguresToBoardWithDefaultPositions() {
    board.addFiguresToBoard();
    assertThatAllFiguresAreInDefaultPosition();
  }

  @Test
  void initialBoard_withoutFiguresIsEmpty() {
    List<Cell> cells = board.allCells();
    assertThat(cells).hasSize(64);
    for (Cell cell : cells) {
      assertThat(cell.isFree()).isTrue();
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
    for (int x : List.of(2, 3, 4, 5, 6, 7)) {
      for (int y : List.of(2, 3, 4, 5, 6, 7)) {
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
    return switch (direction) {
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
    List<Cell> cells = board.allCells();

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
        .isInstanceOf(InvalidUserInputException.class);
    assertThatThrownBy(() -> board.moveFigure(4, 1, 9, 9))
        .isInstanceOf(InvalidUserInputException.class);
  }

  @Test
  void testCheckMateBlackKing_h7() {
    // Status CheckMate
    String fenString = "2K5/1B6/8/8/8/4b2N/R7/4r2k b - - 0 4";
    FenNotation.parseFEN(board, fenString);
    assertThat(board.isCheckmate(FigureColor.BLACK)).isTrue();
  }

  @Test
  void testDoesNotCheckMate() {
    // Given
    String fenString = "rnb1kb1r/pppp1Qpp/5n2/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 4";
    FenNotation.parseFEN(board, fenString);

    // Status: Black is checked, but no checkmated
    assertThat(board.isCheck(FigureColor.BLACK)).isTrue();
    assertThat(board.isCheckmate(FigureColor.BLACK)).isFalse();

    // King can only move to "d8"
    Cell kingCell = board.findKing(FigureColor.BLACK);
    List<Cell> availableCells = board.availableCellsWithoutCheckMoves(kingCell);
    assertThat(availableCells).hasSize(1); // TODO Fix: King's getAvailableCells
  }

  @Test
  void testCheckMateBlackKing_e4() {
    String fenString = "8/4Q1R1/R7/5k2/3pP3/5K2/8/8";
    FenNotation.parseFENOnlyPiecePlacement(board, fenString);
    assertThat(board.isCheckmate(FigureColor.BLACK)).isTrue();
  }

  @Test
  void testFindCellWithString() {
    // invalid Value
    assertThatThrownBy(() -> board.findCell("c3d")).isInstanceOf(InvalidUserInputException.class);
    assertThatThrownBy(() -> board.findCell("a")).isInstanceOf(InvalidUserInputException.class);
    assertNotNull(board.findCell("a1"));
    assertNotNull(board.findCell("e5"));
  }

  @Test
  void testAddFiguresToBoard() {
    // Initial Status as string
    String string = "rnbqkbnrpppppppp                                PPPPPPPPRNBQKBNR";
    board.addFiguresToBoard(string);
    assertThatAllFiguresAreInDefaultPosition();
  }

  @Test
  void testAddFiguresToBoardWithLongString() {
    // Initial Status as string
    String string = "rnbqkbnrpppppppp                                PPPPPPPPRNBQKBNR_aaa";
    assertThatThrownBy(() -> board.addFiguresToBoard(string))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void testFiguresOnBoard() {
    board.addFiguresToBoard();
    String initial = "rnbqkbnrpppppppp                                PPPPPPPPRNBQKBNR";
    String string = board.figuresOnBoard();
    assertThat(string).isEqualTo(initial);
  }

  @Test
  void testMoveFigureWithInteger() {
    board.addFiguresToBoard();
    board.moveFigure(4, 2, 4, 4);
    assertThat(board.findCell(4, 2).figure()).isNull();
    assertThat(board.findCell(4, 4).figure().type()).isEqualTo(FigureType.PAWN);
  }

  @Test
  void testChangeTurn() {
    board = new Board(true);
    board.moveFigure(2, 2, 2, 4);
    assertThat(board.turn()).isEqualTo(FigureColor.BLACK);
  }

  @Test
  void testCastlingKingAndQueen() {
    board = new Board(true);
    // only keep Kings and Rooks on the board
    for (Cell cell : board.allCells()) {
      if (!cell.isOccupiedBy(FigureType.KING) && !cell.isOccupiedBy(FigureType.ROOK)) {
        cell.setFigure(null);
      }
    }
    assertThat(board.canPerformKingSideCastling(FigureColor.WHITE)).isTrue();
    assertThat(board.canPerformQueenSideCastling(FigureColor.WHITE)).isTrue();
    assertThat(board.canPerformKingSideCastling(FigureColor.BLACK)).isTrue();
    assertThat(board.canPerformQueenSideCastling(FigureColor.BLACK)).isTrue();
  }

  @Test
  void testFullmoveNumber() {
    assertThat(board.fullMove()).isZero();
  }

  @Test
  void testHalfmoveClockBlack() {
    assertThat(board.halfMove()).isZero();
  }

  @Test
  void testInitialPosition() {
    board = new Board(true);
    assertThat(board.findCell(1, 1).figure().type()).isEqualTo(FigureType.ROOK);
    assertThat(board.findCell(2, 1).figure().type()).isEqualTo(FigureType.KNIGHT);
    assertThat(board.findCell(3, 1).figure().type()).isEqualTo(FigureType.BISHOP);
    assertThat(board.findCell(4, 1).figure().type()).isEqualTo(FigureType.QUEEN);
    assertThat(board.findCell(5, 1).figure().type()).isEqualTo(FigureType.KING);
    assertThat(board.findCell(6, 1).figure().type()).isEqualTo(FigureType.BISHOP);
    assertThat(board.findCell(7, 1).figure().type()).isEqualTo(FigureType.KNIGHT);
    assertThat(board.findCell(8, 1).figure().type()).isEqualTo(FigureType.ROOK);
    assertThat(board.findCell(1, 8).figure().type()).isEqualTo(FigureType.ROOK);
    assertThat(board.findCell(2, 8).figure().type()).isEqualTo(FigureType.KNIGHT);
    assertThat(board.findCell(3, 8).figure().type()).isEqualTo(FigureType.BISHOP);
    assertThat(board.findCell(4, 8).figure().type()).isEqualTo(FigureType.QUEEN);
    assertThat(board.findCell(5, 8).figure().type()).isEqualTo(FigureType.KING);
    assertThat(board.findCell(6, 8).figure().type()).isEqualTo(FigureType.BISHOP);
    assertThat(board.findCell(7, 8).figure().type()).isEqualTo(FigureType.KNIGHT);
    assertThat(board.findCell(8, 8).figure().type()).isEqualTo(FigureType.ROOK);

    IntStream.range(1, 9)
        .forEach(
            i -> {
              assertThat(board.findCell(i, 2).figure().type()).isEqualTo(FigureType.PAWN);
              assertThat(board.findCell(i, 7).figure().type()).isEqualTo(FigureType.PAWN);
            });
  }

  @Test
  void testInitialColor() {
    board.addFiguresToBoard();
    IntStream.range(1, 9)
        .forEach(
            i -> {
              assertThat(board.findCell(i, 1).figure().color()).isEqualTo(FigureColor.WHITE);
              assertThat(board.findCell(i, 2).figure().color()).isEqualTo(FigureColor.WHITE);
              assertThat(board.findCell(i, 7).figure().color()).isEqualTo(FigureColor.BLACK);
              assertThat(board.findCell(i, 8).figure().color()).isEqualTo(FigureColor.BLACK);
            });
  }

  @Test // TODO Fix En Passant
  void testEnPassant() {
    // given
    board = new Board(false);
    String fen = "rnbqkbnr/ppp1pppp/8/8/3p4/8/PPP1PPPP/RNBQKBNR w KQkq - 0 2";
    FenNotation.parseFEN(board, fen);

    // White Pawn two field moved
    board.moveFigure("e2", "e4");
    String availableEnPassant = "rnbqkbnr/ppp1pppp/8/8/3pP3/8/PPP2PPP/RNBQKBNR b KQkq e3 0 2";
    assertThat(FenNotation.generateFen(board)).isEqualTo(availableEnPassant);

    // Black Pawn catch white pawn using en passnt
    board.moveFigure("d4", "e3");
    assertThat(board.findCell('e', 3).figure().type()).isEqualTo(FigureType.PAWN);
    assertThat(board.findCell('e', 4).figure()).isNull();

    // Check Status
    String afterEnPassant = "rnbqkbnr/ppp1pppp/8/8/8/4p3/PPP2PPP/RNBQKBNR w KQkq - 0 3";
    assertThat(FenNotation.generateFen(board)).isEqualTo(afterEnPassant);
  }

  @Test
  void testHalfMove() {
    // Given
    board = new Board(false); // Create Without Figure
    String initialStatus = "rn1qkbnr/ppp1pppp/8/5b2/5P2/4p3/PPP3PP/RNBQKBNR w KQkq - 1 4";
    FenNotation.parseFEN(board, initialStatus); // Set Figure on Board

    // Move white bishop
    board.moveFigure("f1", "b5");
    String afterBishopMove = "rn1qkbnr/ppp1pppp/8/1B3b2/5P2/4p3/PPP3PP/RNBQK1NR b KQkq - 2 4";

    // Compare Status Of Board
    String changedStatus = FenNotation.generateFen(board);
    assertThat(changedStatus).isEqualTo(afterBishopMove);
  }

  @Test
  void testCheckBlack() {
    // Given: Black checked
    board = new Board(false);
    String initialStatus = "rn1qkbnr/1pp1pppp/8/pB3b2/5P2/4p3/PPP3PP/RNBQK1NR b KQkq - 2 5";
    FenNotation.parseFEN(board, initialStatus);

    assertThat(board.isCheck(FigureColor.BLACK)).isTrue();
  }

  @Test
  void testAvailableCellsIfCheckBlack() {
    // Given: Black checked
    board = new Board(false);
    String initialStatus = "rn1qkbnr/1pp1pppp/8/pB3b2/5P2/4p3/PPP3PP/RNBQK1NR b KQkq - 2 5";
    FenNotation.parseFEN(board, initialStatus);

    Cell cellKingBlack = board.findKing(FigureColor.BLACK);
    assertThat(board.availableCellsWithoutCheckMoves(cellKingBlack)).isEmpty();

    Cell cellQueenBlack = board.findCell("d8");
    assertThat(board.availableCellsWithoutCheckMoves(cellQueenBlack)).hasSize(1);

    Cell cellPawnBlack = board.findCell("c7");
    assertThat(board.availableCellsWithoutCheckMoves(cellPawnBlack)).hasSize(1);

    Cell cellBishopBlack = board.findCell("a8");
    assertThat(board.availableCellsWithoutCheckMoves(cellBishopBlack)).isEmpty();

    Cell cellKnightBlack = board.findCell("g8");
    assertThat(board.availableCellsWithoutCheckMoves(cellKnightBlack)).isEmpty();

    Cell cellRookBlack = board.findCell("a8");
    assertThat(board.availableCellsWithoutCheckMoves(cellRookBlack)).isEmpty();
  }

  /*
  Chess Match: Spasski–Fischer 0:1 Reykjavík, 20. Juli 1972
  */
  @Test
  void testChessGameSpasskiFischer() {
    board = new Board(true);
    board.moveFigure("d2", "d4"); // 1. d4
    board.moveFigure("g8", "f6"); // 1... Nf6
    board.moveFigure("c2", "c4"); // 2. c4
    board.moveFigure("e7", "e6"); // 2... e6
    board.moveFigure("b1", "c3"); // 3. Nc3
    board.moveFigure("f8", "b4"); // 3... Bb4
    board.moveFigure("g1", "f3"); // 4. Nf3
    board.moveFigure("c7", "c5"); // 4... c5
    board.moveFigure("e2", "e3"); // 5. e3
    board.moveFigure("b8", "c6"); // 5... Nc6
    board.moveFigure("f1", "d3"); // 6. Bd3
    board.moveFigure("b4", "c3"); // 6... Bxc3+
    // Black Bishop catches White Knight and White is checked
    assertThat(board.isCheck(FigureColor.WHITE)).isTrue();
    assertThat(board.cellsWithColor(FigureColor.WHITE)).hasSize(15);
    assertThat(board.cellsWithColor(FigureColor.BLACK)).hasSize(16);

    board.moveFigure('b', 2, 'c', 3); // 7. bxc3
    assertThat(board.cellsWithColor(FigureColor.BLACK)).hasSize(15);

    board.moveFigure("d7", "d6"); // 7... d6
    board.moveFigure("e3", "e4"); // 8. e4
    board.moveFigure("e6", "e5"); // 8... e5
    board.moveFigure("d4", "d5"); // 9. d5
    board.moveFigure("c6", "e7"); // 9... Ne7
    board.moveFigure("f3", "h4"); // 10. Nh4
    board.moveFigure("h7", "h6"); // 10... h6
    board.moveFigure("f2", "f4"); // 11. f4
    board.moveFigure("e7", "g6"); // 11... Ng6
    board.moveFigure("h4", "g6"); // 12. Bxg6
    board.moveFigure("f7", "g6"); // 12... fxg6
    board.moveFigure("f4", "e5"); // 13. fxe5
    board.moveFigure("d6", "e5"); // 13... dxe5
    board.moveFigure("c1", "e3"); // 14. Be3
    board.moveFigure("b7", "b6"); // 14... b6

    String fen = FenNotation.generateFen(board);
    assertThat(fen).isEqualTo("r1bqk2r/p5p1/1p3npp/2pPp3/2P1P3/2PBB3/P5PP/R2QK2R w KQkq - 0 14");
    assertThat(((King) board.findCell(5, 1).figure()).hasMoved()).isFalse();
    assertThat(((King) board.findCell(5, 8).figure()).hasMoved()).isFalse();

    board.moveFigure('e', 1, 'g', 1); // 15. O-O White King Castling
    fen = FenNotation.generateFen(board);
    assertThat(fen).isEqualTo("r1bqk2r/p5p1/1p3npp/2pPp3/2P1P3/2PBB3/P5PP/R2Q1RK1 b kq - 1 14");
    assertThat(board.canPerformKingSideCastling(FigureColor.WHITE)).isFalse();
    assertThat(board.canPerformKingSideCastling(FigureColor.BLACK)).isTrue();
    assertThat(((Rook) board.findCell(6, 1).figure()).hasMoved()).isTrue();
    assertThat(((King) board.findCell(7, 1).figure()).hasMoved()).isTrue();

    board.moveFigure('e', 8, 'g', 8); // 15. O-O Black King Castling
    fen = FenNotation.generateFen(board);
    assertThat(fen).isEqualTo("r1bq1rk1/p5p1/1p3npp/2pPp3/2P1P3/2PBB3/P5PP/R2Q1RK1 w  - 2 15");
    assertThat(board.canPerformKingSideCastling(FigureColor.WHITE)).isFalse();
    assertThat(((Rook) board.findCell(6, 8).figure()).hasMoved()).isTrue();
    assertThat(((King) board.findCell(7, 8).figure()).hasMoved()).isTrue();
  }
}
