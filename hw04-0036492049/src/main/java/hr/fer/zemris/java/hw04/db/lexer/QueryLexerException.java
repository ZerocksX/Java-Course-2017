package hr.fer.zemris.java.hw04.db.lexer;

/**
 * Exception for {@link QueryLexer}
 * @author Pavao Jerebić
 */
public class QueryLexerException extends RuntimeException {
    /**
     * Base constructor
     */
    public QueryLexerException() {
        super();
    }

    /**
     * Constructor with message
     * @param s message
     */
    public QueryLexerException(String s) {
        super(s);
    }
}
