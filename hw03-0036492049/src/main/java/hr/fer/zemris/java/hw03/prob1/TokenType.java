package hr.fer.zemris.java.hw03.prob1;

/**
 * Enum for token type<br/>
 * EOF, WORD, NUMBER, SYMBOL
 *
 * @author Pavao Jerebić
 */
public enum TokenType {
    /**
     * End of file
     */
    EOF,
    /**
     * Word as string
     */
    WORD,
    /**
     * Number in {@link Long} range
     */
    NUMBER,
    /**
     * Symbol as char
     */
    SYMBOL
}
