package hwr.oop.chess.cli;

public class InvalidUserInputException extends IllegalArgumentException {
    public InvalidUserInputException(String e) {
        super(e);
    }
}
