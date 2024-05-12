package hwr.oop.chess.persistence;

import java.io.IOException;

public class LoadingErrorException extends IOException {
  public LoadingErrorException(String e) {
    super(e);
  }
}
