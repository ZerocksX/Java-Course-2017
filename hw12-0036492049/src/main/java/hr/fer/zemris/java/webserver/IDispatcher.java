package hr.fer.zemris.java.webserver;

/**
 * Interface representing a dispatcher that defines one method, dispatchRequest
 *
 * @author Pavao Jerebić
 */
public interface IDispatcher {
    /**
     * The dispatch request will take the given urlPath and will dispatch a response or throw an exception
     *
     * @param urlPath url path
     * @throws Exception an exception
     */
    void dispatchRequest(String urlPath) throws Exception;
}
