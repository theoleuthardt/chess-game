package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;
import hwr.oop.chess.cli.InvalidUserInputException;
import hwr.oop.chess.persistence.FenNotation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class PawnTest {
  private Board board;

  @BeforeEach
  public void setUp() {
    // Initialize the board
    board = new Board(true);
  }

  @Test
  void pawn_hasTypePawn() {
    Pawn pawn = new Pawn(FigureColor.WHITE);
    assertThat(pawn.type()).isEqualTo(FigureType.PAWN);
  }

  @Test
  void createPawn() {
    Pawn pawn = new Pawn(FigureColor.BLACK);
    assertThat(pawn.color()).isEqualTo(FigureColor.BLACK);
    assertThat(pawn.type()).isEqualTo(FigureType.PAWN);
  }

  @ParameterizedTest
  @ValueSource(ints = {3, 4})
  void moveWhitePawn_isAllowed(int args) {
    Cell from = board.findCell('d', 2);
    Cell to = board.findCell('d', args);

    Figure pawn = from.figure();
    assertThat(pawn.type()).isEqualTo(FigureType.PAWN);
    assertThat(pawn.canMoveTo(from, to)).isTrue();
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 5})
  void moveWhitePawn_isNotAllowed(int args) {
    Cell from = board.findCell('d', 2);
    Cell to = board.findCell('d', args);

    Figure pawn = from.figure();
    assertThat(pawn.type()).isEqualTo(FigureType.PAWN);
    assertThat(pawn.canMoveTo(from, to)).isFalse();
  }

  @Test
  void moveWhitePawn_twoFieldsOnlyOnStart() {
    Cell from = board.findCell('d', 2);
    Cell to = board.findCell('d', 3);
    Cell then = board.findCell('d', 5);

    Figure pawn = from.figure();
    assertThat(pawn.type()).isEqualTo(FigureType.PAWN);
    assertThat(pawn.canMoveTo(from, to)).isTrue();
    assertThat(pawn.canMoveTo(to, then)).isFalse();
  }

  @ParameterizedTest
  @ValueSource(ints = {6})
  void moveBlackPawn_isAllowed(int args) {
    Cell from = board.findCell('c', 7);
    Cell to = board.findCell('c', args);

    Figure pawn = from.figure();
    assertThat(pawn.type()).isEqualTo(FigureType.PAWN);
    assertThat(pawn.canMoveTo(from, to)).isTrue();
  }

  @ParameterizedTest
  @ValueSource(ints = {8, 7, 4})
  void moveBlackPawn_isNotAllowed(int args) {
    Cell from = board.findCell('c', 7);
    Cell to = board.findCell('c', args);

    Figure pawn = from.figure();
    assertThat(pawn.type()).isEqualTo(FigureType.PAWN);
    assertThat(pawn.canMoveTo(from, to)).isFalse();
  }

  @Test
  void moveBlackPawn_twoFieldsOnlyOnStart() {
    Cell from = board.findCell('c', 7);
    Cell to = board.findCell('c', 6);
    Cell then = board.findCell('c', 4);

    Figure pawn = from.figure();
    assertThat(pawn.type()).isEqualTo(FigureType.PAWN);
    assertThat(pawn.canMoveTo(from, to)).isTrue();
    assertThat(pawn.canMoveTo(to, then)).isFalse();
  }

  @Test
  void movePawn_noCellAvailableOnLastRow() {
    board = new Board(false);
    Figure blackPawn = new Pawn(FigureColor.BLACK);
    Cell blackPawnCell = board.findCell("c1");
    blackPawnCell.setFigure(blackPawn);
    assertThat(board.availableCellsWithoutCheckMoves(blackPawnCell)).isEmpty();

    Figure whitePawn = new Pawn(FigureColor.WHITE);
    Cell whitePawnCell = board.findCell("c8");
    whitePawnCell.setFigure(whitePawn);
    assertThat(board.availableCellsWithoutCheckMoves(whitePawnCell)).isEmpty();
  }

  void pawnDiagonalTest(FigureColor pawnColor, FigureColor diagonalColor, boolean expectedResult) {
    board = new Board(false);

    CellDirection forwards =
        pawnColor == FigureColor.WHITE ? CellDirection.TOP : CellDirection.BOTTOM;
    Cell from = board.findCell("d4");
    Cell forwardsLeft = from.cellInDirection(forwards).leftCell();
    Cell forwardsRight = from.cellInDirection(forwards).rightCell();

    Figure pawn = new Pawn(pawnColor);
    Figure diagonalRook = diagonalColor == null ? null : new Rook(diagonalColor);
    Figure diagonalQueen = diagonalColor == null ? null : new Queen(diagonalColor);

    from.setFigure(pawn);
    forwardsLeft.setFigure(diagonalRook);
    forwardsRight.setFigure(diagonalQueen);

    assertThat(pawn.canMoveTo(from, forwardsLeft)).isEqualTo(expectedResult);
    assertThat(pawn.canMoveTo(from, forwardsRight)).isEqualTo(expectedResult);
  }

  @Test
  void movePawn_diagonalWithOpponentThereIsAllowed() {
    pawnDiagonalTest(FigureColor.WHITE, FigureColor.BLACK, true);
    pawnDiagonalTest(FigureColor.BLACK, FigureColor.WHITE, true);
  }

  @Test
  void movePawn_diagonalWithSameColorThereIsNotAllowed() {
    pawnDiagonalTest(FigureColor.WHITE, FigureColor.WHITE, false);
    pawnDiagonalTest(FigureColor.BLACK, FigureColor.BLACK, false);
  }

  @Test
  void moveWhitePawn_diagonalIsNotAllowedIfFieldIsEmpty() {
    pawnDiagonalTest(FigureColor.WHITE, null, false);
    pawnDiagonalTest(FigureColor.BLACK, null, false);
  }

  @Test
  void isAbleToPromote_WhitePawn() {
    Pawn pawn = new Pawn(FigureColor.WHITE);
    board = new Board(true);
    Cell cell = board.findCell('a', 8);
    cell.setFigure(pawn);

    boolean result = pawn.isAbleToPromote(cell);

    assertThat(result).isTrue();
    assertThat(pawn.getPromotionTypes())
        .containsExactlyInAnyOrder(
            FigureType.QUEEN, FigureType.ROOK, FigureType.BISHOP, FigureType.KNIGHT);
  }

  @Test
  void isAbleToPromote_BlackPawn() {
    Pawn pawn = new Pawn(FigureColor.BLACK);
    board = new Board(true);
    Cell cell = board.findCell('a', 1);
    cell.setFigure(pawn);

    boolean result = pawn.isAbleToPromote(cell);

    assertThat(result).isTrue();
    assertThat(pawn.getPromotionTypes())
        .containsExactlyInAnyOrder(
            FigureType.QUEEN, FigureType.ROOK, FigureType.BISHOP, FigureType.KNIGHT);
  }

  @Test
  void isAbleToPromote_CellAvailableInForwardDirection() {
    Pawn pawn = new Pawn(FigureColor.WHITE);
    board = new Board(true);
    Cell currentCell = board.findCell('a', 7);

    assertThat(pawn.isAbleToPromote(currentCell)).isFalse();
  }

  @Test
  void promotePawn_InvalidPromotionType() {
    Pawn pawn = new Pawn(FigureColor.WHITE);
    board = new Board(true);
    Cell currentCell = board.findCell('a', 7);
    assertThatException()
        .isThrownBy(() -> pawn.promotePawn(currentCell, FigureType.KING))
        .isInstanceOf(InvalidUserInputException.class);
    assertThatException()
        .isThrownBy(() -> pawn.promotePawn(currentCell, FigureType.PAWN))
        .isInstanceOf(InvalidUserInputException.class);
  }

  @Test
  void promotePawn_PawnNotEligibleForPromotion() {
    Pawn pawn = new Pawn(FigureColor.WHITE);
    board = new Board(true);
    Cell currentCell = board.findCell('a', 1);

    assertThatException()
        .isThrownBy(() -> pawn.promotePawn(currentCell, FigureType.QUEEN))
        .isInstanceOf(InvalidUserInputException.class);
  }

  @Test
  void pawn_testHalfMove() {
    board = new Board(false);
    String initialStatus = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 3 1";
    FenNotation.parseFEN(board, initialStatus);

    board.moveFigure("b2", "b3");
    String afterPawnMove = "rnbqkbnr/pppppppp/8/8/8/1P6/P1PPPPPP/RNBQKBNR b KQkq - 0 1";

    String changedStatus = FenNotation.generateFen(board);
    assertThat(changedStatus).isEqualTo(afterPawnMove);
  }

  // TODO: Every assertion should be at the end of the test function.
  @Test
  void canPerformEnPassant_isAllowed() {
    board = new Board(false);
    FenNotation.parseFEN(board, "4k3/8/8/4pP2/8/8/8/4K3 w - e6 0 1");
    Cell from = board.findCell('f', 5);
    Pawn pawn = (Pawn) from.figure();
    Cell to = board.findCell('e', 6);

    assertThat(pawn.canPerformEnPassant(from, to)).isTrue();
    Cell f6 = board.findCell('f', 6);
    f6.setIsEnPassant(true);
    assertThat(pawn.canPerformEnPassant(from, f6)).isFalse();
    Cell f4 = board.findCell('f', 4);
    f4.setIsEnPassant(true);
    assertThat(pawn.canPerformEnPassant(from, f4)).isFalse();
    Cell g6 = board.findCell('g', 6);
    g6.setIsEnPassant(true);
    assertThat(pawn.canPerformEnPassant(from, g6)).isFalse();
  }

  @Test
  void canPerformEnPassant_boardState() {
    board = new Board(false);
    FenNotation.parseFEN(board, "4k3/8/8/4pP2/8/8/8/4K3 w - e6 0 1");
    Cell from = board.findCell('f', 5);
    Pawn pawn = (Pawn) from.figure();
    Cell opponent = board.findCell('e', 5);
    Cell to = board.findCell('e', 6);

    board.moveFigure(from, to);
    assertThat(from.isFree()).isTrue();
    assertThat(opponent.isFree()).isTrue();
    assertThat(to.isOccupiedBy(FigureColor.WHITE, FigureType.PAWN)).isTrue();
  }

  // TODO: Every assertion should be at the end of the test function.
  // If there is a function like "board.moveFigure(...)" after an Assertion,
  // try to change the order or split it into two seperate test.
  @Test
  void canPerformEnPassant_isNotAllowed() {
    board = new Board(false);
    FenNotation.parseFEN(board, "4k3/8/8/4pP2/8/8/8/4K3 w - - 0 1");
    Cell from = board.findCell('f', 5);
    Pawn pawn = (Pawn) from.figure();
    Cell opponent = board.findCell('e', 5);
    Cell to = board.findCell('e', 6);

    assertThat(pawn.canPerformEnPassant(from, to)).isFalse();
    Cell f6 = board.findCell('f', 6);
    f6.setIsEnPassant(true);
    assertThat(pawn.canPerformEnPassant(from, f6)).isFalse();

    Cell f4 = board.findCell('f', 4);
    f4.setIsEnPassant(true);
    assertThat(pawn.canPerformEnPassant(from, f4)).isFalse();

    Cell g6 = board.findCell('g', 6);
    g6.setIsEnPassant(true);
    assertThat(pawn.canPerformEnPassant(from, g6)).isFalse();
    assertThat(from.isOccupiedBy(FigureColor.WHITE, FigureType.PAWN)).isTrue();
    assertThat(opponent.isOccupiedBy(FigureColor.BLACK, FigureType.PAWN)).isTrue();
    assertThat(to.isFree()).isTrue();

    assertThatThrownBy(() -> board.moveFigure(from, to))
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("The figure can't move to that cell");
  }
}
