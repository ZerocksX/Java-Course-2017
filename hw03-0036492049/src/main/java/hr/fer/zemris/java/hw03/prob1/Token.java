package hr.fer.zemris.java.hw03.prob1;

/**
 * Simple token class that has {@link TokenType} and value
 *
 * @author Pavao Jerebić
 */
public class Token {

    /**
     * Type of the token
     */
    private TokenType type;

    /**
     * Value of the token
     */
    private Object value;

    /**
     * Constructor for token with its' type and value
     *
     * @param type  token type
     * @param value token value
     */
    public Token(TokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    /**
     * getter for token type
     *
     * @return token type
     */
    public TokenType getType() {
        return type;
    }

    /**
     * getter for token value
     *
     * @return token value
     */
    public Object getValue() {
        return value;
    }
}
