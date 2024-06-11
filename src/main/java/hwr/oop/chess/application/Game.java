package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.application.figures.FigureType;
import hwr.oop.chess.persistence.Player;
import java.util.List;

public interface Game {

  void saveGame();

  Board board();

  void playerHasWon(EndType endType, FigureColor color);

  void endsWithDraw(EndType endType);

  Player player(FigureColor color);

  boolean isOver();

  boolean isDrawOffered();

  void offerDraw();

  void denyDrawOffer();

  Game keepPlayersOf(Game game);

  List<String> fenHistory();

  List<String> pgnHistory();

  String pgnHistoryOfMoves();

  boolean isThreeFoldRepetition();

  void rememberAndPerformPawnPromotion(Cell startCell, FigureType toFigure);

  void rememberAndPerformMove(Cell from, Cell to);
}
