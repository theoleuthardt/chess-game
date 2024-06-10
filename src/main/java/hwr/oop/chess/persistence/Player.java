package hwr.oop.chess.persistence;

public class Player {
  private double score;
  private int elo;
  private int gameCount;

  public Player(String score, String elo, String gameCount) {
    this(Double.parseDouble(score), Integer.parseInt(elo), Integer.parseInt(gameCount));
  }

  public Player(double score, int elo, int gameCount) {
    this.score = score;
    this.elo = elo;
    this.gameCount = gameCount;
  }

  private void countGameEnd(double addToScore) {
    gameCount += 1;
    score += addToScore;
  }

  public double score() {
    return score;
  }

  public int elo() {
    return elo;
  }

  public int gameCount() {
    return gameCount;
  }

  public void adjustScoreOnGameEnd(double score, double opponentElo) {
    countGameEnd(score);

    double expectedScore = calculateExpectedScore(elo, opponentElo);
    elo = calculateNewRating(score, expectedScore);
  }

  // Method to calculate the expected score
  private double calculateExpectedScore(double ratingA, double ratingB) {
    return 1.0 / (1.0 + Math.pow(10, (ratingB - ratingA) / 400));
  }

  // Method to calculate the new rating
  private int calculateNewRating(double score, double expectedScore) {
    return (int) Math.floor(elo + kFactor() * (score - expectedScore));
  }

  // Method to determine the K-factor
  public int kFactor() {
    if (gameCount < 30) {
      return 40; // New players with less than 30 games
    } else if (elo < 2400) {
      return 20; // Intermediate players
    } else {
      return 10; // High-level players
    }
  }
}
