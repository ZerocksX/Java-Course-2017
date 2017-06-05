package hr.fer.zemris.java.hw04.db.parser;

/**
 * Exception to call in {@link QueryParser}
 * @author Pavao Jerebić
 */
public class QueryParserException extends RuntimeException{
    /**
     * Basic constructor
     */
    public QueryParserException() {
    }

    /**
     * Constructor with message
     * @param s message
     */
    public QueryParserException(String s) {
        super(s);
    }
}
