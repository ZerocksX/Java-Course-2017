package hr.fer.zemris.java.dao.jpa;

import hr.fer.zemris.java.dao.DAOException;

import javax.persistence.EntityManager;

/**
 * JPA entity manager provider
 *
 * @author Pavao Jerebić
 */
public class JPAEMProvider {
    /**
     * Thread local data
     */
    private static ThreadLocal<LocalData> locals = new ThreadLocal<>();

    /**
     * Gets entity manager for this thread or creates one if it does not exist using {@link JPAEMFProvider#getEmf()}
     *
     * @return entity manager
     */
    public static EntityManager getEntityManager() {
        LocalData ldata = locals.get();
        if (ldata == null) {
            ldata = new LocalData();
            ldata.em = JPAEMFProvider.getEmf().createEntityManager();
            ldata.em.getTransaction().begin();
            locals.set(ldata);
        }
        return ldata.em;
    }

    /**
     * Closes currently opened JPA entity manager
     *
     * @throws DAOException if anything fails
     */
    public static void close() throws DAOException {
        LocalData ldata = locals.get();
        if (ldata == null) {
            return;
        }
        DAOException dex = null;
        try {
            ldata.em.getTransaction().commit();
        } catch (Exception ex) {
            dex = new DAOException("Unable to commit transaction.", ex);
        }
        try {
            ldata.em.close();
        } catch (Exception ex) {
            if (dex != null) {
                dex = new DAOException("Unable to close entity manager.", ex);
            }
        }
        locals.remove();
        if (dex != null) throw dex;
    }

    /**
     * Helping class to store local data
     */
    private static class LocalData {
        /**
         * Entity manager
         */
        EntityManager em;
    }
}
