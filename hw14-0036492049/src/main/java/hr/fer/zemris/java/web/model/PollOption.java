package hr.fer.zemris.java.web.model;

/**
 * Poll option entity
 *
 * @author Pavao Jerebić
 */
public class PollOption {
    /**
     * ID
     */
    private long id;
    /**
     * Option title
     */
    private String optionTitle;
    /**
     * Option link
     */
    private String optionLink;
    /**
     * Poll ID
     */
    private long pollID;
    /**
     * Votes count
     */
    private long votesCount;

    /**
     * Basic constructor
     */
    public PollOption() {
    }

    /**
     * Getter for ID
     *
     * @return ID
     */
    public long getId() {
        return id;
    }

    /**
     * Getter for option title
     *
     * @return option title
     */
    public String getOptionTitle() {
        return optionTitle;
    }

    /**
     * Getter for option link
     *
     * @return option link
     */
    public String getOptionLink() {
        return optionLink;
    }

    /**
     * getter for poll id
     *
     * @return poll id
     */
    public long getPollID() {
        return pollID;
    }

    /**
     * getter for votes count
     *
     * @return votes count
     */
    public long getVotesCount() {
        return votesCount;
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
     * Setter for option title
     *
     * @param optionTitle option title
     * @throws IllegalArgumentException if option title is null
     */
    public void setOptionTitle(String optionTitle) {
        if (optionTitle == null) {
            throw new IllegalArgumentException("All parameters must not be null");
        }
        this.optionTitle = optionTitle;
    }

    /**
     * Setter for option link
     *
     * @param optionLink option link
     * @throws IllegalArgumentException if option link is null
     */
    public void setOptionLink(String optionLink) {
        if (optionLink == null) {
            throw new IllegalArgumentException("All parameters must not be null");
        }
        this.optionLink = optionLink;
    }

    /**
     * Setter for poll ID
     *
     * @param pollID poll ID
     */
    public void setPollID(long pollID) {
        this.pollID = pollID;
    }

    /**
     * Setter for votes count
     *
     * @param votesCount votes count
     */
    public void setVotesCount(long votesCount) {
        this.votesCount = votesCount;
    }

    @Override
    public String toString() {
        return "PollOption{" +
                "id=" + id +
                ", optionTitle='" + optionTitle + '\'' +
                ", optionLink='" + optionLink + '\'' +
                ", pollID=" + pollID +
                ", votesCount=" + votesCount +
                '}';
    }
}
