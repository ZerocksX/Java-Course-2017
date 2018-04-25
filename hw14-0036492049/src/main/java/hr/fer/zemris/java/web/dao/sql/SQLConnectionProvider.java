package hr.fer.zemris.java.web.dao.sql;

import java.sql.Connection;

/**
 * Stores connections to database in {@link ThreadLocal}
 *
 * @author Pavao Jerebić
 */
public class SQLConnectionProvider {

    /**
     * Connections to database for each thread
     */
    private static ThreadLocal<Connection> connections = new ThreadLocal<>();

    /**
     * Sets connection for current thread
     *
     * @param con db connection
     */
    public static void setConnection(Connection con) {
        if (con == null) {
            connections.remove();
        } else {
            connections.set(con);
        }
    }

    /**
     * Getter for connection for current thread
     *
     * @return db connection
     */
    public static Connection getConnection() {
        return connections.get();
    }

}