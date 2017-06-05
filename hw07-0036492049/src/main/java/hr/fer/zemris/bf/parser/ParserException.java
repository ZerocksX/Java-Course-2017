package hr.fer.zemris.bf.parser;

/**
 * To be called when parser has an exception
 *
 * @author Pavao Jerebić
 */
public class ParserException extends RuntimeException {

    /**
     * Basic constructor
     */
    public ParserException() {
    }

    /**
     * Constructor with a message
     *
     * @param s a message
     */
    public ParserException(String s) {
        super(s);
    }
}
