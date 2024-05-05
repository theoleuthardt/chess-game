package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.*;
import hwr.oop.chess.cli.CLIAdapter;
import hwr.oop.chess.cli.InvalidUserInputException;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private CLIAdapter cli;
    private Cell firstCell;

    public Board(CLIAdapter cli) {
        this.cli = cli;
        initializeBoard();
    }

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
            Cell bottomCell = bottomCellRowStart = (bottomCellRowStart == null) ? firstCell : bottomCellRowStart.topCell();

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
        List<Cell> cells = allCells();
        for (Cell cell : cells) {
            if (cell.x() == x && cell.y() == y) {
                return cell;
            }
        }
        return null;
    }

    public void printBoard() {
        printBoard(null);
    }

    public void printBoard(List<Cell> highlightCell) {
        List<Cell> cells = allCells();
        cli.println();
        for (Cell cell : cells) {
            if (cell.leftCell() == null) {
                cli.printGray(cell.y() + " | ");
            }

            Figure figure = cell.figure();
            String cellText = String.valueOf(figure == null ? '-' : figure.symbol());

            // Print figure if it exists, otherwise print empty position
            if (highlightCell.contains(cell)) {
                cli.printBlue(cellText);
            } else {
                cli.print(cellText);
            }

            cli.print(" ");
            if (cell.rightCell() == null) {
                cli.println("");
            }
        }
        cli.printlnGray("  \\________________");
        cli.printlnGray("    A B C D E F G H");
    }

    private void addFiguresToBoard() {
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
            throw new IllegalArgumentException("The figurePositions should be a 64 character long string, but it is " + figurePositions.length() + " characters long.");
        }
        int figureIndex = 0;
        for (Cell cell : allCells()) {
            char symbol = figurePositions.charAt(figureIndex);
            FigureColor color = Character.isUpperCase(symbol) ? FigureColor.WHITE : FigureColor.BLACK;
            Figure figure = switch (Character.toLowerCase(symbol)) {
                case ' ' -> null;
                case 'p' -> new Pawn(color);
                case 'r' -> new Rook(color);
                case 'b' -> new Bishop(color);
                case 'k' -> new King(color);
                case 'q' -> new Queen(color);
                case 'n' -> new Knight(color);
                default -> throw new InvalidUserInputException("The is no figure with the type " + symbol);
            };
            cell.setFigure(figure);
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
            throw new InvalidUserInputException("Invalid coordinates. Coordinates must be between 1 and 8.");
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

        if (isCheck(figure.color())) {
            throw new InvalidUserInputException("TODO: Only the king can be moved (if it is a field where the opponent can't get");
        }

        if (!figure.canMoveTo(startCell, endCell)) {
            throw new InvalidUserInputException("The figure can't move to that cell");
        }

        startCell.setFigure(null);
        endCell.setFigure(figure);
    }

    // TODO make check
    // TODO make checkmate

    public Cell cellWithKingOfColor(FigureColor playerColor) {
        List<Cell> cells = allCells();
        for (Cell cell : cells) {
            if (cell.figure() != null && cell.figure().type() == FigureType.KING && cell.figure().color() == playerColor) {
                return cell;
            }
        }
        throw new InvalidUserInputException("Impossible state! There is no king on the field.");
    }

    public boolean isCheck(FigureColor playerColor) {
        Cell kingCell = cellWithKingOfColor(playerColor);
        List<Cell> opponentCells = cellsWithColor(playerColor == FigureColor.WHITE ? FigureColor.BLACK : FigureColor.WHITE);
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
        Cell kingCell = cellWithKingOfColor(playerColor);
        List<Cell> kingCanMoveTo = kingCell.figure().getAvailableCells(kingCell);
        List<Cell> opponentCells = cellsWithColor(playerColor == FigureColor.WHITE ? FigureColor.BLACK : FigureColor.WHITE);

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


    // TODO make castling
    // TODO make enpassnt
    // TODO count move
    // TODO write Compare, CompareTo
}
