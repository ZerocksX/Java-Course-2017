package hr.fer.zemris.java.model;

/**
 * Exception that is raised when user form is invalid
 *
 * @author Pavao Jerebić
 * @see BlogUserForm#validate()
 */
public class BlogUserException extends RuntimeException {
    /**
	 * UID
	 */
	private static final long serialVersionUID = 1L;
	/**
     * Flags
     */
    private boolean[] flags;

    /**
     * Constructor that sets the flags
     *
     * @param flags flags
     */
    public BlogUserException(boolean[] flags) {
        this.flags = flags;
    }

    /**
     * Getter for flags
     *
     * @return flags
     */
    public boolean[] getFlags() {
        return flags;
    }
}
