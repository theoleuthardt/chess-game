package hwr.oop.chess.persistence;

import hwr.oop.chess.application.Game;

import java.util.ArrayList;
import java.util.List;

public class PortableGameNotation {

  private String wrapLongLines(String pgnString) {
    StringBuilder wrappedPgn = new StringBuilder();
    int lineLength = 0;
    for (String word : pgnString.split(" ")) {
      if (lineLength + word.length() > 80) {
        wrappedPgn.append("\n");
        lineLength = 0;
      }
      if (lineLength > 0) {
        wrappedPgn.append(" ");
        lineLength++;
      }
      wrappedPgn.append(word);
      lineLength += word.length();
    }
    return wrappedPgn.toString();
  }

  private List<String> addCounter(List<String> pgnHistory) {
    List<String> moves = new ArrayList<>();
    int moveCount = 2;
    for (String move : pgnHistory) {
      if (moveCount % 2 == 0) {
        moves.add(moveCount / 2 + ". " + move);
      } else {
        moves.add(move);
      }
      moveCount++;
    }
    return moves;
  }

  private String pgnString(Game game) {
    List<String> pgnHistory = game.pgnHistory();
    pgnHistory = addCounter(pgnHistory);
    if (!game.isOver()) {
      pgnHistory.add("*");
    }
    return wrapLongLines(String.join(" ", pgnHistory));
  }

  public static String generatePgn(Game game) {
    PortableGameNotation pgn = new PortableGameNotation();
    return pgn.pgnString(game);
  }
}
