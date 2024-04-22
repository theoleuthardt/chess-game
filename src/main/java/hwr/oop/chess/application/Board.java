package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.*;

import java.util.ArrayList;
import java.util.Objects;

public class Board {
  private Cell firstCell;

  public Board() {
    initializeBoard();
  }

  private void initializeBoard() {
    // Create the first cell and store it in firstCell
    firstCell = new Cell(1, 1);
    Cell previousCell = firstCell;
    Cell previousRowStart = firstCell;
    Cell previousRowCell = firstCell;

    // Create and connect cells for each row and column of the board
    for (int y = 1; y <= 8; y++) {
      for (int x = 1; x <= 8; x++) {
        // Skip the first cell since it's already created
        if (y == 1 && x == 1) {
          continue;
        }
        if (x == 1) {
          previousRowCell = previousRowStart;
        }
        // Create a new cell
        Cell currentCell = new Cell(x, y);
        // Connect horizontally
        connectCells(previousCell, currentCell);
        if (y != 1) {
          // Connect vertically
          connectCells(currentCell, previousRowCell);
          if (x != 1) {
            // Connect diagonally to the left
            connectCells(currentCell, previousRowCell.leftCell());
          }
          if (x != 8) {
            // Connect diagonally to the right
            connectCells(currentCell, previousRowCell.rightCell());
          } else {
            // Change Row
            previousRowStart = previousRowStart.topCell();
          }
        }
        // Change next cell as the current cell
        previousCell = currentCell;
        previousRowCell = previousRowCell.rightCell();
      }
    }
    // Set up the initial chess cells
    setUpInitialChessPositions();
  }

  // Method to connect each cell
  private void connectCells(Cell cell1, Cell cell2) {
    Objects.requireNonNull(cell1);
    Objects.requireNonNull(cell2);

    if (cell1.isEqualTo(cell2)) {
      throw new IllegalArgumentException("The cells are not allowed to be equal");
    }

    // Connect horizontally
    if (cell1.y() == cell2.y()) {
      if (cell1.x() < cell2.x()) { // cell1 is left of cell2
        cell1.setRightCell(cell2);
        cell2.setLeftCell(cell1);
      } else { // cell1 is right of cell2
        cell1.setLeftCell(cell2);
        cell2.setRightCell(cell1);
      }
      return;
    }

    // Connect vertically
    if (cell1.x() == cell2.x()) {
      if (cell1.y() > cell2.y()) { // cell1 is above cell2
        cell1.setBottomCell(cell2);
        cell2.setTopCell(cell1);
      } else { // cell1 is below cell2
        cell1.setTopCell(cell2);
        cell2.setBottomCell(cell1);
      }
      return;
    }

    // Connect diagonally
    if (cell1.y() > cell2.y()) { // cell1 is above cell2
      if (cell1.x() < cell2.x()) { // cell1 is left of cell2
        cell1.setBottomRightCell(cell2);
        cell2.setTopLeftCell(cell1);
      } else { // cell1 is right of cell2
        cell1.setBottomLeftCell(cell2);
        cell2.setTopRightCell(cell1);
      }
      return;
    }

    // Connect diagonally, cell1 is below cell2
    if (cell1.x() < cell2.x()) { // cell1 is left of cell2
      cell1.setTopRightCell(cell2);
      cell2.setBottomLeftCell(cell1);
    } else { // cell1 is right of cell2
      cell1.setTopCell(cell2);
      cell2.setBottomCell(cell1);
    }
  }

  public Cell firstCell() {
    return firstCell;
  }

  public ArrayList<Cell> allCells() {
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

  private void setUpInitialChessPositions() {
    ArrayList<Cell> cells = allCells();
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

  public Cell findCell(int x, int y) {
    Cell searchFor = new Cell(x, y);
    ArrayList<Cell> cells = allCells();
    for (Cell cell : cells) {
      if (cell.isEqualTo(searchFor)) {
        return cell;
      }
    }
    throw new RuntimeException("The given cell is not found");
  }

  public void printBoard() {
    ArrayList<Cell> cells = allCells();
    for (Cell cell : cells) {
      Figure figure = cell.getFigure();
      // Print figure if it exists, otherwise print empty cell
      if (figure != null) {
        System.out.print(figure.symbol() + " "); // Assuming Figure class has getSymbol method
      } else {
        System.out.print("- "); // Empty cell symbol
      }

      if (cell.rightCell() == null) {
        System.out.println();
      }
    }
  }

  // Method to move a piece on the board
  public void moveFigure(int startX, int startY, int endX, int endY) {
    // Get the piece at the start cell
    Cell startCell = findCell(startX, startY);
    Cell endCell = findCell(endX, endY);

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

  public Figure getFigureOnField(int x, int y) {
    return Objects.requireNonNull(findCell(x, y)).getFigure();
  }

  public boolean isFigureOnField(int x, int y) {
    try {
      getFigureOnField(x, y);
      return true;
    } catch (RuntimeException e) {
      return false;
    }
  }
}
