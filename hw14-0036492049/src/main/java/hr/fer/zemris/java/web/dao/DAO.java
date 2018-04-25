package hr.fer.zemris.java.web.dao;

import hr.fer.zemris.java.web.model.Poll;
import hr.fer.zemris.java.web.model.PollOption;

import java.nio.file.Path;
import java.util.List;

/**
 * Classes that implement this interface are used as a layer between the app layer and persistence
 *
 * @author Pavao Jerebić
 */
public interface DAO {
    /**
     * Initializes database with 2 data sets <br/>
     * Polls and PollOptions
     *
     * @param pollsDefinition polls definition file containing poll title, poll message and relative path to poll options(from given pollsDefinition path)
     */
    void initializeDatabase(Path pollsDefinition);

    /**
     * Returns a poll with the given id
     *
     * @param id id
     * @return poll with given id or null if it does not exist
     */
    Poll getPollByID(long id);

    /**
     * Returns all poll options that have pollID equal to the given pollID
     *
     * @param pollID pollID
     * @return all pollOptions for given pollID
     */
    List<PollOption> getPollOptionsByPollID(long pollID);

    /**
     * Returns all polls
     *
     * @return all polls
     */
    List<Poll> getAllPolls();

    /**
     * Returns a poll option with the given id
     *
     * @param id id
     * @return poll option with the given id or null if it does not exist
     */
    PollOption getPollOptionByID(long id);

    /**
     * Updates poll option with the given id to a new vote count
     *
     * @param id        id
     * @param voteCount vote count
     */
    void updatePollOptionVoteCount(long id, long voteCount);
}