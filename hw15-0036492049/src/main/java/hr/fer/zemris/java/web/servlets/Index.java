package hr.fer.zemris.java.web.servlets;

import hr.fer.zemris.java.dao.DAOProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Index page that gives user login form, link to registration form and list of all registered users author page
 * Processes get on '/servleti/main'
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "index", value = {"/servleti/main"})
public class Index extends HttpServlet {
    /**
	 * UID
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("authors", DAOProvider.getDAO().getAllBlogUsers());
        req.setAttribute("login", req.getParameter("login"));
        req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(req, resp);
    }
}
