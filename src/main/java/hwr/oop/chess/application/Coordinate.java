package hwr.oop.chess.application;

public enum Coordinate {
  ONE {
    public int toInt() {
      return 1;
    }
  },
  TWO {
    public int toInt() {
      return 2;
    }
  },
  THREE {
    public int toInt() {
      return 3;
    }
  },
  FOUR {
    public int toInt() {
      return 4;
    }
  },
  FIVE {
    public int toInt() {
      return 5;
    }
  },
  SIX {
    public int toInt() {
      return 6;
    }
  },
  SEVEN {
    public int toInt() {
      return 7;
    }
  },
  EIGHT {
    public int toInt() {
      return 8;
    }
  };

  public abstract int toInt();

  public static Coordinate fromInt(int i) {
    return switch (i) {
      case 1 -> Coordinate.ONE;
      case 2 -> Coordinate.TWO;
      case 3 -> Coordinate.THREE;
      case 4 -> Coordinate.FOUR;
      case 5 -> Coordinate.FIVE;
      case 6 -> Coordinate.SIX;
      case 7 -> Coordinate.SEVEN;
      case 8 -> Coordinate.EIGHT;
      default -> throw new IllegalArgumentException("Invalid coordinate!");
    };
  }

  public static Coordinate fromChar(char c) {
    return Coordinate.fromInt(c - 96);
  }
}
