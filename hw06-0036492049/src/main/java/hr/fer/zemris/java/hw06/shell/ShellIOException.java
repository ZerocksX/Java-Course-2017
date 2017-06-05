package hr.fer.zemris.java.hw06.shell;

/**
 * Exception to be called when environment IO fails
 *
 * @author Pavao Jerebić
 */
public class ShellIOException extends RuntimeException {
    /**
     * Default constructor
     */
    public ShellIOException() {
    }

    /**
     * Constructor with message
     *
     * @param s message
     */
    public ShellIOException(String s) {
        super(s);
    }
}
