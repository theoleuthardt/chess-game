package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.FigureColor;
import hwr.oop.chess.persistence.Player;
import java.util.List;

import java.util.Map;

public interface Game {

  void saveGame();

  Board board();

  void playerHasWon(EndType endType, FigureColor color);

  void endsWithDraw(EndType endType);

  Map<FigureColor, Player> players();

  boolean isOver();

  boolean isDrawOffered();

  void offerDraw();

  void denyDrawOffer();

  Game keepPlayersOf(Game game);

  List<String> fenHistory();
}
