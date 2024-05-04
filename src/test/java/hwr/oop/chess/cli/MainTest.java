package hwr.oop.chess.cli;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final CLIAdapter cliAdapter = new CLIAdapter(new PrintStream(outputStream));
    private Main main;

    @BeforeEach
    public void setUp() {
        main = new Main(new String[]{});
        main.cli = cliAdapter;
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(cliAdapter.printStream);
    }

    @Test
    public void testPrintCommands() {
        // given
        String expected = """
                Chess Commands:\r
                - cli on <ID> move <FROM> to <TO>:      Move a figure\r                
                - cli create <ID>:                      Create a new game of chess\r
                - cli show-board <ID>:                  Show the board\r
                \r
                """;

        // when
        main.printCommands();

        // then
        assertEquals(expected, outputStream.toString());
    }
}