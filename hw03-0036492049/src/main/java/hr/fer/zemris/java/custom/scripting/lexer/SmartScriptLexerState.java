package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Lexer states to control lexer behavior as described in {@link SmartScriptLexer}
 *
 * @author Pavao Jerebić
 */
public enum SmartScriptLexerState {
    /**
     * TEXT state as described in {@link SmartScriptLexer}
     */
    TEXT,
    /**
     * TAG state as described in {@link SmartScriptLexer}
     */
    TAG
}
