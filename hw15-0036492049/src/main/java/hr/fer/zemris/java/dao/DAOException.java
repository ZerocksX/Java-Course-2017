package hr.fer.zemris.java.dao;

/**
 * Exception for {@link DAO}
 *
 * @author Pavao Jerebić
 */
public class DAOException extends RuntimeException {

    /**
     * uid
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor with cause
     *
     * @param throwable cause
     */
    public DAOException(Throwable throwable) {
        super(throwable);
    }

    /**
     * Constructor with message and cause
     *
     * @param message message
     * @param cause   cause
     */
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

}
