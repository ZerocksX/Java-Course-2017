package hr.fer.zemris.java.custom.scripting.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Pavao Jerebić
 */
public class SmartScriptParserTest {

    @Test
    public void testBasicDocument() {
        SmartScriptParser parser = new SmartScriptParser("This is sample text." +
                "{$ FOR i 1 10 1 $}" +
                " This is {$= i $}-th time this message is generated." +
                "{$END$}/n}" +
                "{$FOR i 0 10 2 $}" +
                " sin({$=i$}^2) = {$= i i * @sin \"0.000\" @decfmt $}" +
                "{$END$}");

        parser.printTree();
        assertEquals("DocumentNode{}\n" +
                "\tTextNode{text='This is sample text.'}\n" +
                "\tForLoopNode{variable=i, startExpression=1, endExpression=10, stepExpression=1}\n" +
                "\t\tTextNode{text=' This is '}\n" +
                "\t\tEchoNode{elements= i }\n" +
                "\t\tTextNode{text='-th time this message is generated.'}\n" +
                "\tTextNode{text='/n}'}\n" +
                "\tForLoopNode{variable=i, startExpression=0, endExpression=10, stepExpression=2}\n" +
                "\t\tTextNode{text=' sin('}\n" +
                "\t\tEchoNode{elements= i }\n" +
                "\t\tTextNode{text='^2) = '}\n" +
                "\t\tEchoNode{elements= i i * @sin \"0.000\" @decfmt }\n", outContent.toString());
    }

    @Test
    public void testStringEscaping() {
        SmartScriptParser parser = new SmartScriptParser("A tag follows {$= \"Joe \\\"Long\\\" Smith\"$}.");
        parser.printTree();
        assertEquals("DocumentNode{}\n" +
                "\tTextNode{text='A tag follows '}\n" +
                "\tEchoNode{elements= \"Joe \"Long\" Smith\" }\n" +
                "\tTextNode{text='.'}\n", outContent.toString());

    }

    @Test
    public void testTextEscaping() {
        SmartScriptParser parser = new SmartScriptParser("\\{$=1$}. Now actually write one {$=1$}");
        parser.printTree();
        assertEquals("DocumentNode{}\n" +
                "\tTextNode{text='{$=1$}. Now actually write one '}\n" +
                "\tEchoNode{elements= 1 }\n", outContent.toString());
    }

    @Test(expected = SmartScriptParserException.class)
    public void testExtraEndTag() {
        SmartScriptParser parser = new SmartScriptParser("{$ FOR i 1 10 1 $}\n" +
                " Useless text\n" +
                "{$END$}\n" +
                "{$END$}");
        fail();
    }

    @Test
    public void testNegativeForArgument() {
        SmartScriptParser parser = new SmartScriptParser("{$ FOR i -1 9 $}");
        parser.printTree();
        assertEquals("DocumentNode{}\n" +
                "\tForLoopNode{variable=i, startExpression=-1, endExpression=9}\n", outContent.toString());
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