package hr.fer.zemris.java.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * JPA entity manager factory provider
 *
 * @author Pavao Jerebić
 */
public class JPAEMFProvider {
    /**
     * Instance of entity manager factory
     */
    public static EntityManagerFactory emf;

    /**
     * getter for emf
     *
     * @return emf
     */
    public static EntityManagerFactory getEmf() {
        return emf;
    }

    /**
     * Setter for emf
     *
     * @param emf emf
     */
    public static void setEmf(EntityManagerFactory emf) {
        JPAEMFProvider.emf = emf;
    }
}
