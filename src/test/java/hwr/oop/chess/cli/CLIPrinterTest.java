package hwr.oop.chess.cli;

import hwr.oop.chess.persistence.CSVFilePersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

class CLIPrinterTest {

  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private final CLIAdapter cli =
      new CLIAdapter(new PrintStream(outputStream), new CSVFilePersistence());
  private final CLIPrinter printer = cli.printer();

  @BeforeEach
  void setUp() {
    outputStream.reset();
  }

  @Test
  void printlnTest() {
    printer.println("Hello");
    printer.println("World!");
    assertThat(outputStream.toString()).containsIgnoringNewLines("Hello", "World!");
  }

  @Test
  void printTest() {
    printer.print("Hello");
    printer.print("World!");
    assertThat(outputStream.toString()).contains("HelloWorld!");
  }

  @Test
  void emptyPrintln() {
    printer.println();
    String emptyPrintln = outputStream.toString();
    outputStream.reset();

    printer.println("");
    String printlnWithEmptyString = outputStream.toString();

    assertThat(emptyPrintln).isEqualTo(printlnWithEmptyString).contains("\n");
  }

  @Test
  void printColor_isYellow() {
    printer.print("Yellow", CLIColor.YELLOW);
    assertThat(outputStream.toString()).startsWith("\033[30;1;103mYellow\033[0m");
  }

  @Test
  void printColor_isGray() {
    printer.print("Gray", CLIColor.GRAY);
    assertThat(outputStream.toString()).startsWith("\033[37mGray\033[0m");
  }

  @Test
  void printColor_isBlue() {
    printer.print("Blue", CLIColor.BLUE);
    assertThat(outputStream.toString()).startsWith("\033[30;1;104mBlue\033[0m");
  }

  @Test
  void printlnError_passException() {
    InvalidUserInputException exception = new InvalidUserInputException("ExceptionTest");
    printer.printlnError(exception);
    assertThat(outputStream.toString()).contains("\033[30;1;103m ERROR \033[0m ExceptionTest");
  }

  @Test
  void printlnAction() {
    printer.printlnAction("ActionTest");
    assertThat(outputStream.toString()).contains("\033[30;1;104m ACTION \033[0m ActionTest");
  }
}
