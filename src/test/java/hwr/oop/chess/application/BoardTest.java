package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.application.figures.FigureType;
import hwr.oop.chess.cli.CLIAdapter;
import hwr.oop.chess.cli.InvalidUserInputException;
import hwr.oop.chess.persistence.FenNotation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Logger logger = Logger.getLogger(BoardTest.class.getName());
    private Board board;

    @BeforeEach
    void setUp() {
        // Initialize the board
        board = Mockito.spy(new Board(true));
    }

    void assertThatFigureOnCellHasSymbol(int x, int y, char symbol) {
        Cell cell = board.findCell(x, y);
        assertThat(cell.figure()).isNotNull();
        assertThat(cell.figure().symbol()).isEqualTo(symbol);
    }

    @Test
    void initialBoard_withCorrectSymbolOnEachPosition() {
        for (int x = 1; x <= 8; x++) {
            // row of black figures
            assertThatFigureOnCellHasSymbol(x, 8, " rnbqkbnr ".charAt(x));
            assertThatFigureOnCellHasSymbol(x, 7, " pppppppp ".charAt(x));

            // row of white figures
            assertThatFigureOnCellHasSymbol(x, 2, " PPPPPPPP ".charAt(x));
            assertThatFigureOnCellHasSymbol(x, 1, " RNBQKBNR ".charAt(x));
        }
    }

    @Test
    void initialBoard_onlyMiddleIsFree() {
        List<Cell> cells = board.allCells();
        assertThat(cells).hasSize(64);
        for (Cell cell : cells) {
            if (cell.y() != 1 && cell.y() != 2 && cell.y() != 7 && cell.y() != 8) {
                assertThat(cell.figure()).isNull();
            } else {
                assertThat(cell.figure()).isNotNull();
            }
        }
    }

    @Test
    void initialBoard_withoutFiguresIsEmpty() {
        Board boardNoFigure = new Board(false);
        List<Cell> cells = boardNoFigure.allCells();
        assertThat(cells).hasSize(64);
        for (Cell cell : cells) {
            assertThat(cell.figure()).isNull();
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
        for (int x : IntStream.range(2, 8).toArray()) {
            for (int y : IntStream.range(2, 8).toArray()) {
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
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> board.moveFigure(4, 1, 9, 9))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testCheckMateBlackKing_h7() {
        board = (new Board(new CLIAdapter(System.out)));
        FenNotation fen = new FenNotation();
        // Status CheckMate
        String fenString = "2K5/1B6/8/8/8/4b2N/R7/4r2k b - -";
        fen.placeFigureFromFEN(board, fenString);
        assertThat(board.isCheckmate(FigureColor.BLACK)).isTrue();
    }

    @Test
    void testDoesNotCheckMate() {
        board = (new Board(new CLIAdapter(System.out)));
        FenNotation fen = new FenNotation();
        // Status No CheckMate
        String fenString = "8/4Q1R1/R7/5k2/3pP3/5K2/8/8 b - -";
        fen.placeFigureFromFEN(board, fenString);
        assertThat(board.isCheckmate(FigureColor.BLACK)).isFalse();
    }

    // @Test TODO Update Checkmate
    void testCheckMateBlackKing_e4() {
        board = (new Board(new CLIAdapter(System.out)));
        FenNotation fen = new FenNotation();
        String fenString = "8/4Q1R1/R7/5k2/3pP3/5K2/8/8 b - -";
        fen.placeFigureFromFEN(board, fenString);
        assertThat(board.isCheckmate(FigureColor.BLACK)).isTrue();
    }

    @Test
    void testFindCellWithString(){
        // invalid Value
        assertThrows(InvalidUserInputException.class, () -> {
            board.findCell("c3d");
        });
        assertThrows(InvalidUserInputException.class, () -> {
            board.findCell("a");
        });
        assertNotNull(board.findCell("a1"));
        assertNotNull(board.findCell("e5"));
    }

    @Test
    void testPrintBoard() {
        board = (new Board(new CLIAdapter(System.out)));
        board.printBoard();
    }

    @Test
    void testAddFiguresToBoard(){
        board = (new Board(new CLIAdapter(System.out)));
        // Initial Status as string
        String string = "rnbqkbnrpppppppp                                PPPPPPPPRNBQKBNR";
        board.addFiguresToBoard(string);
        testInitailPosition();
        testInitailColor();
    }

    @Test
    void testAddFiguresToBoardWithLongString(){
        board = (new Board(new CLIAdapter(System.out)));
        // Initial Status as string
        String string = "rnbqkbnrpppppppp                                PPPPPPPPRNBQKBNR_aaa";
        assertThrows(IllegalArgumentException.class, () -> {
            board.addFiguresToBoard(string);
        });
    }

    @Test
    void testAddFiguresToBoardNoParams(){
        board = (new Board(new CLIAdapter(System.out)));
        board.addFiguresToBoard(null);
        testInitailPosition();
        testInitailColor();
    }

    @Test
    void testFiguresOnBoard(){
        String initial = "rnbqkbnrpppppppp                                PPPPPPPPRNBQKBNR";
        String string = board.figuresOnBoard();
        assertThat(string).isEqualTo(initial);
    }

    @Test
    void testMoveFigureWithInteger(){
        board.moveFigure(4,2,4,4);
        assertThat(board.findCell(4,2).figure()).isNull();
        assertThat(board.findCell(4,4).figure().type()).isEqualTo(FigureType.PAWN);
    }

    void testInitailPosition(){
        assertThat(board.findCell(1,1).figure().type()).isEqualTo(FigureType.ROOK);
        assertThat(board.findCell(2,1).figure().type()).isEqualTo(FigureType.KNIGHT);
        assertThat(board.findCell(3,1).figure().type()).isEqualTo(FigureType.BISHOP);
        assertThat(board.findCell(4,1).figure().type()).isEqualTo(FigureType.QUEEN);
        assertThat(board.findCell(5,1).figure().type()).isEqualTo(FigureType.KING);
        assertThat(board.findCell(6,1).figure().type()).isEqualTo(FigureType.BISHOP);
        assertThat(board.findCell(7,1).figure().type()).isEqualTo(FigureType.KNIGHT);
        assertThat(board.findCell(8,1).figure().type()).isEqualTo(FigureType.ROOK);
        assertThat(board.findCell(1,8).figure().type()).isEqualTo(FigureType.ROOK);
        assertThat(board.findCell(2,8).figure().type()).isEqualTo(FigureType.KNIGHT);
        assertThat(board.findCell(3,8).figure().type()).isEqualTo(FigureType.BISHOP);
        assertThat(board.findCell(4,8).figure().type()).isEqualTo(FigureType.QUEEN);
        assertThat(board.findCell(5,8).figure().type()).isEqualTo(FigureType.KING);
        assertThat(board.findCell(6,8).figure().type()).isEqualTo(FigureType.BISHOP);
        assertThat(board.findCell(7,8).figure().type()).isEqualTo(FigureType.KNIGHT);
        assertThat(board.findCell(8,8).figure().type()).isEqualTo(FigureType.ROOK);

            IntStream.range(1, 9).forEach(i ->{
                assertThat(board.findCell(i,2).figure().type()).isEqualTo(FigureType.PAWN);
                assertThat(board.findCell(i,7).figure().type()).isEqualTo(FigureType.PAWN);
            });
    }

    void testInitailColor(){
        IntStream.range(1, 9).forEach(i ->{
            assertThat(board.findCell(i,1).figure().color()).isEqualTo(FigureColor.WHITE);
            assertThat(board.findCell(i,2).figure().color()).isEqualTo(FigureColor.WHITE);
            assertThat(board.findCell(i,7).figure().color()).isEqualTo(FigureColor.BLACK);
            assertThat(board.findCell(i,8).figure().color()).isEqualTo(FigureColor.BLACK);
        });
    }

}
