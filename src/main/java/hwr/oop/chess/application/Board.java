package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.*;
import hwr.oop.chess.cli.InvalidUserInputException;

import java.util.ArrayList;
import java.util.List;

public class Board {
  private Cell firstCell;
  private String enPassant = "-";
  private int halfMove = 0;
  private int fullMove = 0;
  private FigureColor turn = FigureColor.WHITE;

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
    for (int y = 1; y <= 8; y++) {
      Cell leftCell = null;
      Cell bottomCell =
          bottomCellRowStart =
              (bottomCellRowStart == null) ? firstCell : bottomCellRowStart.topCell();

      for (int x = 1; x <= 8; x++) {
        Cell currentCell = new Cell(x, y);

        if (y == 1 && x == 1) {
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

  public void initializeWith(FigureColor turn, String enPassant, int halfMove, int fullMove) {
    this.turn = turn;
    this.enPassant = enPassant;
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

  public Cell firstCell() {
    return firstCell;
  }

  public List<Cell> allCells() {
    ArrayList<Cell> cells = new ArrayList<>();
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
    return findCell(cell.charAt(0) - 96, cell.charAt(1) - 48);
  }

  public Cell findCell(char x, int y) {
    return findCell(x - 96, y);
  }

  public Cell findCell(int x, int y) {
    for (Cell cell : allCells()) {
      if (cell.x() == x && cell.y() == y) {
        return cell;
      }
    }
    return null;
  }

  public Cell findKing(FigureColor playerColor) {
    for (Cell cell : allCells()) {
      if (cell.isOccupiedBy(playerColor, FigureType.KING)) {
        return cell;
      }
    }
    throw new InvalidUserInputException("Impossible state! There is no king on the field.");
  }

  public void addFiguresToBoard() {
    for (Cell cell : allCells()) {
      FigureColor figureColor = cell.y() <= 2 ? FigureColor.WHITE : FigureColor.BLACK;

      if (cell.y() == 1 || cell.y() == 8) {
        switch (cell.x()) {
          case 1, 8 -> cell.setFigure(new Rook(figureColor));
          case 2, 7 -> cell.setFigure(new Knight(figureColor));
          case 3, 6 -> cell.setFigure(new Bishop(figureColor));
          case 4 -> cell.setFigure(new Queen(figureColor));
          case 5 -> cell.setFigure(new King(figureColor));
          default -> throw new IllegalStateException("Unexpected value: " + cell.x());
        }
      }

      if (cell.y() == 2 || cell.y() == 7) {
        cell.setFigure(new Pawn(figureColor));
      }
    }
  }

  public void addFiguresToBoard(String figurePositions) {
    if (figurePositions == null) {
      addFiguresToBoard();
      return;
    }
    if (figurePositions.length() != 64) {
      throw new IllegalArgumentException(
          "The figurePositions should be a 64 character long string, but it is "
              + figurePositions.length()
              + " characters long.");
    }
    int figureIndex = 0;
    for (Cell cell : allCells()) {
      char figureType = figurePositions.charAt(figureIndex);
      if (figureType == ' ') {
        cell.setFigure(null);
      } else {
        Figure figure = Figure.fromChar(figureType);
        cell.setFigure(figure);
      }
      figureIndex++;
    }
  }

  public String figuresOnBoard() {
    StringBuilder figurePositions = new StringBuilder();
    for (Cell cell : allCells()) {
      if (cell.figure() == null) {
        figurePositions.append(' ');
      } else {
        figurePositions.append(cell.figure().symbol());
      }
    }
    return figurePositions.toString();
  }

  // Method to move a piece on the board
  public void moveFigure(Cell start, Cell end) {
    moveFigure(start.x(), start.y(), end.x(), end.y());
  }

  public void moveFigure(String start, String end) {
    moveFigure(findCell(start), findCell(end));
  }

  public void moveFigure(char startX, int startY, char endX, int endY) {
    moveFigure(startX - 96, startY, endX - 96, endY);
  }

  public void moveFigure(int startX, int startY, int endX, int endY) {

    if (firstCell.isInvalidCoordinate(startX, startY)
        || firstCell.isInvalidCoordinate(endX, endY)) {
      throw new InvalidUserInputException(
          "Invalid coordinates. Coordinates must be between 1 and 8.");
    }
    // Get the piece at the start position
    Cell startCell = findCell(startX, startY);
    Cell endCell = findCell(endX, endY);

    if (startCell.isFree()) {
      throw new InvalidUserInputException("On the starting cell is no figure");
    }

    Figure figure = startCell.figure();
    if (isCheckmate(figure.color())) {
      throw new InvalidUserInputException("The game is over as you are in Checkmate!");
    }

    if (figure.color() != turn) {
      throw new InvalidUserInputException(
          "It is not your turn! Try to move a figure of color " + turn.name() + ".");
    }

    if (!figure.canMoveTo(startCell, endCell)) {
      throw new InvalidUserInputException("The figure can't move to that cell");
    }

    if (wouldBeCheckAfterMove(startCell, endCell)) {
      throw new InvalidUserInputException(
          "This move is not allowed as your king would be in check! Move a figure so that your king is not in check (anymore).");
    }

    MoveType moveType = moveType(figure, startCell, endCell);
    boolean resetHalfMove = false;

    switch (moveType) {
      case NORMAL -> {
        resetHalfMove = endCell.isOccupied() || figure.type() == FigureType.PAWN;
        startCell.setFigure(null);
        endCell.setFigure(figure);
      }

      case KING_CASTLING, QUEEN_CASTLING -> handleCastling(startCell, endCell, moveType);

      default ->
          throw new UnsupportedOperationException("This type of move is not implemented yet.");
    }

    if (resetHalfMove) {
      this.halfMove = -1;
    }
    this.halfMove++;
    if (turn == FigureColor.BLACK) {
      this.fullMove++;
    }
    this.changeTurn();
  }

  public List<Cell> availableCellsWithoutCheckMoves(Cell startCell) {
    Figure figure = startCell.figure();
    List<Cell> availableCells = figure.availableCells(startCell);
    availableCells.removeIf(cell -> wouldBeCheckAfterMove(startCell, cell));
    return availableCells;
  }

  public boolean wouldBeCheckAfterMove(Cell startCell, Cell endCell) {
    Figure figure = startCell.figure();
    Figure figureOnEndCell = endCell.figure();
    startCell.setFigure(null);
    endCell.setFigure(figure);
    boolean isCheck = isCheck(figure.color());
    startCell.setFigure(figure);
    endCell.setFigure(figureOnEndCell);
    return isCheck;
  }

  public boolean isCheck(FigureColor playerColor) {
    Cell kingCell = findKing(playerColor);
    List<Cell> opponentCells =
        cellsWithColor(playerColor == FigureColor.WHITE ? FigureColor.BLACK : FigureColor.WHITE);
    for (Cell cell : opponentCells) {
      if (cell.figure().canMoveTo(cell, kingCell)) {
        return true;
      }
    }
    return false;
  }

  public boolean isCheckmate(FigureColor playerColor) {
    if (!isCheck(playerColor)) {
      return false;
    }

    for (Cell startCell : cellsWithColor(playerColor)) {
      List<Cell> availableCells = availableCellsWithoutCheckMoves(startCell);
      if (!availableCells.isEmpty()) {
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

  private void changeTurn() {
    this.turn = turn.opposite();
  }

  private MoveType moveType(Figure figure, Cell startCell, Cell endCell) {
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

    return MoveType.NORMAL;
  }

  public void handleCastling(Cell startKingCell, Cell endKingCell, MoveType type) {
    if (!startKingCell.isOccupiedBy(FigureType.KING)) {
      throw new UnsupportedOperationException("A castling move can only be done by a king.");
    }

    Cell startRookCell;
    Cell endRookCell;
    switch (type) {
      case KING_CASTLING -> {
        startRookCell = findCell(8, startKingCell.y());
        endRookCell = endKingCell.leftCell();
      }
      case QUEEN_CASTLING -> {
        startRookCell = findCell(1, startKingCell.y());
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
    return this.turn;
  }

  public boolean canPerformQueenSideCastling(FigureColor color) {
    Cell kingCell = findKing(color);
    return ((King) kingCell.figure()).canPerformQueenSideCastling(kingCell);
  }

  public boolean canPerformKingSideCastling(FigureColor color) {
    Cell kingCell = findKing(color);
    return ((King) kingCell.figure()).canPerformKingSideCastling(kingCell);
  }

  public String enPassant() {
    return this.enPassant;
  }

  public int fullMove() {
    return this.fullMove;
  }

  public int halfMove() {
    return this.halfMove;
  }
}
