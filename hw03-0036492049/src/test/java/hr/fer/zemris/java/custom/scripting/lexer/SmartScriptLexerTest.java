package hr.fer.zemris.java.custom.scripting.lexer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;


public class SmartScriptLexerTest {

    @Test
    public void testNotNull() {
        SmartScriptLexer lexer = new SmartScriptLexer("");

        assertNotNull("SmartScriptToken was expected but null was returned.", lexer.nextSmartScriptToken());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        // must throw!
        new SmartScriptLexer(null);
    }

    @Test
    public void testEmpty() {
        SmartScriptLexer lexer = new SmartScriptLexer("");

        assertEquals("Empty input must generate only EOF token.", SmartScriptTokenType.EOF, lexer.nextSmartScriptToken().getType());
    }

    @Test
    public void testCompleteDocument() {
        SmartScriptLexer lexer = new SmartScriptLexer("This is sample text.\n" +
                "{$ FOR i 1 10 1 $}\n" +
                " This is {$= i $}-th time this message is generated.\n" +
                "{$END$}\n" +
                "{$FOR i 0 10 2 $}\n" +
                " sin({$=i$}^2) = {$= i i * @sin \"0.000\" @decfmt $}\n" +
                "{$END$}");

        while (lexer.nextSmartScriptToken().getType() != SmartScriptTokenType.EOF) {
            System.out.println(String.format("Type: %s; Value: %s", lexer.getSmartScriptToken().getType(), lexer.getSmartScriptToken().getValue()));
        }

        Assert.assertEquals("Type: WORD; Value: This is sample text.\n" +
                "\n" +
                "Type: TAG; Value: {$\n" +
                "Type: VARIABLE; Value: FOR\n" +
                "Type: VARIABLE; Value: i\n" +
                "Type: CONSTANTINTEGER; Value: 1\n" +
                "Type: CONSTANTINTEGER; Value: 10\n" +
                "Type: CONSTANTINTEGER; Value: 1\n" +
                "Type: TAG; Value: $}\n" +
                "Type: WORD; Value: \n" +
                " This is \n" +
                "Type: TAG; Value: {$\n" +
                "Type: SYMBOL; Value: =\n" +
                "Type: VARIABLE; Value: i\n" +
                "Type: TAG; Value: $}\n" +
                "Type: WORD; Value: -th time this message is generated.\n" +
                "\n" +
                "Type: TAG; Value: {$\n" +
                "Type: VARIABLE; Value: END\n" +
                "Type: TAG; Value: $}\n" +
                "Type: WORD; Value: \n" +
                "\n" +
                "Type: TAG; Value: {$\n" +
                "Type: VARIABLE; Value: FOR\n" +
                "Type: VARIABLE; Value: i\n" +
                "Type: CONSTANTINTEGER; Value: 0\n" +
                "Type: CONSTANTINTEGER; Value: 10\n" +
                "Type: CONSTANTINTEGER; Value: 2\n" +
                "Type: TAG; Value: $}\n" +
                "Type: WORD; Value: \n" +
                " sin(\n" +
                "Type: TAG; Value: {$\n" +
                "Type: SYMBOL; Value: =\n" +
                "Type: VARIABLE; Value: i\n" +
                "Type: TAG; Value: $}\n" +
                "Type: WORD; Value: ^2) = \n" +
                "Type: TAG; Value: {$\n" +
                "Type: SYMBOL; Value: =\n" +
                "Type: VARIABLE; Value: i\n" +
                "Type: VARIABLE; Value: i\n" +
                "Type: OPERATOR; Value: *\n" +
                "Type: FUNCTION; Value: @sin\n" +
                "Type: STRING; Value: \"0.000\"\n" +
                "Type: FUNCTION; Value: @decfmt\n" +
                "Type: TAG; Value: $}\n" +
                "Type: WORD; Value: \n" +
                "\n" +
                "Type: TAG; Value: {$\n" +
                "Type: VARIABLE; Value: END\n" +
                "Type: TAG; Value: $}\n",outContent.toString());

    }

    @Test
    public void testQuoteEscaping() {
        SmartScriptLexer lexer = new SmartScriptLexer("A tag follows {$= \"Joe \\\"Long\\\" Smith\"$}");

        assertEquals("A tag follows ", lexer.nextSmartScriptToken().getValue());
        assertEquals("{$", lexer.nextSmartScriptToken().getValue());
        assertEquals('=', lexer.nextSmartScriptToken().getValue());
        assertEquals("\"Joe \"Long\" Smith\"", lexer.nextSmartScriptToken().getValue());
        assertEquals("$}", lexer.nextSmartScriptToken().getValue());


    }

    @Test
    public void testTagEscaping() {
        SmartScriptLexer lexer = new SmartScriptLexer("\\{$=1$}. Now actually write one {$=1$}");

        assertEquals("{$=1$}. Now actually write one ", lexer.nextSmartScriptToken().getValue());
        assertEquals("{$", lexer.nextSmartScriptToken().getValue());
        assertEquals('=', lexer.nextSmartScriptToken().getValue());
        assertEquals(1, lexer.nextSmartScriptToken().getValue());
        assertEquals("$}", lexer.nextSmartScriptToken().getValue());

    }

    @Test(expected = SmartScriptLexerException.class)
    public void testWrongTags() {
        SmartScriptLexer lexer = new SmartScriptLexer("\\{$=1$}. Now actually write one {$=1}");
        while (lexer.nextSmartScriptToken().getType() != SmartScriptTokenType.EOF) ;
        fail();
    }

    @Test
    public void testNegativeNumber() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$=1-2.3$}");
        assertEquals("{$", lexer.nextSmartScriptToken().getValue());
        assertEquals('=', lexer.nextSmartScriptToken().getValue());
        assertEquals(1, lexer.nextSmartScriptToken().getValue());
        assertEquals(-2.3, lexer.nextSmartScriptToken().getValue());
        assertEquals("$}", lexer.nextSmartScriptToken().getValue());
    }

    @Test
    public void testStringEscaping() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$= \"\\\\ 5 d\"$}");
        assertEquals("{$", lexer.nextSmartScriptToken().getValue());
        assertEquals('=', lexer.nextSmartScriptToken().getValue());
        assertEquals("\"\\ 5 d\"", lexer.nextSmartScriptToken().getValue());
        assertEquals("$}", lexer.nextSmartScriptToken().getValue());
    }

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }
}
