package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.cli.CLIAdapter;

public class ChessGame {
    private final CLIAdapter cli;
    private final Board board;


    public ChessGame(CLIAdapter cli) {
        this.cli = cli;

        board = new Board(cli);

        loadGame();

        if (board.isCheckmate(FigureColor.BLACK)) {
            cli.println("Black lost");
        }
        if (board.isCheckmate(FigureColor.WHITE)) {
            cli.println("White lost");
        }
    }

    private void loadGame() {
        board.addFiguresToBoard(cli.persistence().loadState("figures"));
    }

    public void saveGame(int gameId) {
        cli.persistence().storeState("figures", board.figuresOnBoard());
        cli.persistence().saveGame(gameId);
    }

    public Board board() {
        return board;
    }
}
