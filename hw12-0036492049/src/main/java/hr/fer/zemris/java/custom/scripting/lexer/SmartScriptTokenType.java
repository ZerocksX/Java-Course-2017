package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enum for {@link SmartScriptToken} type<br/>
 * Word, String, Variable, Operator, Function, ConstantDouble, ConstantInteger, Tag, Symbol or EOF<br/>
 * Characteristics are described in {@link SmartScriptLexer}
 *
 * @author Pavao Jerebić
 */
public enum SmartScriptTokenType {
    /**
     * Everything outside of tags
     */
    WORD,
    /**
     * String
     */
    STRING,
    /**
     * Variable
     */
    VARIABLE,
    /**
     * Operator
     */
    OPERATOR,
    /**
     * Function
     */
    FUNCTION,
    /**
     * Double
     */
    CONSTANTDOUBLE,
    /**
     * Integer
     */
    CONSTANTINTEGER,
    /**
     * Tag opener or closer
     */
    TAG,
    /**
     * "="
     */
    SYMBOL,
    /**
     * End of file
     */
    EOF

}
