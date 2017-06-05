package hr.fer.zemris.java.hw04.db.lexer;

/**
 * Token representation
 *
 * @author Pavao Jerebić
 */
public class QueryToken {
    /**
     * Type
     */
    private QueryTokenType type;
    /**
     * Value
     */
    private String value;

    /**
     * Constructor for token
     *
     * @param type  type
     * @param value value
     * @throws IllegalArgumentException if type is null
     */
    public QueryToken(QueryTokenType type, String value) {
        if(type == null){
            throw new IllegalArgumentException("type must not be null");
        }
        this.type = type;
        this.value = value;
    }

    /**
     * Getter for type
     *
     * @return type
     */
    public QueryTokenType getType() {
        return type;
    }

    /**
     * Getter for value
     *
     * @return value
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "QueryToken{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}
