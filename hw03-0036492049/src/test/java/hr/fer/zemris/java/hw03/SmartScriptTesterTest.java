package hr.fer.zemris.java.hw03;


import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;
import static hr.fer.zemris.java.hw03.SmartScriptTester.*;

/**
 * @author Pavao Jerebić
 */
public class SmartScriptTesterTest {


    @Test
    public void testBasicExample() throws Exception{
        String document = loader("doc1.txt");

        SmartScriptParser parser1 = new SmartScriptParser(document);
        String documentBody1 = createOriginalDocumentBody(parser1.getDocumentNode());

        SmartScriptParser parser2 = new SmartScriptParser(documentBody1);
        String documentBody2 = createOriginalDocumentBody(parser2.getDocumentNode());

        assertEquals(documentBody1,documentBody2);
    }

    @Test
    public void testStringEscaping() throws Exception{
        String document = loader("doc2.txt");

        SmartScriptParser parser1 = new SmartScriptParser(document);
        String documentBody1 = createOriginalDocumentBody(parser1.getDocumentNode());

        SmartScriptParser parser2 = new SmartScriptParser(documentBody1);
        String documentBody2 = createOriginalDocumentBody(parser2.getDocumentNode());

        assertEquals(documentBody1,documentBody2);
    }

    @Test
    public void testTextEscaping() throws Exception{
        String document = loader("doc3.txt");

        SmartScriptParser parser1 = new SmartScriptParser(document);
        String documentBody1 = createOriginalDocumentBody(parser1.getDocumentNode());

        SmartScriptParser parser2 = new SmartScriptParser(documentBody1);
        String documentBody2 = createOriginalDocumentBody(parser2.getDocumentNode());

        assertEquals(documentBody1,documentBody2);
    }


    private String loader(String filename) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try(InputStream is =
                    this.getClass().getClassLoader().getResourceAsStream(filename)) {
            byte[] buffer = new byte[1024];
            while(true) {
                int read = is.read(buffer);
                if(read<1) break;
                bos.write(buffer, 0, read);
            }
            return new String(bos.toByteArray(), StandardCharsets.UTF_8);
        } catch(IOException ex) {
            return null;
        }
    }

}