package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;
import hwr.oop.chess.cli.InvalidUserInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PawnTest {
  private Board board;

  @BeforeEach
  public void setUp() {
    // Initialize the board
    board = new Board(true);
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
    Board board = new Board(false);
    Figure blackPawn = new Pawn(FigureColor.BLACK);
    assertThat(blackPawn.getAvailableCells(board.findCell(3, 1))).isEmpty();

    Figure whitePawn = new Pawn(FigureColor.WHITE);
    assertThat(whitePawn.getAvailableCells(board.findCell(3, 8))).isEmpty();
  }

  void pawnDiagonalTest(FigureColor pawnColor, FigureColor diagonalColor, boolean expectedResult) {
    Board board = new Board(false);

    CellDirection forwards =
        pawnColor == FigureColor.WHITE ? CellDirection.TOP : CellDirection.BOTTOM;
    Cell from = board.findCell(4, 4);
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
    Board board = new Board(true);
    Cell cell = board.findCell('a', 8);
    cell.setFigure(pawn);

    boolean result = pawn.isAbleToPromote(cell);

    assertThat(result).isTrue();
  }

  @Test
  void isAbleToPromote_BlackPawn() {
    Pawn pawn = new Pawn(FigureColor.BLACK);
    Board board = new Board(true);
    Cell cell = board.findCell('a', 1);
    cell.setFigure(pawn);

    boolean result = pawn.isAbleToPromote(cell);

    assertThat(result).isTrue();
  }

  @Test
  void promotePawn_InvalidPromotionType() {
    Pawn pawn = new Pawn(FigureColor.WHITE);
    Board board = new Board(true);
    Cell currentCell = board.findCell('a', 7);

    assertThrows(InvalidUserInputException.class, () -> pawn.promotePawn(currentCell, FigureType.KING));
  }

  @Test
  void promotePawn_PawnNotEligibleForPromotion() {
    Pawn pawn = new Pawn(FigureColor.WHITE);
    Board board = new Board(true);
    Cell currentCell = board.findCell('a', 6);

    assertThrows(InvalidUserInputException.class, () -> pawn.promotePawn(currentCell, FigureType.QUEEN));
  }
}
