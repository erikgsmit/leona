// Author: Erik Smit & Hugo Larsson Wilhelmsson

import java.util.Scanner;

public class Leona {
    // Main method, get the input and calls parseAndExecute
    public static void main(String[] args) {
        String input = getInput();
        parseAndExecute(input);
    }

    // Takes an input, of one or multiple Strings, and create a StringBuilder to
    // make one String with \n between them
    private static String getInput() {
        StringBuilder sb = new StringBuilder();

        try (Scanner sc = new Scanner(System.in)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (!line.isEmpty()) {
                    // Trim is used to remove blanc spaces in the beginning and in the end
                    sb.append(line.trim()).append("\n");
                    continue;
                }
                sb.append(line.trim()).append("\n");
            }
            sc.close(); // Close scanner
        }

        return sb.toString();
    }

    // Create instances if lexer, parser and parseTree. Parse the
    // If parseTree is not empty, create an instance of turtle and execute it
    // (handles the different commands) in the
    // parseTree, then update the turtle (prints the turtle)
    private static void parseAndExecute(String input) {
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        ParseTree parseTree = parser.parse();

        if (parseTree != null) {
            Turtle turtle = new Turtle();
            parseTree.execute(turtle);
            turtle.update();
        }
    }
}