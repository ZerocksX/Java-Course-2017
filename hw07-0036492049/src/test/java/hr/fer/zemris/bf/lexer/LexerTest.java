package hr.fer.zemris.bf.lexer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Pavao Jerebić
 */
public class LexerTest {

    @Test
    public void test00() throws Exception {

        Token[] tokens = new Token[]{
                new Token(TokenType.OPEN_BRACKET, '('),
                new Token(TokenType.VARIABLE, "A"),
                new Token(TokenType.OPERATOR, "or"),
                new Token(TokenType.VARIABLE, "B"),
                new Token(TokenType.CLOSED_BRACKET, ')'),
                new Token(TokenType.OPERATOR, "xor"),
                new Token(TokenType.OPEN_BRACKET, '('),
                new Token(TokenType.VARIABLE, "C"),
                new Token(TokenType.OPERATOR, "or"),
                new Token(TokenType.VARIABLE, "D"),
                new Token(TokenType.CLOSED_BRACKET, ')'),
                new Token(TokenType.EOF, null)
        };

        Lexer lexer = new Lexer("(a + b) xor (c or d)");
        Token token = null;
        int i = 0;
        do {
            token = lexer.nextToken();
            assertEquals(tokens[i++], token);
        } while (token.getTokenType() != TokenType.EOF);
    }

    @Test
    public void test01() throws Exception {
        Token[] tokens = new Token[]{
                new Token(TokenType.VARIABLE, "A"),
                new Token(TokenType.OPERATOR, "or"),
                new Token(TokenType.VARIABLE, "B"),
                new Token(TokenType.EOF, null)
        };

        Lexer lexer = new Lexer("A Or b");
        Token token = null;
        int i = 0;
        do {
            token = lexer.nextToken();
            assertEquals(tokens[i++], token);
        } while (token.getTokenType() != TokenType.EOF);
    }

    @Test
    public void test02() throws Exception {

        Token[] tokens = new Token[]{
                new Token(TokenType.OPEN_BRACKET, '('),
                new Token(TokenType.VARIABLE, "A"),
                new Token(TokenType.OPERATOR, "or"),
                new Token(TokenType.VARIABLE, "B"),
                new Token(TokenType.CLOSED_BRACKET, ')'),
                new Token(TokenType.OPERATOR, "xor"),
                new Token(TokenType.VARIABLE, "C"),
                new Token(TokenType.EOF, null)
        };

        Lexer lexer = new Lexer("(a + b) :+: C");
        Token token = null;
        int i = 0;
        do {
            token = lexer.nextToken();
            assertEquals(tokens[i++], token);
        } while (token.getTokenType() != TokenType.EOF);
    }

    @Test(expected = LexerException.class)
    public void test03() throws Exception {

        Lexer lexer = new Lexer("A Or 9a");
        Token token = null;
        int i = 0;
        do {
            token = lexer.nextToken();
        } while (token.getTokenType() != TokenType.EOF);
    }
}