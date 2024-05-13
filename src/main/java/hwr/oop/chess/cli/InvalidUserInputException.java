package hwr.oop.chess.cli;

public class InvalidUserInputException extends RuntimeException {
  public InvalidUserInputException(String e) {
    super(e);
  }
}
