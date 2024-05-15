
// Author: Erik Smit & Hugo Larsson Wilhelmsson
import java.util.Queue;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {

    // Define regex patterns
    private final static String PEN_REGEX = "(UP|DOWN)|";
    private final static String INSTR_REGEX = "(FORW|BACK|LEFT|RIGHT|COLOR|REP)";
    // # followed by 0-9, A-F or a-f, and needs to end there
    private final static String HEX_REGEX = "(#[0-9A-Fa-f]{6}\\b)|";
    // Use positive lookbehind and lookahead to match only the numbers in the string
    // and match spaces (decimal numbers). Need space between instructions and
    // parameters (decimal is parameter)
    private final static String DEC_REGEX = "(?<=^|\\s)\\d+(?=[\\.\\s\\n]+)|";
    // Blanc space or new line, need space between instructions and parameters,
    // concats with INSTR_REGEX
    private final static String SPACE_AFTER_REGEX = "([\\s\\n])|";
    private final static String DOT = "(\\.)|";
    private final static String QUOTE = "(\")|";

    private String[] linedSplitInput;
    private int currentLine = 1;
    private Queue<Token> queue;

    // Compile the regex patterns, create a pattern
    private static final Pattern PATTERN = Pattern.compile(

            DOT +
                    QUOTE +
                    PEN_REGEX +
                    INSTR_REGEX +
                    SPACE_AFTER_REGEX +
                    HEX_REGEX +
                    DEC_REGEX +
                    "(\\n)");

    // Define more regex patterns
    private static final String HEX_PATTERN = "#[0-9A-Fa-f]{6}";
    private static final String DEC_PATTERN = "\\d+";
    private static final String INSTR_PATTERN = "FORW|BACK|LEFT|RIGHT|UP|DOWN|COLOR|REP";

    public Lexer(String input) {
        processInput(input);
        this.queue = new LinkedList<>();
        tokenize();
    }

    /**
     * Process the input by splitting it into lines and adding a space at the end
     * 
     * @param input
     */
    private void processInput(String input) {
        // Split every element into seperate lines, even empty lines (-1)
        this.linedSplitInput = input.toUpperCase().split("\n", -1);
        // With every element in linesWithSpaces, add a blanc space to every element and
        // make it a new array
        String[] linesWithSpaces = Arrays.stream(linedSplitInput)
                .map(s -> s + " ") // Add a space at the end of each line, if command and parameter are on multiple
                                   // lines (newline == whitespace)
                .toArray(String[]::new);
        // Now we have a new array where we have split the input into lines and added
        // spaces
        this.linedSplitInput = linesWithSpaces;
    }

    // Tokenize the input
    private void tokenize() {
        int prevTokenLine = 0; // Keep track of the previous token line
        // Loop through the lines
        while (currentLine <= linedSplitInput.length) {
            String line = this.linedSplitInput[currentLine - 1]; // Get the current line
            line = line.split("%")[0] + " "; // Remove comments by splitting and select index 0 (what comes before %)

            String m = null;
            Matcher matcher = PATTERN.matcher(line);

            // Loop through the line and match the regex patterns
            while (!line.isEmpty() && matcher.find()) {
                // Gets the latest matched String
                m = matcher.group();

                // Continue if new line
                if (m.equals("\n")) {
                    continue;
                }

                // Continue if only blanc spaces
                if (m.trim().isEmpty()) {
                    continue;
                }

                // Match the token type (type gets its token type)
                TokenType type = matchType(m.trim());
                prevTokenLine = currentLine;
                queue.add(new Token(type, m.trim(), this.currentLine));

                // Check if the line has more tokens, if so, move on to those tokens (substring
                // starting at the end of current token)
                int matchLength = m.length();
                line = line.trim();
                int lengthCheck = line.length() - m.length();
                if (lengthCheck < 0) {
                    matchLength--;
                }
                line = line.substring(matchLength);
            }

            if (!line.trim().isEmpty()) {
                queue.add(new Token(TokenType.ERROR, "", currentLine));
            }

            this.currentLine++;
        }
        // Add EOF (End Of File) token, use prevteTokenLine to get the line number
        // (currentLine - 1)
        queue.add(new Token(TokenType.EOF, "", prevTokenLine));
    }

    public Token getNextToken() {
        return queue.poll();
    }

    // Return the token type
    private TokenType matchType(String m) {
        if (m.matches(HEX_PATTERN))
            return TokenType.HEX;
        else if (m.matches(DEC_PATTERN))
            return TokenType.DECIMAL;
        else if (m.matches(INSTR_PATTERN))
            return TokenType.valueOf(m.toUpperCase());
        else if (m.equals("\""))
            return TokenType.QUOTE;
        else if (m.equals("."))
            return TokenType.PERIOD;
        else
            return TokenType.ERROR;
    }

}