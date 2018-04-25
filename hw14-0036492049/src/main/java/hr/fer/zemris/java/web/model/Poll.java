package hr.fer.zemris.java.web.model;

/**
 * Poll entity
 *
 * @author Pavao Jerebić
 */
public class Poll {
    /**
     * id
     */
    private long id;
    /**
     * title
     */
    private String title;
    /**
     * message
     */
    private String message;

    /**
     * Getter for id
     *
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * Getter for title
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for message
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter for id
     *
     * @param id id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Setter for title
     *
     * @param title title
     * @throws IllegalArgumentException if title is null
     */
    public void setTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("Title must not be null");
        }
        this.title = title;
    }

    /**
     * Sets message
     *
     * @param message message
     * @throws IllegalArgumentException if message is null
     */
    public void setMessage(String message) {
        if (title == null) {
            throw new IllegalArgumentException("Message must not be null");
        }
        this.message = message;
    }

    @Override
    public String toString() {
        return "Poll{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }


}
