package hwr.oop.chess.cli;

import java.io.OutputStream;
import java.io.PrintStream;

public class CLIAdapter {
    protected final PrintStream printStream;

    public CLIAdapter(OutputStream outputStream) {
        this.printStream = new PrintStream(outputStream);
    }

    public void println(String message) {
        printStream.println(message);
    }

    public void print(String message) {
        printStream.print(message);
    }
}
