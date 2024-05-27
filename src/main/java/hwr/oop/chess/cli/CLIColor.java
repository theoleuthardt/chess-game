package hwr.oop.chess.cli;

public enum CLIColor {
  YELLOW("\033[30;1;103m"),
  BLUE("\033[30;1;104m"),
  GRAY("\033[37m"),
  RESET("\033[0m");

  private final String ansiCode;

  CLIColor(String ansiCode) {
    this.ansiCode = ansiCode;
  }

  public String code() {
    return ansiCode;
  }
}
