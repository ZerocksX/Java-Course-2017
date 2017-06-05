package hr.fer.zemris.java.hw04.db.lexer;

/**
 * Class that produces {@link QueryToken}<br/>
 * Takes input as string and then produces tokens:<br/>
 * {@link QueryTokenType#FIELD}, {@link QueryTokenType#AND}, {@link QueryTokenType#OPERATOR}, {@link QueryTokenType#STRING} and {@link QueryTokenType#EOL} <br/>
 * It does not support escaping<br/>
 *
 * @author Pavao Jerebić
 */
public class QueryLexer {
    /**
     * data to be processed
     */
    private char[] data;
    /**
     * current token
     */
    private QueryToken token;
    /**
     * current index on data array
     */
    private int currentIndex;

    /**
     * Constructor that takes string data as input char array
     *
     * @param data input data
     * @throws IllegalArgumentException if data is null
     */
    public QueryLexer(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Input can't be null");
        }
        this.data = data.toCharArray();
    }

    /**
     * Returns next {@link QueryToken} in data array
     *
     * @return next token
     * @throws QueryLexerException if input is invalid
     */
    public QueryToken nextToken() {
        if (token != null && token.getType().equals(QueryTokenType.EOL)) {
            throw new QueryLexerException("No new tokens. You have reached the end.");
        }


        skipBlanks();

        if (currentIndex >= data.length) {
            token = new QueryToken(QueryTokenType.EOL, null);
            return token;
        }

        if (Character.isLetter(data[currentIndex])) {
            try {
                processWord();

            } catch (IllegalStateException ex) {
                throw new QueryLexerException("Not a proper word.\nAt position: " + currentIndex);
            }
        } else if (data[currentIndex] == '\"') {

            String value = processString();
            token = new QueryToken(QueryTokenType.STRING, value);

        } else {
            try {
                token = processBasicOperator();
            } catch (IllegalStateException ex) {
                throw new QueryLexerException("Not a proper word.\nAt position: " + currentIndex);
            }
        }

        return token;

    }

    /**
     * Can produce LIKE operator, and word and field name
     *
     * @throws IllegalStateException if any are not properly formed
     */
    private void processWord() throws IllegalStateException {

        if (satisfiesOffsetAndExpression(4, "LIKE", true)) {

            token = new QueryToken(QueryTokenType.OPERATOR, "LIKE");
            currentIndex += 4;

        } else if (satisfiesOffsetAndExpression(5, "jmbag", true)) {

            token = new QueryToken(QueryTokenType.FIELD, "jmbag");
            currentIndex += 5;

        } else if (satisfiesOffsetAndExpression(9, "firstName", true)) {

            token = new QueryToken(QueryTokenType.FIELD, "firstName");
            currentIndex += 9;

        } else if (satisfiesOffsetAndExpression(8, "lastName", true)) {

            token = new QueryToken(QueryTokenType.FIELD, "lastName");
            currentIndex += 8;

        } else if (satisfiesOffsetAndExpression(10, "finalGrade", true)) {

            token = new QueryToken(QueryTokenType.FIELD, "finalGrade");
            currentIndex += 10;

        } else if (satisfiesOffsetAndExpression(3, "and", false)) {

            token = new QueryToken(QueryTokenType.AND, "and");
            currentIndex += 3;

        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Processes basic operators
     *
     * @return next QueryToken from data array
     * @throws IllegalStateException if next word is not {@link QueryTokenType#OPERATOR}
     */
    private QueryToken processBasicOperator() throws IllegalStateException {
        if (satisfiesOffsetAndExpression(2, "<>", false)) {

            currentIndex += 2;
            return new QueryToken(QueryTokenType.OPERATOR, "<>");

        } else if (satisfiesOffsetAndExpression(2, "<=", false)) {

            currentIndex += 2;
            return new QueryToken(QueryTokenType.OPERATOR, "<=");

        } else if (satisfiesOffsetAndExpression(2, ">=", false)) {

            currentIndex += 2;
            return new QueryToken(QueryTokenType.OPERATOR, ">=");

        } else if (satisfiesOffsetAndExpression(1, "<", false)) {

            currentIndex += 1;
            return new QueryToken(QueryTokenType.OPERATOR, "<");

        } else if (satisfiesOffsetAndExpression(1, ">", false)) {

            currentIndex += 1;
            return new QueryToken(QueryTokenType.OPERATOR, ">");

        } else if (satisfiesOffsetAndExpression(1, "=", false)) {

            currentIndex += 1;
            return new QueryToken(QueryTokenType.OPERATOR, "=");

        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Creates a string<br/>
     * Parses everything from one quotation mark to next
     *
     * @return processed string
     * @throws QueryLexerException if has more than 1 wildcard
     */
    private String processString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        currentIndex++;
        boolean hasWildCard = false;
        while (currentIndex < data.length && data[currentIndex] != '\"') {
            if (data[currentIndex] == '*') {
                if (hasWildCard) {
                    throw new QueryLexerException("More than one wildcard character.\nAt position: " + currentIndex);
                } else {
                    hasWildCard = true;
                }
            }
            sb.append(data[currentIndex]);
            currentIndex++;
        }
        if (currentIndex < data.length && data[currentIndex] == '\"') {
            sb.append("\"");
            currentIndex++;
        } else {
            throw new QueryLexerException("Quotation marks not closed.\nAt position: " + currentIndex);
        }
        return sb.toString();
    }

    /**
     * Checks if data array at current index satisfies offset and expression
     *
     * @param offset        offset
     * @param expression    expression
     * @param caseSensitive is expression case sensitive
     * @return true if satisfies
     */
    private boolean satisfiesOffsetAndExpression(int offset, String expression, boolean caseSensitive) {
        return !caseSensitive
                ? (currentIndex + offset < data.length && (new String(data, currentIndex, offset)).toUpperCase().equals(expression.toUpperCase()))
                : (currentIndex + offset < data.length && (new String(data, currentIndex, offset)).equals(expression));
    }

    /**
     * Skips blanks
     */
    private void skipBlanks() {
        while (currentIndex < data.length && isBlank(data[currentIndex])) {
            currentIndex++;
        }
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
     * Getter for current token
     *
     * @return current token
     */
    public QueryToken getToken() {
        return token;
    }
}
