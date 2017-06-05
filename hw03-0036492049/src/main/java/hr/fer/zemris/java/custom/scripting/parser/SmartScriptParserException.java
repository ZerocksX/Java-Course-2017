package hr.fer.zemris.java.custom.scripting.parser;

/**
 * It is thrown if there is an exception in {@link SmartScriptParser#parse()}
 *
 * @author Pavao Jerebić
 */
public class SmartScriptParserException extends RuntimeException {
    /**
     * Constructor
     */
    public SmartScriptParserException() {
    }

    /**
     * Constructor with message
     *
     * @param s message
     */
    public SmartScriptParserException(String s) {
        super(s);
    }
}
