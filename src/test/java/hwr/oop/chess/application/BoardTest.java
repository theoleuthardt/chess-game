package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.*;
import hwr.oop.chess.cli.InvalidUserInputException;
import hwr.oop.chess.persistence.FenNotation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
  private Board board;

  @BeforeEach
  void setUp() {
    board = new Board(false);
  }

  void assertThatFigureOnCellHasSymbol(Coordinate x, Coordinate y, char symbol) {
    Cell cell = board.findCell(x, y);
    assertThat(cell.isFree()).isFalse();
    assertThat(cell.figure().symbol()).isEqualTo(symbol);
  }

  void assertThatAllFiguresAreInDefaultPosition() {
    for (Coordinate x : Coordinate.values()) {
      // row of black figures
      assertThatFigureOnCellHasSymbol(x, Coordinate.EIGHT, " rnbqkbnr ".charAt(x.toInt()));
      assertThatFigureOnCellHasSymbol(x, Coordinate.SEVEN, " pppppppp ".charAt(x.toInt()));

      assertThat(board.findCell(x, Coordinate.SIX).isFree()).isTrue();
      assertThat(board.findCell(x, Coordinate.FIVE).isFree()).isTrue();
      assertThat(board.findCell(x, Coordinate.FOUR).isFree()).isTrue();
      assertThat(board.findCell(x, Coordinate.THREE).isFree()).isTrue();

      // row of white figures
      assertThatFigureOnCellHasSymbol(x, Coordinate.TWO, " PPPPPPPP ".charAt(x.toInt()));
      assertThatFigureOnCellHasSymbol(x, Coordinate.ONE, " RNBQKBNR ".charAt(x.toInt()));
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
    for (Coordinate y : Coordinate.values()) {
      Cell cell = board.findCell(Coordinate.fromChar('a'), y);
      assertThat(cell).isNotNull();
      assertThat(cell.leftCell()).isNull();
      assertThat(cell.topLeftCell()).isNull();
      assertThat(cell.bottomLeftCell()).isNull();
      assertThat(cell.rightCell()).isNotNull();
    }
  }

  @Test
  void outerCells_rightCellColumnHasNoRightCell() {
    for (Coordinate y : Coordinate.values()) {
      Cell cell = board.findCell(Coordinate.fromChar('h'), y);
      assertThat(cell).isNotNull();
      assertThat(cell.rightCell()).isNull();
      assertThat(cell.topRightCell()).isNull();
      assertThat(cell.bottomRightCell()).isNull();
      assertThat(cell.leftCell()).isNotNull();
    }
  }

  @Test
  void outerCells_topCellRowHasNoTopCell() {
    for (Coordinate x : Coordinate.values()) {
      Cell cell = board.findCell(x, Coordinate.EIGHT);
      assertThat(cell).isNotNull();
      assertThat(cell.topCell()).isNull();
      assertThat(cell.topLeftCell()).isNull();
      assertThat(cell.topRightCell()).isNull();
      assertThat(cell.bottomCell()).isNotNull();
    }
  }

  @Test
  void outerCells_bottomCellRowHasNoBottomCell() {
    for (Coordinate x : Coordinate.values()) {
      Cell cell = board.findCell(x, Coordinate.ONE);
      assertThat(cell).isNotNull();
      assertThat(cell.bottomCell()).isNull();
      assertThat(cell.bottomLeftCell()).isNull();
      assertThat(cell.bottomRightCell()).isNull();
      assertThat(cell.topCell()).isNotNull();
      assertThat(cell.hasTopLeftCell())
          .isEqualTo(cell.x() != Coordinate.ONE && cell.y() != Coordinate.EIGHT);
      assertThat(cell.hasTopRightCell())
          .isEqualTo(cell.x() != Coordinate.EIGHT && cell.y() != Coordinate.EIGHT);
      assertThat(cell.hasBottomLeftCell())
          .isEqualTo(cell.x() != Coordinate.ONE && cell.y() != Coordinate.ONE);
      assertThat(cell.hasBottomRightCell())
          .isEqualTo(cell.x() != Coordinate.EIGHT && cell.y() != Coordinate.ONE);
    }
  }

  @ParameterizedTest
  @EnumSource(CellDirection.class)
  void innerCells_hasNeighbourCellInEveryDirection(CellDirection direction) {
    for (Coordinate x :
        List.of(
            Coordinate.TWO,
            Coordinate.THREE,
            Coordinate.FOUR,
            Coordinate.FIVE,
            Coordinate.SIX,
            Coordinate.SEVEN)) {
      for (Coordinate y :
          List.of(
              Coordinate.TWO,
              Coordinate.THREE,
              Coordinate.FOUR,
              Coordinate.FIVE,
              Coordinate.SIX,
              Coordinate.SEVEN)) {
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
    assertThat(availableCells).hasSize(1);
  }

  @Test
  void testCheckMateBlackKing_e4() {
    String fenString = "8/4Q1R1/R7/5k2/3pP3/5K2/8/8 b - - 0 4";
    FenNotation.parseFEN(board, fenString);
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
  void testMoveFigureWithInteger() {
    board.addFiguresToBoard();
    Cell startCell = board.findCell("d2");
    Cell endCell = board.findCell("d4");
    board.moveFigure(startCell, endCell);
    assertThat(startCell.figure()).isNull();
    assertThat(endCell.figure().type()).isEqualTo(FigureType.PAWN);
  }

  @Test
  void testChangeTurn() {
    board.addFiguresToBoard();
    Cell startCell = board.findCell("d2");
    Cell endCell = board.findCell("d4");
    board.moveFigure(startCell, endCell);
    assertThat(board.turn()).isEqualTo(FigureColor.BLACK);
  }

  @Test
  void testCastlingKingAndQueen() {
    board = new Board(true);

    // only keep Kings and Rooks on the board
    String fenString = "r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 10";
    FenNotation.parseFEN(board, fenString);
    assertThat(board.canPerformKingSideCastling(FigureColor.WHITE)).isTrue();
    assertThat(board.canPerformQueenSideCastling(FigureColor.WHITE)).isTrue();
    assertThat(board.canPerformKingSideCastling(FigureColor.BLACK)).isTrue();
    assertThat(board.canPerformQueenSideCastling(FigureColor.BLACK)).isTrue();

    fenString = "r3k2r/8/8/8/8/8/8/R3K2R w - - 0 10";
    FenNotation.parseFEN(board, fenString);
    assertThat(board.canPerformKingSideCastling(FigureColor.WHITE)).isFalse();
    assertThat(board.canPerformQueenSideCastling(FigureColor.WHITE)).isFalse();
    assertThat(board.canPerformKingSideCastling(FigureColor.BLACK)).isFalse();
    assertThat(board.canPerformQueenSideCastling(FigureColor.BLACK)).isFalse();
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
    assertThat(board.findCell("a1").figure().type()).isEqualTo(FigureType.ROOK);
    assertThat(board.findCell("b1").figure().type()).isEqualTo(FigureType.KNIGHT);
    assertThat(board.findCell("c1").figure().type()).isEqualTo(FigureType.BISHOP);
    assertThat(board.findCell("d1").figure().type()).isEqualTo(FigureType.QUEEN);
    assertThat(board.findCell("e1").figure().type()).isEqualTo(FigureType.KING);
    assertThat(board.findCell("f1").figure().type()).isEqualTo(FigureType.BISHOP);
    assertThat(board.findCell("g1").figure().type()).isEqualTo(FigureType.KNIGHT);
    assertThat(board.findCell("h1").figure().type()).isEqualTo(FigureType.ROOK);
    assertThat(board.findCell("a8").figure().type()).isEqualTo(FigureType.ROOK);
    assertThat(board.findCell("b8").figure().type()).isEqualTo(FigureType.KNIGHT);
    assertThat(board.findCell("c8").figure().type()).isEqualTo(FigureType.BISHOP);
    assertThat(board.findCell("d8").figure().type()).isEqualTo(FigureType.QUEEN);
    assertThat(board.findCell("e8").figure().type()).isEqualTo(FigureType.KING);
    assertThat(board.findCell("f8").figure().type()).isEqualTo(FigureType.BISHOP);
    assertThat(board.findCell("g8").figure().type()).isEqualTo(FigureType.KNIGHT);
    assertThat(board.findCell("h8").figure().type()).isEqualTo(FigureType.ROOK);

    for (Coordinate x : Coordinate.values()) {
      assertThat(board.findCell(x, Coordinate.TWO).figure().type()).isEqualTo(FigureType.PAWN);
      assertThat(board.findCell(x, Coordinate.SEVEN).figure().type()).isEqualTo(FigureType.PAWN);
    }
  }

  @Test
  void testInitialColor() {
    board.addFiguresToBoard();
    for (Coordinate x : Coordinate.values()) {
      assertThat(board.findCell(x, Coordinate.ONE).figure().color()).isEqualTo(FigureColor.WHITE);
      assertThat(board.findCell(x, Coordinate.TWO).figure().color()).isEqualTo(FigureColor.WHITE);
      assertThat(board.findCell(x, Coordinate.SEVEN).figure().color()).isEqualTo(FigureColor.BLACK);
      assertThat(board.findCell(x, Coordinate.EIGHT).figure().color()).isEqualTo(FigureColor.BLACK);
    }
  }

  @Test
  void testEnPassant() {
    // given
    board = new Board(false);
    String fen = "rnbqkbnr/ppp1pppp/8/8/3p4/8/PPP1PPPP/RNBQKBNR w KQkq - 0 2";
    FenNotation.parseFEN(board, fen);

    // White Pawn two field moved
    board.moveFigure("e2", "e4");
    String availableEnPassant = "rnbqkbnr/ppp1pppp/8/8/3pP3/8/PPP2PPP/RNBQKBNR b KQkq e3 0 2";
    assertThat(FenNotation.generateFen(board)).isEqualTo(availableEnPassant);

    // Black Pawn catch white pawn using en passant
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

    assertThatThrownBy(() -> board.moveFigure("d8", "c8"))
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining(
            "This move is not allowed as your king would be in check! Move a figure so that your king is not in check (anymore).");
  }

  @Test
  void testNotCheckMateState() {
    board = new Board(false);
    String initialStatus = "rnb1kb1r/ppp1pppp/3q1np1/3p4/2P5/2N2N1B/PP1PPP1P/R1BQK2R b KQkq - 3 5";
    FenNotation.parseFEN(board, initialStatus);

    assertThat(board.isCheckmate(FigureColor.WHITE)).isFalse();
    board.moveFigure("b8", "c6");
    assertThat(board.isCheckmate(FigureColor.BLACK)).isFalse();
  }

  @Test
  void testNotExistKing() {
    board = new Board(false);
    String initialStatus = "rn1q1bnr/1pp1pppp/8/pB3b2/5P2/4p3/PPP3PP/RNBQ2NR b - - 2 10";

    assertThatThrownBy(() -> FenNotation.parseFEN(board, initialStatus))
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("Impossible state! There is no king on the field.");
  }

  @Test
  void testInvalidMoveFigure() {
    board = new Board(false);
    String initialStatus = "rnbqkbnr/ppp1pppp/8/3p4/2P5/8/PP1PPPPP/RNBQKBNR w KQkq d6 0 3";
    FenNotation.parseFEN(board, initialStatus);

    assertThat(board.isCheck(FigureColor.WHITE)).isFalse();

    assertThatThrownBy(() -> board.moveFigure("e4", "d5"))
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("On the starting cell is no figure");

    assertThatThrownBy(() -> board.moveFigure("d5", "c4"))
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("It is not your turn! Try to move a figure of color");

    assertThatThrownBy(() -> board.moveFigure("c4", "c6"))
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("The figure can't move to that cell");
  }

  @Test
  void testInValidCastling() {
    board = new Board(false);
    String initialState = "rnb1kb1r/ppp1pppp/3q1np1/3p4/2P5/2N2N1B/PP1PPP1P/R1BQK2R b KQkq - 3 5";
    FenNotation.parseFEN(board, initialState);

    Cell f1 = board.findCell("f1");
    Cell g1 = board.findCell("g1");
    Cell e1 = board.findCell("e1");

    assertThatThrownBy(() -> board.handleCastling(f1, g1, MoveType.KING_CASTLING))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessageContaining("A castling move can only be done by a king.");
    assertThatThrownBy(() -> board.handleCastling(e1, g1, MoveType.EN_PASSANT))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessageContaining("This is not a valid castling move.");
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

    board.moveFigure("b2", "c3"); // 7. bxc3
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
    assertThat(((King) board.findCell("e1").figure()).hasMoved()).isFalse();
    assertThat(((King) board.findCell("e8").figure()).hasMoved()).isFalse();

    board.moveFigure("e1", "g1"); // 15. O-O White King Castling
    fen = FenNotation.generateFen(board);
    assertThat(fen).isEqualTo("r1bqk2r/p5p1/1p3npp/2pPp3/2P1P3/2PBB3/P5PP/R2Q1RK1 b kq - 1 14");
    assertThat(board.canPerformKingSideCastling(FigureColor.WHITE)).isFalse();
    assertThat(board.canPerformKingSideCastling(FigureColor.BLACK)).isTrue();
    assertThat(((Rook) board.findCell("f1").figure()).hasMoved()).isTrue();
    assertThat(((King) board.findCell("g1").figure()).hasMoved()).isTrue();

    board.moveFigure("e8", "g8"); // 15. O-O Black King Castling
    fen = FenNotation.generateFen(board);
    assertThat(fen).isEqualTo("r1bq1rk1/p5p1/1p3npp/2pPp3/2P1P3/2PBB3/P5PP/R2Q1RK1 w - - 2 15");
    assertThat(board.canPerformKingSideCastling(FigureColor.WHITE)).isFalse();
    assertThat(((Rook) board.findCell("f8").figure()).hasMoved()).isTrue();
    assertThat(((King) board.findCell("g8").figure()).hasMoved()).isTrue();
  }
}
