package hwr.oop.chess.persistence;

public class Player {
  private int doubleOfScore;

  public Player(String doubleOfScore) {
    this.doubleOfScore = Integer.parseInt(doubleOfScore);
  }

  public double score() {
    return doubleOfScore / 2.0;
  }

  public int doubleOfScore() {
    return doubleOfScore;
  }

  public void fullPointOnWin() {
    doubleOfScore += 2;
  }

  public void halfPointOnDraw() {
    doubleOfScore += 1;
  }
}
