package hr.fer.zemris.bf.lexer;

/**
 * To be called when an exception happens in {@link Lexer}
 * @author Pavao Jerebić
 */
public class LexerException extends RuntimeException {

    /**
     * Basic constructor
     */
    public LexerException() {
    }

    /**
     * Constructor with a message
     * @param s a message
     */
    public LexerException(String s) {
        super(s);
    }
}
