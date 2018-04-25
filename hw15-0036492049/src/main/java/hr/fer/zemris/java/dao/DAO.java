package hr.fer.zemris.java.dao;

import hr.fer.zemris.java.model.BlogComment;
import hr.fer.zemris.java.model.BlogEntry;
import hr.fer.zemris.java.model.BlogUser;

import java.util.List;

/**
 * Interface representing a data access object
 *
 * @author Pavao Jerebić
 */
public interface DAO {

    /**
     * Returns one blog entry with given id
     *
     * @param id id
     * @return blog entry, or null if it does not exist
     * @throws DAOException if anything fails
     */
    BlogEntry getBlogEntry(Long id) throws DAOException;

    /**
     * Returns one blog user with given id
     *
     * @param id id
     * @return blog user, or null if it does not exist
     * @throws DAOException if anything fails
     */
    BlogUser getBlogUserById(long id) throws DAOException;

    /**
     * Returns one blog user with given nick
     *
     * @param nick nick
     * @return blog entry, or null if it does not exist
     * @throws DAOException if anything fails
     */
    BlogUser getBlogUserByNick(String nick) throws DAOException;

    /**
     * Returns list of all blog users
     *
     * @return blog users, or null if they do not exist
     * @throws DAOException if anything fails
     */
    List<BlogUser> getAllBlogUsers() throws DAOException;

    /**
     * Saves one blog entry
     *
     * @param blogEntry blog entry
     * @throws DAOException if anything fails
     */
    void persistBlogEntry(BlogEntry blogEntry) throws DAOException;

    /**
     * Saves one blog user
     *
     * @param blogUser blog user
     * @throws DAOException if anything fails
     */
    void persistBlogUser(BlogUser blogUser) throws DAOException;

    /**
     * Saves one blog comment
     *
     * @param blogComment blog comment
     * @throws DAOException if anything fails
     */
    void persistBlogComment(BlogComment blogComment) throws DAOException;

}
