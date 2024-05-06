package hwr.oop.chess.cli;

import hwr.oop.chess.persistence.CsvGameRepository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// The use of System.out is allowed here as the only place in the repo!
@java.lang.SuppressWarnings("java:S106")
public class Main {
    private final CLIAdapter cli;

    public Main(String[] args, CLIAdapter cli) {
        this.cli = cli;
        cli.println("");
        List<String> arguments = new LinkedList<>(List.of(args));
        if (arguments.isEmpty() || arguments.getFirst().equals("help")) {
            printCommands();
            printParameterTypes();
            return;
        }
        String command = arguments.removeFirst();

        List<String> commandsWithGameId = List.of("on", "show");
        if (commandsWithGameId.contains(command)) {
            cli.forGameId(arguments.removeFirst());
        }

        try {
            switch (command) {
                case "create" -> cli.createGame(arguments.removeFirst());
                case "show" -> cli.showBoard();
                case "on" -> cli.performActionOnBoard(arguments.removeFirst(), arguments);
                default -> throw new InvalidUserInputException("Unknown command: " + command);
            }

            if (!arguments.isEmpty()) {
                throw new InvalidUserInputException("You passed more arguments than expected: " + arguments);
            }
        } catch (InvalidUserInputException e) {
            cli.printlnError(e);
        }
        cli.println();
    }

    public static void main(String[] args) {
        new Main(args, new CLIAdapter(System.out, new CsvGameRepository()));
    }

    private String padWithSpaces(int columnSize, String text) {
        String format = "%-" + columnSize + "s";
        return String.format(format, text);
    }

    private String toListName(int columnSize, String text) {
        return padWithSpaces(columnSize, "- " + text + ": ");
    }

    public void printCommands() {
        Map<String, String> commandAndExplanation = new HashMap<>();
        commandAndExplanation.put("chess create <ID>", "Create a new game of chess");
        commandAndExplanation.put("chess show <ID>", "Show the board");
        commandAndExplanation.put("chess on <ID> move <FROM> <TO>", "Move a figure");
        commandAndExplanation.put("chess on <ID> show-moves <FROM>", "Show where the figure can go");
        commandAndExplanation.put("chess on <ID> promote <FROM> <TYPE>", "Promote a pawn");

        cli.println("Chess Commands:");
        commandAndExplanation.forEach((k, v) -> cli.println(toListName(40, k) + v));
        cli.println("");
    }

    public void printParameterTypes() {
        Map<String, String> parameterTypes = new HashMap<>();
        parameterTypes.put("ID", "Game ID (e.g. 123)");
        parameterTypes.put("FROM", "XY-Coordinate of the cell (e.g. a1, c4, 22)");
        parameterTypes.put("TYPE", "Figure (e.g. knight, rook, queen)");

        cli.println("Parameter Types:");
        parameterTypes.forEach((k, v) -> cli.println(toListName(10, k) + v));
        cli.println("");
    }
}
