package hr.fer.zemris.java.web.servlets.glasanje;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet that produces a HTML page that contains info on votes for each band, with charts, xls and more info on bands<br/>
 * Processes get requests on '/glasanje-rezultati'
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "glasanjeRezultati", urlPatterns = "/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<GlasanjeServlet.BandEntry, Integer> votingResults = (Map<GlasanjeServlet.BandEntry, Integer>) req.getAttribute("votingResults");
        if (votingResults == null) {
            try {
                votingResults = getVotingResults(req);
            } catch (Exception e) {
                req.setAttribute("errorMessage", "Error while writing/reading into results file.");
                req.getRequestDispatcher("/WEB-INF/pages/errorMessage.jsp").forward(req, resp);
                return;
            }
        }
        req.setAttribute("votingResults", votingResults);
        List<GlasanjeServlet.BandEntry> winningBands = new ArrayList<>();
        int maxCount = -1;
        for (Map.Entry<GlasanjeServlet.BandEntry, Integer> entry : votingResults.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                winningBands.clear();
                winningBands.add(entry.getKey());
            } else if (entry.getValue() == maxCount) {
                winningBands.add(entry.getKey());
            }
        }
        req.setAttribute("winningBands", winningBands);
        req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
    }

    /**
     * Helping method that reads voting info from the disk
     *
     * @param req current request
     * @return map containing {@link hr.fer.zemris.java.web.servlets.glasanje.GlasanjeServlet.BandEntry} and current vote count
     * @throws Exception if anything goes wrong
     */
    public synchronized static Map<GlasanjeServlet.BandEntry, Integer> getVotingResults(HttpServletRequest req) throws Exception {
        Map<GlasanjeServlet.BandEntry, Integer> votingResults = new HashMap<>();
        String resultsFileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
        String definitionFileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
        Path resultsFilePath = Paths.get(resultsFileName);
        Path definitionFilePath = Paths.get(definitionFileName);
        Files.readAllLines(definitionFilePath).forEach(s -> {
            String[] data = s.split("\t");
            votingResults.put(new GlasanjeServlet.BandEntry(Integer.parseInt(data[0]), data[1], data[2]), 0);
        });
        if (Files.exists(resultsFilePath)) {
            Files.readAllLines(resultsFilePath).forEach(s -> {
                String[] data = s.split("\t");
                int votingId = Integer.parseInt(data[0]);
                int count = Integer.parseInt(data[1]);
                votingResults.computeIfPresent(new GlasanjeServlet.BandEntry(votingId, null, null), (key, oldValue) -> oldValue + count);
            });
        }
        return votingResults;
    }
}
