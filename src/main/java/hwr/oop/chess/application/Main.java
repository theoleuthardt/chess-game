package hwr.oop.chess.application;

import java.util.Arrays;
import java.util.logging.Logger;

public class Main {
  public static void main(String[] args) {
    Logger logger = Logger.getLogger(Main.class.getName());
    logger.info("Arguments: " + Arrays.asList(args));
  }
}
