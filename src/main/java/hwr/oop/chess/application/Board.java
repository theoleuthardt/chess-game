package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.*;
import hwr.oop.chess.cli.InvalidUserInputException;

import java.util.ArrayList;
import java.util.List;

import static hwr.oop.chess.application.Cell.isKingInitialPosition;
import static hwr.oop.chess.application.Cell.isRookInitialPosition;
import static hwr.oop.chess.persistence.FenNotation.charToFigureType;

public class Board {
  private Cell firstCell;
  private boolean canCastlingWhiteKing;
  private boolean canCastlingWhiteQueen;
  private boolean canCastlingBlackKing;
  private boolean canCastlingBlackQueen;
  private String enPassant;
  private int halfmoveClockWhite;
  private int halfmoveClockBlack;
  private int fullmoveNumber;
  private FigureColor turn;

  public Board(boolean setFigures) {
    initializeBoard();
    if (setFigures) {
      addFiguresToBoard();
    }
  }

  private void initializeBoard() {
    Cell bottomCellRowStart = null;
    this.canCastlingWhiteKing = true;
    this.canCastlingWhiteQueen = true;
    this.canCastlingBlackKing = true;
    this.canCastlingBlackQueen = true;
    this.enPassant = "-";
    this.halfmoveClockWhite = 0;
    this.halfmoveClockBlack = 0;
    this.fullmoveNumber = 0;
    this.turn = FigureColor.WHITE;

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
    Cell cell = firstCell;
    while (cell.topCell() != null) {
      cell = cell.topCell();
    }
    Cell rowStart = cell;

    while (cell != null) {
      cells.add(cell);
      if (cell.rightCell() != null) {
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
    List<Cell> cells = this.allCells();
    for (Cell cell : cells) {
      if (cell.x() == x && cell.y() == y) {
        return cell;
      }
    }
    return null;
  }

  public void addFiguresToBoard() {
    List<Cell> cells = allCells();
    for (Cell cell : cells) {
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
        Figure figure = charToFigureType(figureType);
        cell.setFigure(figure);
      }
      figureIndex++;
    }
  }

  public String figuresOnBoard() {
    StringBuilder figurePositions = new StringBuilder();
    List<Cell> cells = allCells();
    for (Cell cell : cells) {
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

    Figure figure = startCell.figure();
    if (figure == null) {
      throw new InvalidUserInputException("On the starting cell is no figure");
    }

    if (isCheckmate(figure.color())) {
      throw new InvalidUserInputException("The game is over as you are in Checkmate!");
    }

    if (!figure.canMoveTo(startCell, endCell)) {
      throw new InvalidUserInputException("The figure can't move to that cell");
    }

    MoveType moveType = moveType(startX, startY, endX, endY);
    if(moveType == MoveType.NORMAL){
      startCell.setFigure(null);
      endCell.setFigure(figure);
      // Check Status
      checkMoveKing(startX, startY);
      checkMoveRook(startX, startY);
    }else{
      handleCastling(findCell(startX, startY), moveType);
    }

    Figure figureOnEndCell = endCell.figure();

    if (isCheck(figure.color())) {
      // After this move the king is not allowed to be in check anymore.
      // As the king is still in check, we undo this action
      startCell.setFigure(figure);
      endCell.setFigure(figureOnEndCell);
      throw new InvalidUserInputException(
          "This move is not allowed as your king would be in check! Move a figure so that your king is not in check (anymore).");
    }

    this.fullmoveNumber++;
    this.changeTurn();
  }

  public Cell findKing(FigureColor playerColor) {
    List<Cell> cells = allCells();
    for (Cell cell : cells) {
      if (cell.figure() != null
          && cell.figure().type() == FigureType.KING
          && cell.figure().color() == playerColor) {
        return cell;
      }
    }
    throw new InvalidUserInputException("Impossible state! There is no king on the field.");
  }
  public List<Cell> findRook(FigureColor playerColor) {
    List<Cell> rooks = new ArrayList<>();
    List<Cell> cells = allCells();
    for (Cell cell : cells) {
      if (cell.figure() != null
              && cell.figure().type() == FigureType.ROOK
              && cell.figure().color() == playerColor) {
        rooks.add(cell);
      }
    }
    return rooks;
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
    Cell kingCell = findKing(playerColor);
    List<Cell> kingCanMoveTo = kingCell.figure().getAvailableCells(kingCell);
    List<Cell> opponentCells =
        cellsWithColor(playerColor == FigureColor.WHITE ? FigureColor.BLACK : FigureColor.WHITE);

    for (Cell escapeCell : kingCanMoveTo) {
      boolean opponentCanMoveHere = false;
      for (Cell opponentCell : opponentCells) {
        if (opponentCell.figure().canMoveTo(opponentCell, escapeCell)) {
          opponentCanMoveHere = true;
          break;
        }
      }
      if (!opponentCanMoveHere) {
        return false;
      }
    }

    return true;
  }

  public List<Cell> cellsWithColor(FigureColor myColor) {
    List<Cell> cells = allCells();
    cells.removeIf(cell -> cell.figure() == null);
    cells.removeIf(cell -> cell.figure().color() != myColor);
    return cells;
  }

  private void changeTurn() {
    this.turn = this.turn == FigureColor.BLACK ? FigureColor.WHITE : FigureColor.BLACK;
  }

  private void handleCastling(Cell kingCell, MoveType type) {
    if (type == MoveType.KING_CASTLING) {
      castlingKing(kingCell);
    }
    if (type == MoveType.QUEEN_CASTLING) {
      castlingQueen(kingCell);
    }
  }

  private MoveType moveType(int startX, int startY, int endX, int endY) {
    int diffX = endX - startX;
    int diffY = endY - startY;

    if(startX == 5 && startY == 1 || startX == 5 && startY == 8){
      Cell kingCell = findCell(startX, startY);
      if (kingCell.figure().type() == FigureType.KING) {
        King king = (King) kingCell.figure();
        if (diffX == 2 && diffY == 0 && king.canCastlingKing(kingCell)) {
          return MoveType.KING_CASTLING;
        }
        if (diffX == -2 && diffY == 0 && king.canCastlingQueen(kingCell)) {
          return MoveType.QUEEN_CASTLING;
        }
      }
    }
    return MoveType.NORMAL;
  }

  private void castlingKing(Cell kingCell){
    // Mark King as moved
    ((King)kingCell.figure()).figureMoved();
    handleCastingValueWithKing(kingCell.figure().color());

    // Mark Rook as moved
    Rook newRook = new Rook(kingCell.figure().color());
    newRook.figureMoved();

    int row = 0;
    if(kingCell.figure().color() == FigureColor.WHITE){
      row = 1;
    } else{
      row = 8;
    }
    // Move Rook
    findCell(8, row).setFigure(null);
    findCell(7, row).setFigure(kingCell.figure());
    findCell(6, row).setFigure(newRook);
    findCell(5, row).setFigure(null);
  }

  private void castlingQueen(Cell kingCell){
    // Mark King as moved
    ((King)kingCell.figure()).figureMoved();
    handleCastingValueWithKing(kingCell.figure().color());

    // Mark Rook as moved
    Rook newRook = new Rook(kingCell.figure().color());
    newRook.figureMoved();

    int row = 0;
    if(kingCell.figure().color() == FigureColor.WHITE){
      row = 1;
    } else{
      row = 8;
    }
    // Move Rook
    findCell(1, row).setFigure(null);
    findCell(3, row).setFigure(kingCell.figure());
    findCell(4, row).setFigure(newRook);
    findCell(5, row).setFigure(null);
  }

  private void checkMoveKing(int startX, int startY){
    Cell kingCell = findCell(startX, startY);
    if (isKingInitialPosition(kingCell)) {
      King king = (King) kingCell.figure();
      king.figureMoved();
      handleCastingValueWithKing(king.color());
    }
  }

  private void checkMoveRook(int startX, int startY){
    Cell rookCell = findCell(startX, startY);
    if(isRookInitialPosition(rookCell)){
      Rook rook = (Rook) rookCell.figure();
      rook.figureMoved();
      handleCastingValueWithRook(startX, startY);
    }
  }

  private void handleCastingValueWithKing(FigureColor color){
    if (color == FigureColor.WHITE) {
      this.canCastlingWhiteKing = false;
      this.canCastlingWhiteQueen = false;
    } else {
      this.canCastlingBlackKing = false;
      this.canCastlingBlackQueen = false;
    }
  }

  private void handleCastingValueWithRook(int startX, int startY) {
    if(startY == 1){
      if (startX == 1) {
        this.canCastlingWhiteQueen = false;
      } else {
        // startX == 8
        this.canCastlingWhiteKing = false;
      }
    } else {
      if (startX == 1) {
        this.canCastlingBlackQueen = false;
      } else {
        // startX == 8
        this.canCastlingBlackKing = false;
      }
    }
  }

  public FigureColor turn(){
    return this.turn;
  }
  public boolean canCastlingWhiteKing(){
    return this.canCastlingWhiteKing;
  }
  public boolean canCastlingWhiteQueen(){
    return this.canCastlingWhiteQueen;
  }
  public boolean canCastlingBlackKing(){
    return this.canCastlingBlackKing;
  }
  public boolean canCastlingBlackQueen(){
    return this.canCastlingBlackQueen;
  }
  public String enPassant(){
    return this.enPassant;
  }
  public int fullmoveNumber(){
    return this.fullmoveNumber;
  }
  public int halfmoveClockWhite(){
    return this.halfmoveClockWhite;
  }
  public int halfmoveClockBlack(){
    return this.halfmoveClockBlack;
  }
}
