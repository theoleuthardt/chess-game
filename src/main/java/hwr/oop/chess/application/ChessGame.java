package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.persistence.FenNotation;
import hwr.oop.chess.persistence.Persistence;
import hwr.oop.chess.persistence.Player;

public class ChessGame {
  private final Persistence persistence;
  private final Board board;

  public ChessGame(Persistence persistence, boolean isNew) {
    this.persistence = persistence;
    board = new Board(false);
    if (isNew) {
      newGame();
    } else {
      loadGame();
    }
  }

  private void newGame() {
    board.addFiguresToBoard();
    Player player1 = new Player(FigureColor.WHITE, 0);
    Player player2 = new Player(FigureColor.BLACK, 0);
  }

  private void loadGame() {
    persistence.loadGame();
    FenNotation.parseFEN(board, persistence.loadState("fen"));
  }

  public void saveGame() {
    persistence.storeState("fen", FenNotation.generateFen(board));
    persistence.saveGame();
  }

  public Board board() {
    return board;
  }
}
