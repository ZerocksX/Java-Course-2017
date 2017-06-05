package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Simple token class that has {@link SmartScriptTokenType} and value
 *
 * @author Pavao Jerebić
 */
public class SmartScriptToken {

    /**
     * Type of the token
     */
    private SmartScriptTokenType type;

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
    public SmartScriptToken(SmartScriptTokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    /**
     * getter for token type
     *
     * @return token type
     */
    public SmartScriptTokenType getType() {
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

    @Override
    public String toString() {
        return "SmartScriptToken{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }
}
