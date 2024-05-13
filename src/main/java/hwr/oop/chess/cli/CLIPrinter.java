package hwr.oop.chess.cli;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class CLIPrinter {
  private final PrintStream printStream;
  private final List<Cell> highlightCell = new ArrayList<>();

  CLIPrinter(OutputStream out) {
    this.printStream = new PrintStream(out);
  }

  private String applyColor(String message, CLIColor color) {
    return color.code() + message + CLIColor.RESET.code();
  }

  // Print text and start a new line
  public void println() {
    printStream.println();
  }

  public void println(String message) {
    printStream.println(message);
  }

  public void println(String message, CLIColor color) {
    printStream.println(applyColor(message, color));
  }

  // Print text but do not start a new line
  public void print(String message) {
    printStream.print(message);
  }

  public void print(String message, CLIColor color) {
    printStream.print(applyColor(message, color));
  }

  public void print(Board board) {
    printWithCoordinates(linesFromBoard(board));
  }

  public void alsoHighlightOnBoard(Cell cell) {
    highlightCell.add(cell);
  }

  public void setHighlightOnBoard(List<Cell> cells) {
    highlightCell.removeIf(e -> true);
    highlightCell.addAll(cells);
  }

  private List<String> linesFromBoard(Board board) {
    List<Cell> cells = board.allCells();
    List<String> lines = new ArrayList<>();
    StringJoiner currentLine = new StringJoiner(" ");
    for (Cell cell : cells) {
      String currentSymbol = String.valueOf(cell.isFree() ? '-' : cell.figure().symbol());

      if (highlightCell.contains(cell)) {
        currentSymbol = applyColor(currentSymbol, CLIColor.BLUE);
      }
      currentLine.add(currentSymbol);

      if (cell.rightCell() == null) {
        lines.add(currentLine.toString());
        currentLine = new StringJoiner(" ");
      }
    }
    return lines;
  }

  private void printWithCoordinates(List<String> rows) {
    int y = rows.size();
    for (String row : rows) {
      print(y + " | ", CLIColor.GRAY);
      println(row);
      y--;
    }
    println("  \\________________", CLIColor.GRAY);
    println("    A B C D E F G H", CLIColor.GRAY);
  }

  private String stringToWidth(int width, String text) {
    String format = "%-" + width + "s";
    return String.format(format, text);
  }

  public void printAsTable(String title, int firstColumWidth, Map<String, String> rows) {
    println(title, CLIColor.BLUE);
    rows.forEach(
        (firstCol, secondCol) -> {
          firstCol = stringToWidth(firstColumWidth, "- " + firstCol + ":");
          print(firstCol);
          println(secondCol, CLIColor.GRAY);
        });
  }

  public void printlnError(Exception e) {
    printlnError(e.getMessage());
  }

  public void printlnError(String message) {
    print(" ERROR ", CLIColor.YELLOW);
    println(" " + message);
  }

  public void printlnAction(String message) {
    print(" ACTION ", CLIColor.BLUE);
    println(" " + message);
  }
}
