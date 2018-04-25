package hr.fer.zemris.java.web.dao;


import hr.fer.zemris.java.web.dao.sql.SQLDAO;

/**
 * Singleton class that provides user with instance of {@link DAO}
 *
 * @author Pavao Jerebić
 */
public class DAOProvider {

    /**
     * private instance
     */
    private static DAO dao = new SQLDAO();

    /**
     * Getter for {@link DAO} instance
     *
     * @return DAO object
     */
    public static DAO getDao() {
        return dao;
    }

}