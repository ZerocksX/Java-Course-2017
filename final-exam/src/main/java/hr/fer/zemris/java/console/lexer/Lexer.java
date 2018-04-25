package hr.fer.zemris.java.console.lexer;

/**
 * @author Pavao Jerebić
 */
public class Lexer {
    private char[] data;
    private Token token;
    private int index;
    private static char lineSplitter = System.lineSeparator().charAt(0);
    private boolean isNewLine = true;

    public Lexer(char[] data) {
        this.data = data;
    }

    public Token nextToken() {
        if (token != null && token.getType() == TokenType.EOF) {
            return null;
        }
        if (index >= data.length) {
            token = new Token(TokenType.EOF, String.valueOf(index));
            return token;
        }

        skipBlanks();

        if (data[index] == '#') {
            if(!isNewLine){
                throw new RuntimeException();
            }
            index++;
            token = new Token(TokenType.DIRECTIVE, "#" + parseAlfa());
            isNewLine = false;
            return token;
        }

        if (data[index] == '$') {
            index++;
            token = new Token(TokenType.VARIABLE, "$" + parseAlfaNum());
            return token;
        }

        if (data[index] == ';') {
            token = new Token(TokenType.COLON, ";");
            index++;
            return token;
        }

        if (Character.isLetter(data[index])) {
            token = new Token(TokenType.NAME, parseAlfaNum());
            return token;
        }

        switch (data[index]) {
            case '+':
                token = new Token(TokenType.OPERATOR, "+");
                index++;
                return token;
            case '-':
                token = new Token(TokenType.OPERATOR, "-");
                index++;
                return token;
            case '/':
                token = new Token(TokenType.OPERATOR, "/");
                index++;
                return token;
            case '*':
                token = new Token(TokenType.OPERATOR, "*");
                index++;
                return token;
            case '=':
                token = new Token(TokenType.OPERATOR, "=");
                index++;
                return token;

        }

        if(data[index] == lineSplitter){
            index++;
            isNewLine = true;
            return nextToken();
        }

        if(isNewLine){
            token = new Token(TokenType.EOF, String.valueOf(index));
            return token;
        }

        throw new RuntimeException("unknown char");

    }

    private String parseAlfa() {
        StringBuilder sb = new StringBuilder();
        while (Character.isLetter(data[index]) && index < data.length) {
            sb.append(data[index]);
            index++;
        }
        return sb.toString();
    }

    private String parseAlfaNum() {
        StringBuilder sb = new StringBuilder();
        while (Character.isLetterOrDigit(data[index]) && index < data.length) {
            sb.append(data[index]);
            index++;
        }
        return sb.toString();
    }

    private void skipBlanks() {
        while (index < data.length && isBlank(data[index])) {
            index++;
        }
    }

    private boolean isBlank(char c) {
        return c == ' ' || c == '\t';
    }

    public Token getToken() {
        return token;
    }
}
