package hr.fer.zemris.java.dao;

import hr.fer.zemris.java.dao.jpa.JPADAOImpl;

/**
 * {@link DAO} provider
 * @author Pavao Jerebić
 */
public class DAOProvider {

    /**
     * Instance of {@link DAO} implementation
     */
    private static DAO dao = new JPADAOImpl();

    /**
     * getter for DAO
     * @return DAO
     */
    public static DAO getDAO() {
        return dao;
    }
}
