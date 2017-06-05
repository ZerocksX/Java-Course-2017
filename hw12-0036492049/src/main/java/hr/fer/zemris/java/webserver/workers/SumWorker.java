package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * @author Pavao Jerebić
 */
public class SumWorker implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {
        Integer a = 1, b = 2;
        try {
            a = Integer.parseInt(context.getParameter("a"));
        } catch (Exception ignored) {
        }
        try {
            b = Integer.parseInt(context.getParameter("b"));
        } catch (Exception ignored) {

        }

        context.setTemporaryParameter("a", a.toString());
        context.setTemporaryParameter("b", b.toString());
        context.setTemporaryParameter("zbroj", Integer.toString(a + b));

        context.getDispatcher().dispatchRequest("/private/calc.smscr");

    }
}
