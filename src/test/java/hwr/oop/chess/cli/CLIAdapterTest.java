package hwr.oop.chess.cli;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CLIAdapterTest {

    @Test
    void printlnTest() {
        // given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CLIAdapter cliAdapter = new CLIAdapter(outputStream);
        String message = isWindows() ? "Hello, World!\r\n" : "Hello, World!\n";
        // when
        cliAdapter.println("Hello, World!");

        // then
        assertEquals(message, outputStream.toString());
    }

    @Test
    void printTest() {
        // given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CLIAdapter cliAdapter = new CLIAdapter(outputStream);
        String message = "Hello, World!";

        // when
        cliAdapter.print("Hello, World!");

        // then
        assertEquals(message, outputStream.toString());
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
}