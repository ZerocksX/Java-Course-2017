package hr.fer.zemris.java.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Redirects user from {"/","/index.jsp"} to "/servleti/main"
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "homeRedirect", value = {"/", "/index.jsp"})
public class HomeRedirect extends HttpServlet {
    /**
	 * UID
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/servleti/main");
    }
}
