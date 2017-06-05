package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demo that represents some functionalities of the {@link SmartScriptEngine}
 *
 * @author Pavao Jerebić
 */
public class DemoSmartScriptEngine {
    /**
     * Starting method
     *
     * @param args args
     * @throws IOException e
     */
    public static void main(String[] args) throws IOException {
        demo1();
        demo2();
        demo3();
        demo4();
    }


    /**
     * Demo 4, writes fibonacci numbers
     *
     * @throws IOException if something fails
     */
    private static void demo4() throws IOException {
        String documentBody = readFromDisk("webroot/scripts/fibonacci.smscr");
        Map<String, String> parameters = new HashMap<>();
        Map<String, String> persistentParameters = new HashMap<>();
        List<RequestContext.RCCookie> cookies = new ArrayList<>();
        new SmartScriptEngine(
                new SmartScriptParser(documentBody).getDocumentNode(),
                new RequestContext(System.out, parameters, persistentParameters, cookies)
        ).execute();
    }

    /**
     * Demo 3, Counts number of calls
     *
     * @throws IOException if writing fails
     */
    private static void demo3() throws IOException {
        String documentBody = readFromDisk("webroot/scripts/brojPoziva.smscr");
        Map<String, String> parameters = new HashMap<>();
        Map<String, String> persistentParameters = new HashMap<>();
        List<RequestContext.RCCookie> cookies = new ArrayList<>();
        persistentParameters.put("brojPoziva", "3");
        RequestContext rc = new RequestContext(System.out, parameters, persistentParameters, cookies);
        new SmartScriptEngine(
                new SmartScriptParser(documentBody).getDocumentNode(),
                rc
        ).execute();
        System.out.println("Vrijednost u mapi: " + rc.getPersistentParameter("brojPoziva"));
    }

    /**
     * Helping method that reads whole string from the file
     *
     * @param s path
     * @return file
     * @throws IOException if reading fails
     */
    private static String readFromDisk(String s) throws IOException {
        return new String(Files.readAllBytes(Paths.get(s)), StandardCharsets.UTF_8);
    }

    /**
     * Represents addition
     *
     * @throws IOException if writing fails
     */
    private static void demo2() throws IOException {
        String documentBody = readFromDisk("webroot/scripts/zbrajanje.smscr");
        Map<String, String> parameters = new HashMap<>();
        Map<String, String> persistentParameters = new HashMap<>();
        List<RequestContext.RCCookie> cookies = new ArrayList<>();
        parameters.put("a", "4");
        parameters.put("b", "2");
        new SmartScriptEngine(
                new SmartScriptParser(documentBody).getDocumentNode(),
                new RequestContext(System.out, parameters, persistentParameters, cookies)
        ).execute();
    }

    /**
     * Represents basic for loop and echo tag
     *
     * @throws IOException if writing fails
     */
    private static void demo1() throws IOException {
        String documentBody = readFromDisk("webroot/scripts/osnovni.smscr");
        Map<String, String> parameters = new HashMap<>();
        Map<String, String> persistentParameters = new HashMap<>();
        List<RequestContext.RCCookie> cookies = new ArrayList<>();
        new SmartScriptEngine(
                new SmartScriptParser(documentBody).getDocumentNode(),
                new RequestContext(System.out, parameters, persistentParameters, cookies)
        ).execute();
    }
}
