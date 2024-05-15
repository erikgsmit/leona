// Author: Erik Smit & Hugo Larsson Wilhelmsson

import java.util.ArrayList;
import java.util.List;

/*
 * The parser class is responsible for parsing the tokens from the lexer and
 * creating a parse tree
 */
public class Parser {
    private Lexer lexer;
    private Token currentToken;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.currentToken = lexer.getNextToken();
    }

    public ParseTree parse() {
        try {
            return program();
        } catch (Exception exc) {
            System.out.println("Syntaxfel på rad " + currentToken.getPos());
            return null;
        }

    }

    // Parse the entire program
    private ParseTree program() {
        List<ParseTree> instructList = new ArrayList<>();
        // While not end of file, keep parsing instructions
        while (currentToken.getType() != TokenType.EOF) {
            instructList.add(instruction());
        }
        return new Program(instructList);
    }

    // Parse individual instructions
    private ParseTree instruction() {
        TokenType type = currentToken.getType();
        switch (type) {
            case FORW:
            case BACK:
                return parseMovement();
            case UP:
            case DOWN:
                return parsePen();
            case LEFT:
            case RIGHT:
                return parseRotation();
            case COLOR:
                return parseColor();
            case REP:
                return parseRepeat();
            default:
                throw new SyntaxErrorException("Syntax Error");
        }
    }

    // Parse movement instructions
    private ParseTree parseMovement() {
        TokenType type = currentToken.getType();
        eat(type);
        Token decimal = eat(TokenType.DECIMAL);
        eat(TokenType.PERIOD);
        int val = checkInteger(decimal);
        return new MovementNode(type, val);
    }

    // Parse pen instructions
    private ParseTree parsePen() {
        TokenType type = currentToken.getType();
        eat(type);
        eat(TokenType.PERIOD);
        return new PenNode(type);
    }

    // Parse rotation instruction
    private ParseTree parseRotation() {
        TokenType type = currentToken.getType();
        eat(type);
        Token decimal = eat(TokenType.DECIMAL);
        eat(TokenType.PERIOD);
        int val = checkInteger(decimal);
        return new RotationNode(type, val);
    }

    // Parse color instruction
    private ParseTree parseColor() {
        eat(TokenType.COLOR);
        Token hex = eat(TokenType.HEX);
        eat(TokenType.PERIOD);
        return new ColorNode(hex.getData());

    }

    // Parse repeat instruction
    private ParseTree parseRepeat() {
        eat(TokenType.REP);
        Token decimal = eat(TokenType.DECIMAL);
        int val = checkInteger(decimal);

        Token firstToken = currentToken;
        List<ParseTree> repIntructs = new ArrayList<>();
        if (currentToken.getType() == TokenType.QUOTE) {
            eat(TokenType.QUOTE);
            while (currentToken.getType() != TokenType.QUOTE) {
                repIntructs.add(instruction());
            }
            eat(TokenType.QUOTE); // Expect end with quote
        } else {
            // Non quoted instructions
            repIntructs.add(instruction());
        }

        if (repIntructs.isEmpty()) {
            currentToken = firstToken;
            throw new IllegalArgumentException("Error: There are no instructions to repeat");
        }
        return new RepNode(val, repIntructs);
    }

    /*
     * Helper method to consume the current token and advance to the next token
     *
     * @param type the expected token type
     * 
     * @return the consumed token
     */
    private Token eat(TokenType type) {
        if (currentToken.getType() == type) {
            Token temp = currentToken; // Temporary placeholder
            currentToken = lexer.getNextToken();
            return temp;
        } else {
            throw new SyntaxErrorException("Syntax error");
        }
    }

    /*
     * Helper method to check if the token is an integer
     *
     * @param token the token to check
     * 
     * @return the integer value of the token
     */
    private int checkInteger(Token token) {
        int result = Integer.parseInt(token.getData());
        if (result <= 0) {
            throw new SyntaxErrorException("Error: Expected a positive integer value.");
        }
        return result;
    }
}

/*
 * Abstract class for the parse tree
 */
abstract class ParseTree {
    public abstract void execute(Turtle turtle);
}

class Program extends ParseTree {
    private List<ParseTree> instructions;

    public Program(List<ParseTree> instructions) {
        this.instructions = new ArrayList<>(instructions);
    }

    @Override
    public void execute(Turtle turtle) {
        for (ParseTree instruct : instructions) {
            instruct.execute(turtle);
        }
    }

}

class MovementNode extends ParseTree {
    private TokenType type;
    private int val;

    public MovementNode(TokenType type, int val) {
        this.type = type;
        this.val = val;
    }

    @Override
    public void execute(Turtle turtle) {
        switch (type) {
            case FORW:
                turtle.forward(val);
                break;
            case BACK:
                turtle.backward(val);
                break;
            default:
                throw new SyntaxErrorException("Error: movement type");
        }
    }

}

class PenNode extends ParseTree {
    private TokenType type;

    public PenNode(TokenType type) {
        this.type = type;
    }

    @Override
    public void execute(Turtle turtle) {
        switch (type) {
            case UP:
                turtle.penUp();
                break;
            case DOWN:
                turtle.penDown();
                break;
            default:
                throw new SyntaxErrorException("Error: pen type");
        }
    }
}

class RotationNode extends ParseTree {
    private TokenType type;
    private int val;

    public RotationNode(TokenType type, int val) {
        this.type = type;
        this.val = val;
    }

    @Override
    public void execute(Turtle turtle) {
        switch (type) {
            case LEFT:
                turtle.left(val);
                break;
            case RIGHT:
                turtle.right(val);
                break;
            default:
                throw new SyntaxErrorException("Error: rotation type");
        }
    }
}

class ColorNode extends ParseTree {
    private String color;

    public ColorNode(String color) {
        this.color = color;
    }

    @Override
    public void execute(Turtle turtle) {
        turtle.setColor(color);
    }
}

class RepNode extends ParseTree {
    private int val;
    private List<ParseTree> instructions;

    public RepNode(int val, List<ParseTree> instructions) {
        this.val = val;
        this.instructions = new ArrayList<>(instructions);
    }

    @Override
    public void execute(Turtle turtle) {
        for (int i = 0; i < val; i++) {
            for (ParseTree instruct : instructions) {
                instruct.execute(turtle);
            }
        }
    }
}