package hr.fer.zemris.bf.lexer;

/**
 * Token that is produces by {@link Lexer#nextToken()}
 *
 * @author Pavao Jerebić
 */
public class Token {

    /**
     * Token type
     */
    private TokenType tokenType;
    /**
     * Token value
     */
    private Object tokenValue;

    /**
     * Constructor for token
     *
     * @param tokenType  token type
     * @param tokenValue token value
     */
    public Token(TokenType tokenType, Object tokenValue) {
        this.tokenType = tokenType;
        this.tokenValue = tokenValue;
    }


    /**
     * Getter for token type
     *
     * @return token type
     */
    public TokenType getTokenType() {
        return tokenType;
    }

    /**
     * Getter for token value
     *
     * @return token value
     */
    public Object getTokenValue() {
        return tokenValue;
    }

    @Override
    public String toString() {
        return "Type: " + tokenType
                + (tokenValue == null
                ? (", Value: null")
                : (", Value: " + tokenValue.toString() + ", Value is instance of: " + tokenValue.getClass().getCanonicalName())
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;

        Token token = (Token) o;

        return tokenType == token.tokenType && (tokenValue != null ? tokenValue.equals(token.tokenValue) : token.tokenValue == null);
    }

    @Override
    public int hashCode() {
        int result = tokenType.hashCode();
        result = 31 * result + (tokenValue != null ? tokenValue.hashCode() : 0);
        return result;
    }
}
