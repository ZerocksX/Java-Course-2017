package hr.fer.zemris.java.web.dao;

/**
 * Exception used with {@link DAO}
 *
 * @author Pavao Jerebić
 */
public class DAOException extends RuntimeException {

    /**
     * @see RuntimeException#RuntimeException()
     */
    public DAOException() {
    }

    /**
     * @param message            message
     * @param cause              cause
     * @param enableSuppression  enalbe suppresion
     * @param writableStackTrace writable stack trace
     * @see RuntimeException#RuntimeException(String, Throwable, boolean, boolean)
     */
    public DAOException(String message, Throwable cause,
                        boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * @param message message
     * @param cause   cause
     * @see RuntimeException#RuntimeException(String, Throwable)
     */
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message message
     * @see RuntimeException#RuntimeException(String)
     */
    public DAOException(String message) {
        super(message);
    }

    /**
     * @param cause cause
     * @see RuntimeException#RuntimeException(Throwable)
     */
    public DAOException(Throwable cause) {
        super(cause);
    }
}