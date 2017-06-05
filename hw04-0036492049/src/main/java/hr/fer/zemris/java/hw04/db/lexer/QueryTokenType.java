package hr.fer.zemris.java.hw04.db.lexer;

/**
 * Query token type enum<br/>
 * Consists of FIELD, OPERATOR, STRING, AND and EOL types
 *
 * @author Pavao Jerebić
 */
public enum QueryTokenType {
    /**
     * field of {@link hr.fer.zemris.java.hw04.db.StudentRecord}
     */
    FIELD,
    /**
     * One of operators: <, <=, ==, <>, >, >= and LIKE
     */
    OPERATOR,
    /**
     * String surrounded with quotation marks
     */
    STRING,
    /**
     * word "and", not case sensitive
     */
    AND,
    /**
     * End of line
     */
    EOL
}