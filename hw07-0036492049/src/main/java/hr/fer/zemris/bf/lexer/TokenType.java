package hr.fer.zemris.bf.lexer;

/**
 * Enum for {@link Token} types
 *
 * @author Pavao Jerebić
 */
public enum TokenType {
    /**
     * End of file
     */
    EOF,
    /**
     * A variable
     */
    VARIABLE,
    /**
     * A constant<br/>
     * True ( or '1' ) , False ( or '0' ) ( not case sensitive )
     */
    CONSTANT,
    /**
     * Boolean operator<br/>
     * and ( or '*' ) , xor ( or ':+:' ) , or ( or '+' ) , not ( or '!' ) ( not case sensitive )
     */
    OPERATOR,
    /**
     * An open bracket, i.e. '('
     */
    OPEN_BRACKET,
    /**
     * A closed bracket, i.e. ')'
     */
    CLOSED_BRACKET
}
