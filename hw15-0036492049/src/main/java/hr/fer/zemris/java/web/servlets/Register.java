package hr.fer.zemris.java.web.servlets;

import hr.fer.zemris.java.dao.DAOException;
import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.model.BlogUser;
import hr.fer.zemris.java.model.BlogUserException;
import hr.fer.zemris.java.model.BlogUserForm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Processes form and if possible creates a new blog user
 * Processes post and get on '/servleti/register'
 *
 * @author Pavao Jerebić
 * @see BlogUserForm
 */
@WebServlet(name = "register", value = "/servleti/register")
public class Register extends HttpServlet {
    /**
	 * UID
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String nick = req.getParameter("nick");
        String password = req.getParameter("password");
        BlogUser blogUser;
        BlogUserForm blogUserForm = new BlogUserForm(firstName, lastName, email, nick, password);
        try {
            blogUser = new BlogUser(blogUserForm);
        } catch (BlogUserException e) {
            boolean[] flags = e.getFlags();
            req.setAttribute("flags", flags);
            req.setAttribute("invalidForm", blogUserForm);
            req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
            return;
        }
        BlogUser existingUser = null;
        try {
            existingUser = DAOProvider.getDAO().getBlogUserByNick(nick);
        } catch (Exception ignored) {
        }

        if (existingUser != null) {
            req.setAttribute("flags", new boolean[]{false, false, false, true, false});
            req.setAttribute("invalidForm", blogUser);
            req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
            return;
        }

        try {
            DAOProvider.getDAO().persistBlogUser(blogUser);
        } catch (DAOException e) {
            req.setAttribute("flags", new boolean[]{false, false, false, true, false});
            req.setAttribute("invalidForm", blogUser);
            req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
    }
}
