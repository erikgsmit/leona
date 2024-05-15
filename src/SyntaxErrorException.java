// Author: Erik Smit & Hugo Larsson Wilhelmsson

// Purpose: Exception class for syntax errors in the input file.
// Using super to use the current message as error message
public class SyntaxErrorException extends RuntimeException {
    public SyntaxErrorException(String message) {
        super(message);
    }
}
