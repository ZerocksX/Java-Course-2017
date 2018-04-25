package hr.fer.zemris.java.web.servlets.servleti;

import hr.fer.zemris.java.web.dao.DAOProvider;
import hr.fer.zemris.java.web.model.Poll;
import hr.fer.zemris.java.web.model.PollOption;
import hr.fer.zemris.java.web.util.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet that provides user with a list of band names for whom can the user vote<br/>
 * Processes get request on '/servleti/glasanje' with given pollID as parameter
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "glasanje", urlPatterns = "/servleti/glasanje")
public class GlasanjeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int pollID = 0;
        try {
            pollID = Integer.parseInt(req.getParameter("pollID"));
        } catch (Exception e) {
            Utils.forwardToErrorPage(req, resp, "Error while reading parameter id");
            return;
        }
        Poll poll = DAOProvider.getDao().getPollByID(pollID);
        if (poll == null) {
            Utils.forwardToErrorPage(req, resp, "Could not find poll with id: '" + pollID + "'");
            return;
        }
        List<PollOption> pollOptions = DAOProvider.getDao().getPollOptionsByPollID(pollID);

        req.setAttribute("poll", poll);
        req.setAttribute("pollOptions", pollOptions);
        req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);

    }

}
