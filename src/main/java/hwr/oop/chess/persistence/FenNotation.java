package hwr.oop.chess.persistence;

// https://www.chess.com/de/terms/forsyth-edwards-notation-fen

// pawn = "P", knight = "N", bishop = "B", rook = "R", queen = "Q" and king = "K"

public class FenNotation {
  // TODO connect with board
  // TODO connect with GameDataManager
  private String cell;
  private String activeColor;
  private String castling;
  private String enPassant;
  private int halfmoveClock;
  private int fullmoveNumber;

  public FenNotation(
      String cell,
      String activeColor,
      String castling,
      String enPassant,
      int halfmoveClock,
      int fullmoveNumber) {
    this.cell = cell;
    this.activeColor = activeColor;
    this.castling = castling;
    this.enPassant = enPassant;
    this.halfmoveClock = halfmoveClock;
    this.fullmoveNumber = fullmoveNumber;
  }

  // getter setter
  public String cell() {
    return this.cell;
  }

  public void setPosition(String cell) {
    this.cell = cell;
  }

  public String getActiveColor() {
    return this.activeColor;
  }

  public void setActiveColor(String activeColor) {
    this.activeColor = activeColor;
  }

  public String getCastling() {
    return this.castling;
  }

  public void setCastling(String castling) {
    this.castling = castling;
  }

  public String getEnPassant() {
    return this.enPassant;
  }

  public void setEnPassant(String enPassant) {
    this.enPassant = enPassant;
  }

  public int getHalfmoveClock() {
    return this.halfmoveClock;
  }

  public void setHalfmoveClock(int halfmoveClock) {
    this.halfmoveClock = halfmoveClock;
  }

  public int getFullmoveNumber() {
    return this.fullmoveNumber;
  }

  public void setFullmoveNumber(int fullmoveNumber) {
    this.fullmoveNumber = fullmoveNumber;
  }
}
