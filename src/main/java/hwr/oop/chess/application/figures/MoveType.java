package hwr.oop.chess.application.figures;

public enum MoveType {
  KING_CASTLING,
  QUEEN_CASTLING,
  ENPASSANT,
  CATCH_FIGURE, // for counting half move
  MOVE_PAWN, // for counting half move
  MOVE_BLACK, // for counting full move
  NORMAL
}
