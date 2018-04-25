package hr.fer.zemris.java.web.servlets.servleti;

import hr.fer.zemris.java.web.dao.DAOException;
import hr.fer.zemris.java.web.dao.DAOProvider;
import hr.fer.zemris.java.web.model.Poll;
import hr.fer.zemris.java.web.util.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Index for poll voting<br/>
 * Processes get requests on '/servleti/index.html'
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "servletiIndex", urlPatterns = {"/servleti/index.html"})
public class ServletiIndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            List<Poll> polls = DAOProvider.getDao().getAllPolls();
            req.setAttribute("polls", polls);
            req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
        } catch (DAOException e) {
            Utils.forwardToErrorPage(req, resp, "Error while writing/reading from database");
        }
    }
}
