package hr.fer.zemris.java.webserver;

/**
 * Worker interface that has one method process Request
 *
 * @author Pavao Jerebić
 */
public interface IWebWorker {
    /**
     * Processes the request with the given context and produces an output or throws an exception
     *
     * @param context context
     * @throws Exception if anything goes wrong
     */
    void processRequest(RequestContext context) throws Exception;
}
