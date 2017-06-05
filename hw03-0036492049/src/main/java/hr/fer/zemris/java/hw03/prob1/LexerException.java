package hr.fer.zemris.java.hw03.prob1;

/**
 * Thrown when lexer can't parse given string
 *
 * @author Pavao Jerebić
 */
public class LexerException extends RuntimeException {
    /**
     * Basic constructor
     */
    public LexerException() {
    }

    /**
     * Constructor with message
     *
     * @param s message
     */
    public LexerException(String s) {
        super(s);
    }
}
