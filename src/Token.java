// Author: Erik Smit & Hugo Larsson Wilhelmsson

/*
 * Enum class that represent the different token types
 */
enum TokenType {
    FORW, BACK, LEFT, RIGHT, DOWN, UP, COLOR, REP, PERIOD, QUOTE, HEX, DECIMAL, ERROR, EOF
}

/*
 * Token class that represent a token in the input
 */
class Token {
    private TokenType type;
    private String data;
    private int pos;

    // Constructor
    public Token(TokenType type, String data, int pos) {
        this.type = type;
        this.data = data;
        this.pos = pos;
    }

    public TokenType getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public int getPos() {
        return pos;
    }

    // Adds parenthesis around the data if it is not null
    public String toString() {
        if (data == null)
            return type.toString();
        else
            return type.toString() + "(" + data.toString() + ")";
    }
}
