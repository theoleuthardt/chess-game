package hwr.oop.chess.persistence;

import hwr.oop.chess.application.figures.FigureColor;

public class Player {
  private String name;
  private FigureColor color;
  private int elo; // rank system: 0=lowest; rank count of won games

  public Player(String name, FigureColor color, int elo) {
    this.name = name;
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

  public String name() {
    return this.name;
  }

  // Setter Methods
  public void setElo(int elo) {
    this.elo = elo;
  }

  public void setColor(FigureColor color) {
    this.color = color;
  }

  public void setName(String name) {
    this.name = name;
  }

  // TODO input from user
}
