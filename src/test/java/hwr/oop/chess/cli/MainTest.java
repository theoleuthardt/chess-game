package hwr.oop.chess.cli;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public void testNoArguments() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});
        String expected = """             
                \r
                Chess Commands:\r
                - cli on <ID> move <FROM> to <TO>:      Move a figure\r                
                - cli create <ID>:                      Create a new game of chess\r
                - cli show-board <ID>:                  Show the board\r
                \r
                """;

        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testWithArguments() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{"arg1", "arg2"});
        String expected = """             
                \r
                Arguments: [arg1, arg2]\r
                """;

        assertEquals(expected, outContent.toString());
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