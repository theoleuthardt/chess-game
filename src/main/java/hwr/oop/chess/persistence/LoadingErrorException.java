package hwr.oop.chess.persistence;

public class LoadingErrorException extends RuntimeException {
  public LoadingErrorException(Throwable e) {
    super(e);
  }
}
