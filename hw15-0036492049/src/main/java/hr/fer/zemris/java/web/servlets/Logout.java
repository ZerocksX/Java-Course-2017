package hr.fer.zemris.java.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet that invalidates session and redirects to '/'
 * Processes get on '/servleti/logout'
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "logout", value = "/servleti/logout")
public class Logout extends HttpServlet {
    /**
	 * UID
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
