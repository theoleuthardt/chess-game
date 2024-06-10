package hwr.oop.chess.persistence;

public enum State {
  FEN_HISTORY {
    @Override
    public String toString() {
      return "fen";
    }
  },
  WINNER {
    @Override
    public String toString() {
      return "winner";
    }
  },
  WHITE_SCORE {
    @Override
    public String toString() {
      return "whiteScore";
    }
  },
  BLACK_SCORE {
    @Override
    public String toString() {
      return "blackScore";
    }
  },
  END_TYPE {
    @Override
    public String toString() {
      return "endType";
    }
  },
  IS_DRAW_OFFERED {
    @Override
    public String toString() {
      return "isDrawOffered";
    }
  }, PGN;

  @Override
  public abstract String toString();
}
