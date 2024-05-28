package hwr.oop.chess.persistence;

import hwr.oop.chess.application.figures.FigureColor;

public class Player {
  private FigureColor color;
  private int elo; // rank system: 0=lowest; rank count of won games

  public Player(FigureColor color, int elo) {
    this.color = color;
    this.elo = elo;
  }

  // Getter Methods
  public int elo() {
    return this.elo;
  }

  public FigureColor color() {
    return this.color;
  }

  // Setter Methods
  public void setElo(int elo) {
    this.elo = elo;
  }

  public void setColor(FigureColor color) {
    this.color = color;
  }
}
