package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Class that produces tokens from given string <br/>
 * 2 types of production <br/>
 * - TEXT : produces Word, Tag or EOF token <br/>
 * - TAG : produces String, Variable, Operator, Function, ConstantDouble, ConstantInteger, Tag or EOF tokens<br/>
 * Allowed escaping on:<br/>
 * - '\\' and '\{' in text<br/>
 * - '\\', '\"', '\n', '\r' and '\t' in tags<br/>
 * Program will automatically enter TAG type of token production when '{' token is produced, and it will go back to TEXT type when '}' is encountered<br/>
 * Word : any String(with blanks)<br/>
 * Tag : "{$" or "$}"<br/>
 * String : any String but with quotation marks<br/>
 * Function : starts with "@" followed by 1 letter and then are possible letters, numbers or underscores<br/>
 * Variable : starts with 1 letter and then are possible letters, numbers or underscores<br/>
 * Operator : one of '+', '-', '*', '/', '^'<br/>
 * - if exactly after operator '-' comes digit then it is processed as number instead as an operator<br/>
 * ConstantDouble : double<br/>
 * ConstantInteger: int<br/>
 * EOF: end of file<br/>
 *
 * @author Pavao Jerebić
 */
public class SmartScriptLexer {
    /**
     * data to be processed
     */
    private char[] data;
    /**
     * current token
     */
    private SmartScriptToken token;
    /**
     * current index on data array
     */
    private int currentIndex;

    /**
     * SmartScriptLexer state
     */
    private SmartScriptLexerState state;

    /**
     * Allowed operators
     */
    private static char[] operators;

    static {
        operators = new char[]{
                '+',
                '-',
                '*',
                '/',
                '^'
        };
    }

    /**
     * Constructor that takes string data as input char array
     * Sets state as TEXT
     *
     * @param data input data
     * @throws IllegalArgumentException if data is null
     */
    public SmartScriptLexer(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Input can't be null");
        }
        this.data = data.toCharArray();
        state = SmartScriptLexerState.TEXT;
    }

    /**
     * Returns next {@link SmartScriptToken} in data array
     *
     * @return nex token
     * @throws SmartScriptLexerException if input is invalid
     */
    public SmartScriptToken nextSmartScriptToken() {

        if (token != null && token.getType().equals(SmartScriptTokenType.EOF)) {
            throw new SmartScriptLexerException("No new tokens. You have reached the end.");
        }

        if (currentIndex >= data.length) {
            token = new SmartScriptToken(SmartScriptTokenType.EOF, null);
            return token;
        }

        if (state == SmartScriptLexerState.TEXT) {
            if (data[currentIndex] == '{') {
                token = processTag();
            } else {
                token = processText();
            }
        } else {
            if (Character.isLetter(data[currentIndex])) {

                token = processVariable();

            } else if (Character.isDigit(data[currentIndex])) {

                token = processNumber();

            } else if (String.valueOf(operators).contains(String.valueOf(data[currentIndex]))) {

                if (data[currentIndex] == '-' && currentIndex + 1 < data.length && Character.isDigit(data[currentIndex + 1])) {
                    currentIndex++;
                    SmartScriptToken token = processNumber();
                    if (token.getType() == SmartScriptTokenType.CONSTANTDOUBLE) {
                        this.token = new SmartScriptToken(SmartScriptTokenType.CONSTANTDOUBLE, -1.0 * (double) token.getValue());
                    } else {
                        this.token = new SmartScriptToken(SmartScriptTokenType.CONSTANTINTEGER, -1 * (int) token.getValue());
                    }
                } else {
                    token = new SmartScriptToken(SmartScriptTokenType.OPERATOR, data[currentIndex++]);
                }

            } else if (data[currentIndex] == '@') {

                token = processFunction();

            } else if (data[currentIndex] == '=') {

                token = new SmartScriptToken(SmartScriptTokenType.SYMBOL, data[currentIndex++]);

            } else if (data[currentIndex] == '"') {

                token = processString();

            } else if (isBlank(data[currentIndex])) {

                return skipBlanks();

            } else if (data[currentIndex] == '$') {

                token = processCloseTag();

            } else {

                throw new SmartScriptLexerException("Wrong input. " + " At position " + currentIndex);

            }

        }

        return token;

    }

    /**
     * Helping method to process closing tag "$}"
     *
     * @return token representing closed tag
     * @throws SmartScriptLexerException if tags are malformed
     */
    private SmartScriptToken processCloseTag() {
        if (currentIndex + 1 < data.length && data[currentIndex + 1] == '}') {
            currentIndex += 2;
            state = SmartScriptLexerState.TEXT;
            return new SmartScriptToken(SmartScriptTokenType.TAG, "$}");
        } else {
            throw new SmartScriptLexerException("Wrong input. Tags not properly closed" + " At position " + currentIndex);
        }
    }

    /**
     * Helping method to process string
     *
     * @return token representing string
     * @throws SmartScriptLexerException if escape sequence is wrong or if quotation marks are not closed
     */
    private SmartScriptToken processString() {
        StringBuilder sb = new StringBuilder();
        sb.append(data[currentIndex++]);
        while (currentIndex < data.length && data[currentIndex] != '"') {
            if (data[currentIndex] == '\\') {
                if (currentIndex + 1 < data.length && (data[currentIndex + 1] == '\\' || data[currentIndex + 1] == '"')) {
                    sb.append(data[++currentIndex]);
                    currentIndex++;
                } else {
                    throw new SmartScriptLexerException("Wrong input. Invalid escape sequence" + " At position " + currentIndex);
                }
            } else {
                sb.append(data[currentIndex++]);
            }
        }
        if (currentIndex >= data.length || data[currentIndex] != '"') {
            throw new SmartScriptLexerException("Wrong input. Quotation marks not closed" + " At position " + currentIndex);
        } else {
            sb.append(data[currentIndex++]);
            return new SmartScriptToken(SmartScriptTokenType.STRING, sb.toString());
        }
    }

    /**
     * Helping method for processing function<br/>
     * Starts with @ symbol then 1 or more letters numbers or underscores
     *
     * @return token representing function
     * @throws SmartScriptLexerException if function name is invalid
     */
    private SmartScriptToken processFunction() {
        StringBuilder sb = new StringBuilder();
        sb.append(data[currentIndex]);
        currentIndex++;
        if (currentIndex < data.length && Character.isLetter(data[currentIndex])) {
            sb.append(processVariable().getValue());
        } else {
            throw new SmartScriptLexerException("Wrong input. Invalid variable name" + " At position " + currentIndex);
        }
        return new SmartScriptToken(SmartScriptTokenType.FUNCTION, sb.toString());
    }

    /**
     * Helping method that processes number(double and integer)
     *
     * @return token representing double or integer
     * @throws SmartScriptLexerException if number is invalid
     */
    private SmartScriptToken processNumber() {
        StringBuilder sb = new StringBuilder();
        while (currentIndex < data.length
                && (Character.isDigit(data[currentIndex]) || data[currentIndex] == '.')) {
            if (data[currentIndex] == '.' && sb.toString().contains(".")) {
                throw new SmartScriptLexerException("Wrong input. Invalid double value");
            } else {
                sb.append(data[currentIndex++]);
            }
        }
        try {
            if (sb.toString().contains(".")) {
                return new SmartScriptToken(SmartScriptTokenType.CONSTANTDOUBLE, Double.parseDouble(sb.toString()));
            } else {
                return new SmartScriptToken(SmartScriptTokenType.CONSTANTINTEGER, Integer.parseInt(sb.toString()));
            }
        } catch (NumberFormatException ex) {
            throw new SmartScriptLexerException("Wrong input. Can't parse number" + " At position " + currentIndex);
        }
    }

    /**
     * Helping method to process text<br/>
     * Includes spaces
     *
     * @return token representing text
     * @throws SmartScriptLexerException if escape sequence is malformed
     */
    private SmartScriptToken processText() {
        StringBuilder sb = new StringBuilder();
        while (currentIndex < data.length && data[currentIndex] != '{') {
            if (data[currentIndex] == '\\') {
                if ((currentIndex + 1) < data.length
                        && (data[currentIndex + 1] == '{' || data[currentIndex + 1] == '\\')) {

                    sb.append(data[++currentIndex]);
                    currentIndex++;

                } else {
                    throw new SmartScriptLexerException("Wrong input. Invalid escape sequence" + " At position " + currentIndex);
                }

            } else {
                sb.append(data[currentIndex++]);
            }
        }
        return new SmartScriptToken(SmartScriptTokenType.WORD, sb.toString());
    }

    /**
     * Helping method to process opening tag "{$"
     *
     * @return token representing opening tag
     * @throws SmartScriptLexerException if tag is malformed
     */
    private SmartScriptToken processTag() {
        StringBuilder sb = new StringBuilder();
        sb.append(data[currentIndex]);
        if (currentIndex + 1 < data.length && data[currentIndex + 1] == '$') {
            sb.append(data[currentIndex + 1]);
            setState(SmartScriptLexerState.TAG);
            currentIndex += 2;
            return new SmartScriptToken(SmartScriptTokenType.TAG, sb.toString());
        } else {
            throw new SmartScriptLexerException("Tags not formed properly" + " At position " + currentIndex);
        }
    }

    /**
     * Helping method to process variable <br/>
     * Allowed form is first letter then letters numbers or underscores
     *
     * @return token representing variable
     * @throws SmartScriptLexerException if variable is malformed
     */
    private SmartScriptToken processVariable() {
        StringBuilder sb = new StringBuilder();
        while (currentIndex < data.length
                && (Character.isLetter(data[currentIndex])
                || Character.isDigit(data[currentIndex])
                || data[currentIndex] == '_')) {

            sb.append(data[currentIndex++]);
        }
        return new SmartScriptToken(SmartScriptTokenType.VARIABLE, sb.toString());
    }

    /**
     * Helping method that skips blanks and calls nextSmartScriptToken on data array
     *
     * @return same as nextToken after skipping blanks
     */
    private SmartScriptToken skipBlanks() {
        while (currentIndex < data.length && isBlank(data[currentIndex])) {
            currentIndex++;
        }
        return nextSmartScriptToken();
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
    public SmartScriptToken getSmartScriptToken() {
        return token;
    }

    /**
     * Setter for lexer state
     *
     * @param state lexer state
     * @throws IllegalArgumentException if given state is null
     */
    public void setState(SmartScriptLexerState state) {

        if (state == null) {
            throw new IllegalArgumentException("Input can't be null" + " At position " + currentIndex);
        }
        this.state = state;
    }


}