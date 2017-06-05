package hr.fer.zemris.java.hw03.prob1;


/**
 * Class that produces tokens from given string <br/>
 * 2 types of production <br/>
 * - BASIC : produces Word, Number, Symbol or EOF token ( escape sequences are allowed on digits and '\' using '\' char)<br/>
 * - EXTENDED : produces only Word tokens ( escape sequences are not allowed )<br/>
 * Program will automatically enter extended type of token production when '#' token is produced, and it will go back to Basic type when it is encountered again
 *
 * @author Pavao Jerebić
 */
public class Lexer {
    /**
     * data to be processed
     */
    private char[] data;
    /**
     * current token
     */
    private Token token;
    /**
     * current index on data array
     */
    private int currentIndex;

    /**
     * Lexer state
     */
    private LexerState state;

    /**
     * Constructor that takes string data as input char array
     * Sets state as BASIC
     *
     * @param data input data
     * @throws IllegalArgumentException if data is null
     */
    public Lexer(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Input can't be null");
        }
        this.data = data.toCharArray();
        state = LexerState.BASIC;
    }

    /**
     * Returns next {@link Token} in data array
     *
     * @return nex token
     * @throws LexerException if input is invalid
     */
    public Token nextToken() {

        if (token != null && token.getType().equals(TokenType.EOF)) {
            throw new LexerException("No new tokens. You have reached the end.");
        }

        if (currentIndex == data.length) {
            token = new Token(TokenType.EOF, null);
            return token;
        }

        if (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\') {

            token = state == LexerState.BASIC ? processWord() : processWordExtended();

        } else if (Character.isDigit(data[currentIndex])) {

            token = state == LexerState.BASIC ? processNumber() : processWordExtended();

        } else if (isBlank(data[currentIndex])) {

            return skipBlanks();

        } else if (state == LexerState.EXTENDED && data[currentIndex] != '#') {

            token = processWordExtended();

        } else {
            token = processSymbol();

        }

        return token;
    }

    /**
     * Helping method that produces {@link Token} of {@link TokenType#WORD} type wi
     *
     * @return Word type {@link Token}
     */
    private Token processWordExtended() {
        StringBuilder value = new StringBuilder();

        while (currentIndex < data.length && !isBlank(data[currentIndex]) && data[currentIndex] != '#') {
            value.append(data[currentIndex++]);
        }

        return new Token(TokenType.WORD, value.toString());
    }

    /**
     * Helping method that skips blanks and calls nextToken on data array
     *
     * @return same as nextToken after skipping blanks
     */
    private Token skipBlanks() {
        while (currentIndex < data.length && isBlank(data[currentIndex])) {
            currentIndex++;
        }
        return nextToken();
    }

    /**
     * Helping method that produces {@link Token} of {@link TokenType#NUMBER} type
     *
     * @return Number type {@link Token}
     * @throws LexerException if input is invalid
     */
    private Token processNumber() {
        StringBuilder value = new StringBuilder();

        while (currentIndex < data.length && Character.isDigit(data[currentIndex])) {
            value.append(data[currentIndex++]);
        }

        try {
            return new Token(TokenType.NUMBER, Long.parseLong(value.toString()));
        } catch (NumberFormatException ex) {
            throw new LexerException("Wrong input. Number out of range");
        }

    }

    /**
     * Helping method that produces {@link Token} of {@link TokenType#WORD} type
     *
     * @return Word type {@link Token}
     * @throws LexerException if input is invalid
     */
    private Token processWord() {
        StringBuilder value = new StringBuilder();

        while (currentIndex < data.length && (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\')) {
            if (data[currentIndex] == '\\') {
                if ((currentIndex + 1) < data.length
                        && (Character.isDigit(data[currentIndex + 1]) || data[currentIndex + 1] == '\\')) {

                    value.append(data[++currentIndex]);
                    currentIndex++;

                } else {

                    throw new LexerException("Wrong input. Invalid escape sequence");
                }

            } else {
                value.append(data[currentIndex++]);
            }
        }

        return new Token(TokenType.WORD, value.toString());
    }


    /**
     * Helping method that produces {@link Token} of {@link TokenType#SYMBOL} type
     *
     * @return Symbol type {@link Token}
     */
    private Token processSymbol() {
        Token token = new Token(TokenType.SYMBOL, data[currentIndex++]);
        if (token.getValue().equals('{') || token.getValue().equals('}')) {
            if (state == LexerState.EXTENDED) {
                setState(LexerState.BASIC);
            } else {
                setState(LexerState.EXTENDED);
            }
        }
        return token;
    }

    /**
     * Returns true if given char is blank i.e. '\r','\n', '\t' or ' '
     *
     * @param c given char
     * @return true if char is blank, false otherwise
     */
    private boolean isBlank(char c) {
        return c == '\r' || c == '\n' || c == '\t' || c == ' ';
    }


    /**
     * Simple getter for current token
     *
     * @return current token
     */
    public Token getToken() {
        return token;
    }

    /**
     * Setter for lexer state
     *
     * @param state lexer state
     * @throws IllegalArgumentException if given state is null
     */
    public void setState(LexerState state) {

        if (state == null) {
            throw new IllegalArgumentException("Input can't be null");
        }
        this.state = state;
    }
}
