package hr.fer.zemris.java.hw04.db.lexer;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Pavao Jerebić
 */
public class QueryLexerTest {
    @Test
    public void testNotNull() {
        QueryLexer lexer = new QueryLexer("");

        assertNotNull("SmartScriptToken was expected but null was returned.", lexer.nextToken());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        // must throw!
        new QueryLexer(null);
    }

    @Test
    public void testEmpty() {
        QueryLexer lexer = new QueryLexer("");

        assertEquals("Empty input must generate only EOF token.", QueryTokenType.EOL, lexer.nextToken().getType());
    }

    @Test
    public void testManyTokens(){
        QueryLexer lexer = new QueryLexer("firstName = \"pero\" aNd jmbag > \"5\" and finalGrade <> \"666\" and lastName LIKE \"abba*\"");
        assertEquals(QueryTokenType.FIELD, lexer.nextToken().getType());
        assertEquals(QueryTokenType.OPERATOR, lexer.nextToken().getType());
        assertEquals(QueryTokenType.STRING, lexer.nextToken().getType());
        assertEquals(QueryTokenType.AND, lexer.nextToken().getType());
        assertEquals(QueryTokenType.FIELD, lexer.nextToken().getType());
        assertEquals(QueryTokenType.OPERATOR, lexer.nextToken().getType());
        assertEquals(QueryTokenType.STRING, lexer.nextToken().getType());
        assertEquals(QueryTokenType.AND, lexer.nextToken().getType());
        assertEquals(QueryTokenType.FIELD, lexer.nextToken().getType());
        assertEquals(QueryTokenType.OPERATOR, lexer.nextToken().getType());
        assertEquals(QueryTokenType.STRING, lexer.nextToken().getType());
        assertEquals(QueryTokenType.AND, lexer.nextToken().getType());
        assertEquals(QueryTokenType.FIELD, lexer.nextToken().getType());
        assertEquals(QueryTokenType.OPERATOR, lexer.nextToken().getType());
        assertEquals(QueryTokenType.STRING, lexer.nextToken().getType());
        assertEquals(QueryTokenType.EOL, lexer.nextToken().getType());
    }
}