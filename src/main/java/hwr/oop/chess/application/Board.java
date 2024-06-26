package hwr.oop.chess.application;

import static hwr.oop.chess.application.figures.FigureType.*;

import hwr.oop.chess.application.figures.*;
import hwr.oop.chess.cli.InvalidUserInputException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Board {
  private Cell firstCell;
  private int halfMove = 0;
  private int fullMove = 0;
  private FigureColor currentTurnColor = FigureColor.WHITE;

  public Board(boolean setFigures) {
    initializeBoard();
    if (setFigures) {
      addFiguresToBoard();
    }
  }

  private void initializeBoard() {
    Cell bottomCellRowStart = null;

    // create the board row by row
    // starts at the bottom left
    // if a row is complete the next row will also be connected from the left
    for (Coordinate y : Coordinate.values()) {
      Cell leftCell = null;
      Cell bottomCell =
          bottomCellRowStart =
              (bottomCellRowStart == null) ? firstCell : bottomCellRowStart.topCell();

      for (Coordinate x : Coordinate.values()) {
        Cell currentCell = new Cell(x, y);

        if (y == Coordinate.ONE && x == Coordinate.ONE) {
          firstCell = currentCell;
          bottomCell = bottomCellRowStart = null;
        }

        connectCells(currentCell, leftCell);
        leftCell = currentCell;

        if (bottomCell != null) {
          connectCells(currentCell, bottomCell);
          connectCells(currentCell, bottomCell.leftCell());
          connectCells(currentCell, bottomCell.rightCell());
          bottomCell = bottomCell.rightCell();
        }
      }
    }
  }

  public void initializeWith(FigureColor turn, int halfMove, int fullMove) {
    this.currentTurnColor = turn;
    this.halfMove = halfMove;
    this.fullMove = fullMove;
  }

  // Method to connect each cell
  public void connectCells(Cell currentCell, Cell anotherCell) {
    if (anotherCell != null && currentCell != null) {
      currentCell.connectTo(anotherCell);
      anotherCell.connectTo(currentCell);
    }
  }

  public List<Cell> allCells() {
    List<Cell> cells = new ArrayList<>();
    Cell cell = firstCell.allCellsInDirection(CellDirection.TOP).getLast();
    Cell rowStart = cell;

    while (cell != null) {
      cells.add(cell);
      if (cell.hasRightCell()) {
        cell = cell.rightCell();
      } else {
        cell = rowStart = rowStart.bottomCell();
      }
    }
    return cells;
  }

  public Cell findCell(String cell) {
    if (cell.length() != 2) {
      throw new InvalidUserInputException("The XY-Coordinates must be two characters long.");
    }
    cell = cell.toLowerCase();
    Coordinate x = Coordinate.fromChar(cell.charAt(0));
    Coordinate y = Coordinate.fromInt(cell.charAt(1) - 48);
    return findCell(x, y);
  }

  public Cell findCell(char x, int y) {
    return findCell(Coordinate.fromChar(x), Coordinate.fromInt(y));
  }

  public Cell findCell(Coordinate x, Coordinate y) {
    return allCells().stream()
        .filter(cell -> cell.x() == x && cell.y() == y)
        .findFirst()
        .orElse(null);
  }

  public Cell findKingCell(FigureColor playerColor) {
    for (Cell cell : allCells()) {
      if (cell.isOccupiedBy(playerColor, FigureType.KING)) {
        return cell;
      }
    }
    throw new InvalidUserInputException("Impossible state! There is no king on the field.");
  }

  public List<Cell> cellsWhere(Predicate<Cell> filter) {
    return allCells().stream().filter(filter).toList();
  }

  public int countCellsWhere(Predicate<Cell> filter) {
    return cellsWhere(filter).size();
  }

  public boolean cellExistsWhere(Predicate<Cell> filter) {
    return allCells().stream().anyMatch(filter);
  }

  public void addFiguresToBoard() {
    for (Cell cell : allCells()) {
      FigureColor figureColor = cell.y().toInt() <= 2 ? FigureColor.WHITE : FigureColor.BLACK;

      if (cell.y() == Coordinate.ONE || cell.y() == Coordinate.EIGHT) {
        switch (cell.x()) {
          case ONE, EIGHT -> cell.setFigure(new Rook(figureColor));
          case TWO, SEVEN -> cell.setFigure(new Knight(figureColor));
          case THREE, SIX -> cell.setFigure(new Bishop(figureColor));
          case FOUR -> cell.setFigure(new Queen(figureColor));
          case FIVE -> cell.setFigure(new King(figureColor));
          default -> {
            // This column does not exist
          }
        }
      }

      if (cell.y() == Coordinate.TWO || cell.y() == Coordinate.SEVEN) {
        cell.setFigure(new Pawn(figureColor));
      }
    }
  }

  public void moveFigure(String start, String end) {
    moveFigure(findCell(start), findCell(end));
  }

  public void moveFigure(Cell startCell, Cell endCell) {
    MoveType moveType = moveType(startCell, endCell);
    if (moveType != MoveType.EN_PASSANT) {
      // reset EN_PASSANT because the next move is not an en passant move
      allCells().forEach(cell -> cell.setIsEnPassant(false));
    }
    switch (moveType) {
      case EN_PASSANT -> handleEnPassant(startCell, endCell);
      case KING_CASTLING, QUEEN_CASTLING -> handleCastling(startCell, endCell, moveType);
      default -> handleNormalMove(startCell, endCell);
    }

    changeTurnAndCountMoves();
  }

  public List<Cell> availableCellsWithoutCheckMoves(Cell startCell) {
    List<Cell> availableCells = startCell.figure().availableCells(startCell);
    availableCells.removeIf(endCell -> wouldBeCheckAfterMove(startCell, endCell));
    return availableCells;
  }

  public boolean wouldBeCheckAfterMove(Cell startCell, Cell endCell) {
    Figure movingFigure = startCell.figure();
    Figure capturedFigure = endCell.figure();
    startCell.setFigure(null);
    endCell.setFigure(movingFigure);
    boolean wouldBeCheck = isCheck(movingFigure.color());
    startCell.setFigure(movingFigure);
    endCell.setFigure(capturedFigure);
    return wouldBeCheck;
  }

  public boolean isCheck(FigureColor playerColor) {
    Cell kingCell = findKingCell(playerColor);
    for (Cell opponentCell : cellsWithColor(playerColor.ofOpponent())) {
      if (opponentCell.figure().canMoveTo(opponentCell, kingCell)) {
        return true;
      }
    }
    return false;
  }

  public boolean isCheckmate(FigureColor playerColor) {
    return isCheck(playerColor) && playerCannotMoveAnyFigure(playerColor);
  }

  public boolean isStalemate(FigureColor playerColor) {
    return !isCheck(playerColor) && playerCannotMoveAnyFigure(playerColor);
  }

  public boolean playerCannotMoveAnyFigure(FigureColor playerColor) {
    for (Cell startCell : cellsWithColor(playerColor)) {
      if (!availableCellsWithoutCheckMoves(startCell).isEmpty()) {
        return false;
      }
    }
    return true;
  }

  public List<Cell> cellsWithColor(FigureColor myColor) {
    List<Cell> cells = allCells();
    cells.removeIf(cell -> !cell.isOccupiedBy(myColor));
    return cells;
  }

  private void changeTurnAndCountMoves() {
    if (currentTurnColor == FigureColor.BLACK) {
      this.fullMove++;
    }
    this.halfMove++;
    this.currentTurnColor = currentTurnColor.ofOpponent();
  }

  public MoveType moveType(Cell startCell, Cell endCell) {
    if (startCell.isFree()) {
      throw new InvalidUserInputException("On the starting cell is no figure");
    }

    Figure figure = startCell.figure();
    if (figure.color() != currentTurnColor) {
      throw new InvalidUserInputException(
          "It is not your turn! Try to move a figure of color " + currentTurnColor.name() + ".");
    }

    if (!figure.canMoveTo(startCell, endCell)) {
      throw new InvalidUserInputException("The figure can't move to that cell");
    }

    if (wouldBeCheckAfterMove(startCell, endCell)) {
      throw new InvalidUserInputException(
          "This move is not allowed as your king would be in check! Move a figure so that your king is not in check (anymore).");
    }

    if (figure.type() == FigureType.KING) {
      King king = (King) figure;
      if (king.canPerformKingSideCastling(startCell)
          && king.kingSideCastlingCell(startCell).isEqualTo(endCell)) {
        return MoveType.KING_CASTLING;
      }
      if (king.canPerformQueenSideCastling(startCell)
          && king.queenSideCastlingCell(startCell).isEqualTo(endCell)) {
        return MoveType.QUEEN_CASTLING;
      }
    }

    if (figure.type() == FigureType.PAWN
        && ((Pawn) figure).canPerformEnPassant(startCell, endCell)) {
      return MoveType.EN_PASSANT;
    }

    return MoveType.NORMAL;
  }

  public EndType endType(FigureColor color) {
    if (isCheckmate(color)) {
      return EndType.CHECKMATE;
    }
    if (isStalemate(color)) {
      return EndType.STALEMATE;
    }
    if (isDeadPosition()) {
      return EndType.DEAD_POSITION;
    }
    if (isFiftyMoveEnd()) {
      return EndType.FIFTY_MOVE_RULE;
    }
    return EndType.NOT_END;
  }

  public boolean isFiftyMoveEnd() {
    return halfMove >= 100;
  }

  private void handleNormalMove(Cell startCell, Cell endCell) {
    Figure figure = startCell.figure();
    if (endCell.isOccupied() || figure.type() == FigureType.PAWN) {
      this.halfMove = -1;
    }
    startCell.setFigure(null);
    endCell.setFigure(figure);

    if (figure.type() == FigureType.PAWN
        && Math.abs(startCell.y().toInt() - endCell.y().toInt()) == 2) {
      startCell.cellInDirection(((Pawn) figure).forwards()).setIsEnPassant(true);
    }
  }

  private void handleEnPassant(Cell startCell, Cell endCell) {
    Figure figure = startCell.figure();
    this.halfMove = -1;
    startCell.setFigure(null);
    endCell.setFigure(figure);
    endCell.setIsEnPassant(false);

    CellDirection backwards =
        ((Pawn) figure).forwards() == CellDirection.TOP ? CellDirection.BOTTOM : CellDirection.TOP;
    Cell cellWithOpponentPawn = endCell.cellInDirection(backwards);
    cellWithOpponentPawn.setFigure(null);
  }

  public void handleCastling(Cell startKingCell, Cell endKingCell, MoveType type) {
    if (!startKingCell.isOccupiedBy(FigureType.KING)) {
      throw new UnsupportedOperationException("A castling move can only be done by a king.");
    }

    Cell startRookCell;
    Cell endRookCell;
    switch (type) {
      case KING_CASTLING -> {
        startRookCell = findCell(Coordinate.fromChar('h'), startKingCell.y());
        endRookCell = endKingCell.leftCell();
      }
      case QUEEN_CASTLING -> {
        startRookCell = findCell(Coordinate.fromChar('a'), startKingCell.y());
        endRookCell = endKingCell.rightCell();
      }
      default -> throw new UnsupportedOperationException("This is not a valid castling move.");
    }

    King king = (King) startKingCell.figure();
    king.figureMoved();
    startKingCell.setFigure(null);
    endKingCell.setFigure(king);

    Rook rook = (Rook) startRookCell.figure();
    rook.figureMoved();
    startRookCell.setFigure(null);
    endRookCell.setFigure(rook);
  }

  public FigureColor turn() {
    return this.currentTurnColor;
  }

  public boolean canPerformQueenSideCastling(FigureColor color) {
    Cell kingCell = findKingCell(color);
    return ((King) kingCell.figure()).canPerformQueenSideCastling(kingCell);
  }

  public boolean canPerformKingSideCastling(FigureColor color) {
    Cell kingCell = findKingCell(color);
    return ((King) kingCell.figure()).canPerformKingSideCastling(kingCell);
  }

  public int fullMove() {
    return this.fullMove;
  }

  public int halfMove() {
    return this.halfMove;
  }

  public boolean isPawnPromotionPossible() {
    Predicate<Cell> pawnCanPromote = cell -> ((Pawn) cell.figure()).isAbleToPromote(cell);
    return cellExistsWhere(isOccupiedBy(PAWN).and(pawnCanPromote));
  }

  private Predicate<Cell> isOccupiedBy(FigureType type) {
    return cell -> cell.isOccupiedBy(type);
  }

  public boolean isDeadPosition() {
    // while any major figure is on the board checkmate is still possible.
    if (cellExistsWhere(isOccupiedBy(ROOK).or(isOccupiedBy(QUEEN).or(isOccupiedBy(PAWN))))) {
      return false;
    }

    int countOfMinorFigures = countCellsWhere(isOccupiedBy(BISHOP).or(isOccupiedBy(KNIGHT)));
    // if there are more than 2 minor figures on the board checkmate is still possible.
    if (countOfMinorFigures > 2) return false;
    // if there are less than 2 minor figures on the board it is a dead position.
    if (countOfMinorFigures < 2) return true;

    // there is one bishop of each color
    // both are on *cells of the same color* -> this is a dead position.
    List<Cell> bishopCells = cellsWhere(isOccupiedBy(BISHOP));
    List<FigureColor> bishopColors =
        bishopCells.stream().map(cell -> cell.figure().color()).toList();
    if (bishopColors.contains(FigureColor.WHITE) && bishopColors.contains(FigureColor.BLACK)) {
      return bishopCells.getFirst().isCellBackgroundColorWhite()
          == bishopCells.getLast().isCellBackgroundColorWhite();
    }
    return false;
  }

  public void promotePawn(Cell startCell, FigureType promoteToType) {
    Pawn pawn = (Pawn) startCell.figure();
    pawn.promotePawn(startCell, promoteToType);
  }
}
