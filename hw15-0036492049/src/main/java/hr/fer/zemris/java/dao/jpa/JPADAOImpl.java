package hr.fer.zemris.java.dao.jpa;

import hr.fer.zemris.java.dao.DAO;
import hr.fer.zemris.java.dao.DAOException;
import hr.fer.zemris.java.model.BlogComment;
import hr.fer.zemris.java.model.BlogEntry;
import hr.fer.zemris.java.model.BlogUser;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

/**
 * JPA implementation of {@link DAO}
 *
 * @author Pavao Jerebić
 */
public class JPADAOImpl implements DAO {
    @Override
    public BlogEntry getBlogEntry(Long id) throws DAOException {
        return JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
    }

    @Override
    public BlogUser getBlogUserById(long id) throws DAOException {
        return JPAEMProvider.getEntityManager().find(BlogUser.class, id);

    }

    @Override
    public BlogUser getBlogUserByNick(String nick) throws DAOException {
        try {
            return (BlogUser) JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.findByNick")
                    .setParameter("nick", nick)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BlogUser> getAllBlogUsers() throws DAOException {
        try {
            return (List<BlogUser>) JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.findAll")
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void persistBlogEntry(BlogEntry blogEntry) throws DAOException {
        EntityManager em = JPAEMProvider.getEntityManager();
        try {
            em.persist(blogEntry);
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void persistBlogUser(BlogUser blogUser) throws DAOException {
        EntityManager em = JPAEMProvider.getEntityManager();
        try {
            em.persist(blogUser);
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void persistBlogComment(BlogComment blogComment) throws DAOException {
        EntityManager em = JPAEMProvider.getEntityManager();
        try {
            em.persist(blogComment);
            blogComment.getBlogEntry().getComments().add(blogComment);
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }
}
