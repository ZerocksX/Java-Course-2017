package hr.fer.zemris.java.webserver.demo;

import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo that shows some of the functionalites of the request context
 *
 * @author Pavao Jerebić
 */
public class DemoRequestContext {
    /**
     * starting method
     *
     * @param args ignored
     * @throws IOException if writing fails
     */
    public static void main(String[] args) throws IOException {
        demo1("primjer1.txt", "ISO-8859-2");
        demo1("primjer2.txt", "UTF-8");
        demo2("primjer3.txt", "UTF-8");
    }

    /**
     * Demo 1
     *
     * @param filePath file path
     * @param encoding enc encoding
     * @throws IOException if writing fails
     */
    private static void demo1(String filePath, String encoding) throws IOException {
        OutputStream os = Files.newOutputStream(Paths.get(filePath));
        RequestContext rc = new RequestContext(os, new HashMap<>(),
                new HashMap<>(),
                new ArrayList<>());
        rc.setEncoding(encoding);
        rc.setMimeType("text/plain");
        rc.setStatusCode(205);
        rc.setStatusText("Idemo dalje");
        rc.write("Čevapčići i Šiščevapčići.");
        os.close();
    }

    /**
     * Demo 2<br/>
     * produces cookies
     *
     * @param filePath file path
     * @param encoding enc encoding
     * @throws IOException if writing fails
     */
    private static void demo2(String filePath, String encoding) throws IOException {
        OutputStream os = Files.newOutputStream(Paths.get(filePath));
        RequestContext rc = new RequestContext(os, new HashMap<>(),
                new HashMap<>(),
                new ArrayList<>());
        rc.setEncoding(encoding);
        rc.setMimeType("text/plain");
        rc.setStatusCode(205);
        rc.setStatusText("Idemo dalje");
        rc.addRCCookie(new RequestContext.RCCookie("korisnik", "perica", 3600, "127.0.0.1",
                "/"));
        rc.addRCCookie(new RequestContext.RCCookie("zgrada", "B4", null, null, "/"));
        rc.write("Čevapčići i Šiščevapčići.");
        os.close();
    }
}
