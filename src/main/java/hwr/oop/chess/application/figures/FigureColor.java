package hwr.oop.chess.application.figures;

public enum FigureColor {
  WHITE {
    public FigureColor ofOpponent() {
      return BLACK;
    }
  },
  BLACK {
    public FigureColor ofOpponent() {
      return WHITE;
    }
  };

  public abstract FigureColor ofOpponent();
}
