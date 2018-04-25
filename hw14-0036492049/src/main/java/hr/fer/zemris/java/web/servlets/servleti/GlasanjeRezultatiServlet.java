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
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet that produces a HTML page that contains info on votes for each band, with charts, xls and more info on bands<br/>
 * Processes get requests on '/servleti/glasanje-rezultati' with given pollID as parameter
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "glasanjeRezultati", urlPatterns = "/servleti/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {
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
        req.setAttribute("pollOptions", pollOptions);
        List<PollOption> winningOptions = new ArrayList<>();
        long maxCount = -1;
        for (PollOption pollOption : pollOptions) {
            if (pollOption.getVotesCount() > maxCount) {
                maxCount = pollOption.getVotesCount();
                winningOptions.clear();
                winningOptions.add(pollOption);
            } else if (pollOption.getVotesCount() == maxCount) {
                winningOptions.add(pollOption);
            }
        }
        req.setAttribute("winningOptions", winningOptions);
        req.setAttribute("poll", poll);
        req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
    }
}
