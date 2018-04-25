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

/**
 * Servlet that operates get requests on '/servleti/glasanje-glasaj'<br/>
 * Adds one vote for a band with given id in the parameters
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "glasanjeGlasaj", urlPatterns = "/servleti/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id;
        try {
            id = Integer.parseInt(req.getParameter("id"));
        } catch (Exception ignored) {
            Utils.forwardToErrorPage(req, resp, "Error while reading parameter id");
            return;
        }
        PollOption pollOption = DAOProvider.getDao().getPollOptionByID(id);
        if (pollOption == null) {
            Utils.forwardToErrorPage(req, resp, "Could not find option with id: '" + id + "'");
            return;
        }
        Poll poll = DAOProvider.getDao().getPollByID(pollOption.getPollID());
        if (poll == null) {
            Utils.forwardToErrorPage(req, resp, "Could not find poll with id: '" + pollOption.getPollID() + "'");
            return;
        }

        DAOProvider.getDao().updatePollOptionVoteCount(pollOption.getId(), pollOption.getVotesCount() + 1);

        resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati?pollID=" + poll.getId());
    }
}
