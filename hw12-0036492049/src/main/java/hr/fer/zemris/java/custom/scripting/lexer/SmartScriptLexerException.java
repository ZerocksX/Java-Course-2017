package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Exception that is thrown when {@link SmartScriptLexer} has an exception
 *
 * @author Pavao Jerebić
 */
public class SmartScriptLexerException extends RuntimeException {
    /**
     * Constructor
     */
    public SmartScriptLexerException() {
    }

    /**
     * Constructor with message
     *
     * @param s message
     */
    public SmartScriptLexerException(String s) {
        super(s);
    }
}
