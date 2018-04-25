package hr.fer.zemris.java.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet that redirects get requests from '/index.html' to '/servleti/index.html'
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "index", urlPatterns = "/index.html")
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/servleti/index.html");
    }
}
