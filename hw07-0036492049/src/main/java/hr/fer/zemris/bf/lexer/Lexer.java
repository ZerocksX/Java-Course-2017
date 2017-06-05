package hr.fer.zemris.bf.lexer;

/**
 * Lexer that produces tokens from a given string representation of a boolean expression<br/>
 * It can produce tokens of {@link TokenType}
 *
 * @author Pavao Jerebić
 */
public class Lexer {

    /**
     * Data array
     */
    private char[] data;
    /**
     * Current index
     */
    private int current;
    /**
     * Current token
     */
    private Token token;

    /**
     * Constructor that accepts given expression
     *
     * @param expression boolean expression
     */
    public Lexer(String expression) {
        data = expression.toCharArray();
        current = 0;
    }

    /**
     * Produces a next token
     *
     * @return a new token
     * @throws LexerException if there are no more tokens, or if any expression is malformed
     */
    public Token nextToken() {

        if (token != null && token.getTokenType() == TokenType.EOF) {
            throw new LexerException("No more tokens");
        }


        skipWhitespace();

        if (current >= data.length) {
            token = new Token(TokenType.EOF, null);
            return token;
        }


        Character firstChar = data[current];

        if (Character.isLetter(firstChar)) {

            String string = produceIdentificator().toLowerCase();

            switch (string) {
                case "and":
                    token = new Token(TokenType.OPERATOR, string);
                    break;
                case "xor":
                    token = new Token(TokenType.OPERATOR, string);
                    break;
                case "or":
                    token = new Token(TokenType.OPERATOR, string);
                    break;
                case "not":
                    token = new Token(TokenType.OPERATOR, string);
                    break;

                case "true":
                    token = new Token(TokenType.CONSTANT, true);
                    break;
                case "false":
                    token = new Token(TokenType.CONSTANT, false);
                    break;

                default:
                    token = new Token(TokenType.VARIABLE, string.toUpperCase());
                    break;
            }
        } else if (Character.isDigit(firstChar)) {
            String string = produceNumberArray();
            switch (string) {
                case "1":
                    token = new Token(TokenType.CONSTANT, true);
                    break;
                case "0":
                    token = new Token(TokenType.CONSTANT, false);
                    break;
                default:
                    throw new LexerException("Invalid number array at position " + current);
            }
        } else {
            String string = produceSymbol();
            switch (string) {
                case "*":
                    token = new Token(TokenType.OPERATOR, "and");
                    break;
                case ":+:":
                    token = new Token(TokenType.OPERATOR, "xor");
                    break;
                case "+":
                    token = new Token(TokenType.OPERATOR, "or");
                    break;
                case "!":
                    token = new Token(TokenType.OPERATOR, "not");
                    break;

                case "(":
                    token = new Token(TokenType.OPEN_BRACKET, '(');
                    break;
                case ")":
                    token = new Token(TokenType.CLOSED_BRACKET, ')');
                    break;

                default:
                    throw new LexerException("Invalid symbol array at position " + current);
            }
        }


        return token;
    }

    /**
     * Helping method that produces an identificator<br/>
     * This is called when first character is a letter
     *
     * @return string representation af an identificator
     * @throws LexerException if identificator is not consisted only of letters, digits or '_'
     */
    private String produceIdentificator() throws LexerException {

        StringBuilder sb = new StringBuilder();

        skipWhitespace();

        while (current < data.length && !Character.isWhitespace(data[current]) && !isProperSymbol(data[current])) {
            if (!Character.isLetterOrDigit(data[current]) && data[current] != '_') {
                throw new LexerException("Invalid character at position " + current);
            }
            sb.append(data[current]);
            current++;
        }

        return sb.toString();
    }

    /**
     * Helping method that produces a number array
     *
     * @return string representation of a number array
     * @throws LexerException if number array is consisted of anything other then a digit
     */
    private String produceNumberArray() throws LexerException {

        StringBuilder sb = new StringBuilder();

        skipWhitespace();

        while (current < data.length && !Character.isWhitespace(data[current]) && !isProperSymbol(data[current])) {
            if (!Character.isDigit(data[current])) {
                throw new LexerException("Invalid character at position " + current);
            }
            sb.append(data[current]);
            current++;
        }

        return sb.toString();
    }

    /**
     * Helping method that produces a string representation of a symbol<br/>
     * A symbol can be a combination of symbols '*', '+', '!', ':', '(', ')';
     *
     * @return a string representation of a symbol array
     */
    private String produceSymbol() {

        StringBuilder sb = new StringBuilder();

        skipWhitespace();

        while (current < data.length
                && !Character.isWhitespace(data[current])
                && isProperSymbol(data[current])) {
            sb.append(data[current]);
            current++;
        }

        return sb.toString();
    }

    /**
     * Skips all whitespaces
     */
    private void skipWhitespace() {
        while (current < data.length && Character.isWhitespace(data[current])) {
            current++;
        }
    }

    /**
     * Checks if character is a proper symbol
     *
     * @param c a character
     * @return true if c is a proper symbol
     */
    private boolean isProperSymbol(char c) {
        return c == '*'
                || c == '+'
                || c == '!'
                || c == ':'
                || c == '('
                || c == ')';
    }

    /**
     * Getter for current token
     *
     * @return current token
     */
    public Token getToken() {
        return token;
    }
}
