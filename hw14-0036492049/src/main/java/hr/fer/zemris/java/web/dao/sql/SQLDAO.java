package hr.fer.zemris.java.web.dao.sql;


import hr.fer.zemris.java.web.dao.DAO;
import hr.fer.zemris.java.web.dao.DAOException;
import hr.fer.zemris.java.web.model.Poll;
import hr.fer.zemris.java.web.model.PollOption;
import hr.fer.zemris.java.web.util.Utils;

import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of DAO that uses Apache Derby database
 *
 * @author Pavao Jerebić
 */
public class SQLDAO implements DAO {


    private static final String DELETE_FROM_POLLOPTIONS = "DELETE FROM POLLOPTIONS";
    private static final String DELETE_FROM_POLLS = "DELETE FROM POLLS";
    private static final String CREATE_TABLE_POLLS = "CREATE TABLE Polls (" +
            "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
            " title VARCHAR(150) NOT NULL," +
            " message CLOB(2048) NOT NULL" +
            ")";
    private static final String CREATE_TABLE_POLL_OPTIONS = "CREATE TABLE PollOptions" +
            " (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
            " optionTitle VARCHAR(100) NOT NULL," +
            " optionLink VARCHAR(150) NOT NULL," +
            " pollID BIGINT," +
            " votesCount BIGINT," +
            " FOREIGN KEY (pollID) REFERENCES Polls(id)" +
            ")";
    private static final String INSERT_INTO_POLLS = "INSERT INTO Polls (title, message) VALUES (?,?)";
    private static final String INSERT_INTO_POLL_OPTIONS = "INSERT INTO PollOptions(optionTitle, optionLink, pollID, votesCount) VALUES( ?, ?,?,?)";
    private static final String SELECT_ALL_POLLS = "SELECT * FROM POLLS";
    private static final String SELECT_ALL_POLL_OPTIONS = "SELECT * FROM PollOptions";
    private static final String SELECT_FROM_POLLS_WHERE_ID = "SELECT * FROM POLLS WHERE ID = (?)";
    private static final String SELECT_FROM_POLLOPTIONS_WHERE_POLLID = "SELECT * FROM POLLOPTIONS WHERE POLLID = (?)";
    private static final String SELECT_FROM_POLLOPTIONS_WHERE_ID = "SELECT * FROM POLLOPTIONS WHERE ID = (?)";
    private static final String UPDATE_POLLOPTIONS_SET_VOTESCOUNT_WHERE_ID = "UPDATE POLLOPTIONS SET VOTESCOUNT=(?) WHERE ID = (?)";

    /**
     * Initializes db with two tables: <br/>
     * CREATE TABLE Polls( <br/>
     * id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, <br/>
     * title VARCHAR(150) NOT NULL, <br/>
     * message CLOB(2048) NOT NULL <br/>
     * ); <br/>
     * CREATE TABLE PollOptions( <br/>
     * id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, <br/>
     * optionTitle VARCHAR(100) NOT NULL, <br/>
     * optionLink VARCHAR(150) NOT NULL, <br/>
     * pollID BIGINT, <br/>
     * votesCount BIGINT, <br/>
     * FOREIGN KEY (pollID) REFERENCES Polls(id) <br/>
     * ); <br/>
     *
     * @param pollsDefinition polls definition file containing poll title, poll message and relative path to poll options
     */
    @Override
    public void initializeDatabase(Path pollsDefinition) {

        Connection connection = SQLConnectionProvider.getConnection();
        boolean pollsInitialized = checkTable(SELECT_ALL_POLLS, CREATE_TABLE_POLLS, connection) & checkTable(SELECT_ALL_POLL_OPTIONS, CREATE_TABLE_POLL_OPTIONS, connection);

        if (!pollsInitialized) {
            Map<Poll, List<PollOption>> polls = Utils.getAllPolls(pollsDefinition);
            try {
                executeAndCloseStatement(connection.prepareStatement(DELETE_FROM_POLLOPTIONS));
                executeAndCloseStatement(connection.prepareStatement(DELETE_FROM_POLLS));

                for (Poll poll : polls.keySet()) {
                    try (PreparedStatement pollsInsert = connection.prepareStatement(INSERT_INTO_POLLS, Statement.RETURN_GENERATED_KEYS)) {

                        pollsInsert.setString(1, poll.getTitle());
                        pollsInsert.setString(2, poll.getMessage());

                        pollsInsert.executeUpdate();
                        ResultSet resultSet = pollsInsert.getGeneratedKeys();
                        if (resultSet != null && resultSet.next()) {
                            long pollID = resultSet.getLong(1);
                            for (PollOption pollOption : polls.get(poll)) {
                                try (
                                        PreparedStatement pollOptionInsert = connection.prepareStatement(INSERT_INTO_POLL_OPTIONS)
                                ) {
                                    pollOptionInsert.setString(1, pollOption.getOptionTitle());
                                    pollOptionInsert.setString(2, pollOption.getOptionLink());
                                    pollOptionInsert.setLong(3, pollID);
                                    pollOptionInsert.setLong(4, pollOption.getVotesCount());

                                    pollOptionInsert.executeUpdate();
                                }
                            }
                        } else {
                            throw new DAOException("Failed to initialize polls. Polls did not generate id.");
                        }
                        resultSet.close();
                    }
                }

            } catch (SQLException e) {
                throw new DAOException(e);
            }
        }

    }

    @Override
    public Poll getPollByID(long id) {
        Connection connection = SQLConnectionProvider.getConnection();
        Poll poll = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_POLLS_WHERE_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                poll = new Poll();
                poll.setId(resultSet.getLong(1));
                poll.setTitle(resultSet.getString(2));
                poll.setMessage(resultSet.getString(3));
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return poll;
    }

    @Override
    public List<PollOption> getPollOptionsByPollID(long pollID) {
        List<PollOption> pollOptions = new ArrayList<>();
        Connection connection = SQLConnectionProvider.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_POLLOPTIONS_WHERE_POLLID)) {
            preparedStatement.setLong(1, pollID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                PollOption pollOption = new PollOption();
                pollOption.setId(resultSet.getLong(1));
                pollOption.setOptionTitle(resultSet.getString(2));
                pollOption.setOptionLink(resultSet.getString(3));
                pollOption.setPollID(resultSet.getLong(4));
                pollOption.setVotesCount(resultSet.getLong(5));
                pollOptions.add(pollOption);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return pollOptions;
    }

    @Override
    public List<Poll> getAllPolls() {
        List<Poll> polls = new ArrayList<>();
        Connection connection = SQLConnectionProvider.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_POLLS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Poll poll = new Poll();
                poll.setId(resultSet.getLong(1));
                poll.setTitle(resultSet.getString(2));
                poll.setMessage(resultSet.getString(3));
                polls.add(poll);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return polls;
    }

    @Override
    public PollOption getPollOptionByID(long id) {
        PollOption pollOption = null;
        Connection connection = SQLConnectionProvider.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_POLLOPTIONS_WHERE_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                pollOption = new PollOption();
                pollOption.setId(resultSet.getLong(1));
                pollOption.setOptionTitle(resultSet.getString(2));
                pollOption.setOptionLink(resultSet.getString(3));
                pollOption.setPollID(resultSet.getLong(4));
                pollOption.setVotesCount(resultSet.getLong(5));
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return pollOption;
    }

    @Override
    public void updatePollOptionVoteCount(long id, long voteCount) {
        Connection connection = SQLConnectionProvider.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_POLLOPTIONS_SET_VOTESCOUNT_WHERE_ID)) {
            preparedStatement.setLong(2, id);
            preparedStatement.setLong(1, voteCount);
            int r = preparedStatement.executeUpdate();
            if (r != 1) {
                throw new DAOException("Update not performed");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Helping method that executes simple sql queries and closes them
     *
     * @param pst prepared statement
     * @throws SQLException if anything goes wrong
     */
    private void executeAndCloseStatement(PreparedStatement pst) throws SQLException {
        try {
            pst.execute();
            pst.close();
        } catch (SQLException e) {
            try {
                pst.close();
            } catch (Exception ignored) {
            }
            throw e;
        }
    }

    /**
     * Helping method that checks if the given table exists or if it is empty and if it does not creates a new table with the given string as {@link PreparedStatement}
     *
     * @param checkString  sql query used to check if table exist
     * @param createString ddl query that creates a table
     * @param connection   connection to db
     * @return true if table existed and it was not empty
     */
    private boolean checkTable(String checkString, String createString, Connection connection) {
        boolean pollsValid = true;
        try (PreparedStatement preparedStatement = connection.prepareStatement(checkString)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                pollsValid = false;
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("42X05")) {
                try {
                    executeAndCloseStatement(
                            connection.prepareStatement(createString)
                    );
                } catch (SQLException e1) {
                    throw new DAOException(e1);
                }
            }
            pollsValid = false;
        }
        return pollsValid;
    }
}