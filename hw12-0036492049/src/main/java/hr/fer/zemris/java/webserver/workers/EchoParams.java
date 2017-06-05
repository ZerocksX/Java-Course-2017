package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;

/**
 * @author Pavao Jerebić
 */
public class EchoParams implements IWebWorker {

    @Override
    public void processRequest(RequestContext context) throws Exception {
        context.setMimeType("text/plain");
        context.getParameters().forEach((k, v) -> {
            try {
                context.write(String.format("key = '%s', value = '%s'%n", k, v));
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        });
    }
}
