package hr.fer.zemris.java.web.init;

import hr.fer.zemris.java.dao.jpa.JPAEMFProvider;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Initialization listener that sets up EntityManagerFactory and
 * {@link JPAEMFProvider}
 *
 * @author Pavao Jerebić
 */
@WebListener
public class InitializationListener implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("baza.podataka.za.blog");
		sce.getServletContext().setAttribute("baza.podataka.za.blog", emf);
		JPAEMFProvider.setEmf(emf);
		// used when developing, while hibernate was on 'create-drop'
		// initializeDB();

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		JPAEMFProvider.setEmf(null);
		EntityManagerFactory emf = (EntityManagerFactory) sce.getServletContext().getAttribute("baza.podataka.za.blog");
		if (emf != null) {
			emf.close();
		}
	}

	// /**
	// * Helping method that initialized DB to some set of data for easier
	// testing
	// */
	// private void initializeDB() {
	// try {
	// DAO dao = DAOProvider.getDAO();
	// BlogUser admin = new BlogUser();
	// admin.setEmail("aa@e.c");
	// admin.setFirstName("admin");
	// admin.setLastName("admin");
	// admin.setNick("admin");
	// admin.setPasswordHash(Encoder.encode("password"));
	// dao.persistBlogUser(admin);
	//
	// BlogEntry entry = new BlogEntry();
	// entry.setCreatedAt(new Date());
	// entry.setLastModifiedAt(new Date());
	// entry.setTitle("Title");
	// entry.setText("Text");
	// entry.setCreator(admin);
	// dao.persistBlogEntry(entry);
	//
	// BlogComment comment = new BlogComment();
	// comment.setBlogEntry(entry);
	// comment.setMessage("Message");
	// comment.setPostedOn(new Date());
	// comment.setUsersEMail(admin.getEmail());
	// dao.persistBlogComment(comment);
	//
	// } catch (DAOException e) {
	// e.printStackTrace();
	// } finally {
	// JPAEMProvider.close();
	// }
	// }
}
