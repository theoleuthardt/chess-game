package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.*;

import java.util.ArrayList;
import java.util.Objects;

public class Board {
  private static Cell firstCell;

  public Board(boolean setFigures) {
    initializeBoard(setFigures);
  }

  private void initializeBoard(boolean setFigures) {
    // TODO make codes simple
    // Create the first cell and store it in firstCell
    firstCell = new Cell(1, 1);
    Cell leftCell = firstCell;
    Cell topCellRowStart = firstCell;
    Cell topCell = firstCell;

    // Create and connect cells for each row and column of the board
    for (int y = 1; y <= 8; y++) {
      for (int x = 1; x <= 8; x++) {
        // Skip the first cell since it's already created
        if (y == 1 && x == 1) {
          continue;
        }
        if (x == 1) {
          topCell = topCellRowStart;
        }
        // Create a new cell
        Cell currentCell = new Cell(x, y);
        // Connect horizontally
        connectCells(leftCell, currentCell);
        if (y != 1) {
          // Connect vertically
          connectCells(currentCell, topCell);
          if (x != 1) {
            // Connect diagonally to the left
            connectCells(currentCell, topCell.leftCell());
          }
          if (x != 8) {
            // Connect diagonally to the right
            connectCells(currentCell, topCell.rightCell());
          } else {
            // Change Row
            topCellRowStart = topCellRowStart.topCell();
          }
        }
        // Change next cell as the current cell
        leftCell = currentCell;
        topCell = topCell.rightCell();
      }
    }
    if (setFigures) {

      addFiguresToBoard();
    }
  }

  // Method to connect each cell
  public void connectCells(Cell currentCell, Cell nextCell) {
    int x = currentCell.x();
    int y = currentCell.y();
    int diffX = x - nextCell.x();
    int diffY = y - nextCell.y();

    // do not connect edges
    if (diffX == 1 && x == 8 || diffX == -1 && x== 1 || diffY == 1 && y == 1 || diffX == -1 && y == 8) {
      return;
    }

    switch (diffX) {
      case 0->{
        switch (diffY) {
          // Connect vertically
          case 1 -> {
              currentCell.setBottomCell(nextCell);
              Objects.requireNonNull(nextCell).setTopCell(currentCell);
          }
          // Connect vertically
          case -1 -> {
              currentCell.setTopCell(nextCell);
              Objects.requireNonNull(nextCell).setBottomCell(currentCell);
          }
        }
      }
      case 1->{
        switch (diffY) {
          // Connect horizontally
          case 0 ->{
            currentCell.setLeftCell(nextCell);
            Objects.requireNonNull(nextCell).setRightCell(currentCell);
          }
          // Connect diagonally
          case 1 -> {
              currentCell.setBottomLeftCell(nextCell);
              Objects.requireNonNull(nextCell).setTopRightCell(currentCell);
          }
          // Connect diagonally
          case -1 -> {
              currentCell.setTopLeftCell(nextCell);
              Objects.requireNonNull(nextCell).setBottomRightCell(currentCell);
          }
        }
      }
      case -1 ->{
        switch (diffY) {
          // Connect horizontally
          case 0 ->{
            currentCell.setRightCell(nextCell);
            Objects.requireNonNull(nextCell).setLeftCell(currentCell);
          }
          // Connect diagonally
          case 1 -> {
              currentCell.setBottomRightCell(nextCell);
              Objects.requireNonNull(nextCell).setTopLeftCell(currentCell);
          }
          // Connect diagonally
          case -1 -> {
              currentCell.setTopRightCell(nextCell);
              Objects.requireNonNull(nextCell).setBottomLeftCell(currentCell);
          }
        }
      }
    }
  }

  public static Cell firstCell() {
    return firstCell;
  }

  public static ArrayList<Cell> allCells() {
    ArrayList<Cell> cells = new ArrayList<>();
    Cell cell = firstCell;
    Cell rowStart = cell;

    while (cell != null) {
      cells.add(cell);
      if (cell.rightCell() != null) {
        cell = cell.rightCell();
      } else {
        cell = rowStart = rowStart.topCell();
      }
    }
    return cells;
  }

  public Cell cell(char x, int y) {
    return cell(x - 96, y);
  }

  public Cell cell(int x, int y) {
    Cell searchFor = new Cell(x, y);
    ArrayList<Cell> cells = Board.allCells();
    for (Cell cell : cells) {
      if (cell.isEqualTo(searchFor)) {
        return cell;
      }
    }
    return null;
  }

  private void addFiguresToBoard() {
    ArrayList<Cell> cells = Board.allCells();
    for (Cell cell : cells) {
      // Set up white figures
      if (cell.y() == 1) {
        switch (cell.x()) {
          case 1 -> cell.setFigure(new Rook(FigureColor.WHITE));
          case 2 -> cell.setFigure(new Knight(FigureColor.WHITE));
          case 3 -> cell.setFigure(new Bishop(FigureColor.WHITE));
          case 4 -> cell.setFigure(new Queen(FigureColor.WHITE));
          case 5 -> cell.setFigure(new King(FigureColor.WHITE));
          case 6 -> cell.setFigure(new Bishop(FigureColor.WHITE));
          case 7 -> cell.setFigure(new Knight(FigureColor.WHITE));
          case 8 -> cell.setFigure(new Rook(FigureColor.WHITE));
        }
      }
      if (cell.y() == 2) {
        cell.setFigure(new Pawn(FigureColor.WHITE));
      }

      // Set up black figures
      if (cell.y() == 7) {
        cell.setFigure(new Pawn(FigureColor.BLACK));
      }
      if (cell.y() == 8) {
        switch (cell.x()) {
          case 1 -> cell.setFigure(new Rook(FigureColor.BLACK));
          case 2 -> cell.setFigure(new Knight(FigureColor.BLACK));
          case 3 -> cell.setFigure(new Bishop(FigureColor.BLACK));
          case 4 -> cell.setFigure(new Queen(FigureColor.BLACK));
          case 5 -> cell.setFigure(new King(FigureColor.BLACK));
          case 6 -> cell.setFigure(new Bishop(FigureColor.BLACK));
          case 7 -> cell.setFigure(new Knight(FigureColor.BLACK));
          case 8 -> cell.setFigure(new Rook(FigureColor.BLACK));
        }
      }
    }
  }

  public void printBoard() {
    ArrayList<Cell> cells = Board.allCells();
    for (Cell cell : cells) {
      Figure figure = cell.getFigure();
      // Print figure if it exists, otherwise print empty position
      if (figure != null) {
        System.out.print(figure.symbol() + " "); // Assuming Figure class has getSymbol method
      } else {
        System.out.print("- "); // Empty position symbol
      }

      if (cell.rightCell() == null) {
        System.out.println();
      }
    }
  }

  // Method to move a piece on the board
  public void moveFigure(int startX, int startY, int endX, int endY)  {
    if (!isValidCoordinate(startX, startY) || !isValidCoordinate(endX, endY)) {
      throw new IllegalArgumentException(
          "Invalid coordinates. Coordinates must be between 1 and 8.");
    }
    // Get the piece at the start position
    Cell startCell = cell(startX, startY);
    Cell endCell = cell(endX, endY);

    Figure figure = startCell.getFigure();
    if (figure == null) {
      throw new RuntimeException("On the starting cell is no figure");
    }

    if (!figure.canMoveTo(startCell, endCell)) {
      throw new RuntimeException("The figure can't move to that cell");
    }

    startCell.setFigure(null);
    endCell.setFigure(figure);
  }

  public void moveFigureDiagonal(
      Board board, CellDirection direction, int startX, int startY, int diff)  {
    switch (direction) {
      case TOP_LEFT -> board.moveFigure(startX, startY, startX - diff, startY + diff);
      case TOP_RIGHT -> board.moveFigure(startX, startY, startX + diff, startY + diff);
      case BOTTOM_LEFT -> board.moveFigure(startX, startY, startX - diff, startY - diff);
      case BOTTOM_RIGHT -> board.moveFigure(startX, startY, startX + diff, startY - diff);
    }
  }

  public Figure getFigureOnField(int x, int y) {
    return Objects.requireNonNull(cell(x, y)).getFigure();
  }

  public boolean isFigureOnField(int x, int y) {
    try {
      getFigureOnField(x, y);
      return true;
    } catch (RuntimeException e) {
      return false;
    }
  }

  private boolean isValidCoordinate(int x, int y) {
    return x >= 1 && x <= 8 && y >= 1 && y <= 8;
  }
}
