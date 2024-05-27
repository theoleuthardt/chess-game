package hwr.oop.chess.application.figures;

public enum FigureColor {
  WHITE {
    public FigureColor opposite() {
      return BLACK;
    }
  },
  BLACK {
    public FigureColor opposite() {
      return WHITE;
    }
  };

  public abstract FigureColor opposite();
}
