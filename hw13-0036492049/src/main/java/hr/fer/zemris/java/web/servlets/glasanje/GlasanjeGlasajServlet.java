package hr.fer.zemris.java.web.servlets.glasanje;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet that operates get requests on '/glasanje-glasaj'<br/>
 * Adds one vote for a band with given id in the parameters
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "glasanjeGlasaj", urlPatterns = "/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = null;
        try {
            id = Integer.parseInt(req.getParameter("id"));
        } catch (Exception ignored) {
        }
        if (id == null) {
            resp.sendRedirect(req.getContextPath() + "/glasanje");
            return;
        }
        String resultsFileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
        Map<GlasanjeServlet.BandEntry, Integer> votingResults = new HashMap<>();
        Boolean[] failed = new Boolean[]{false};

        try {
            votingResults = GlasanjeRezultatiServlet.getVotingResults(req);
            votingResults.computeIfPresent(new GlasanjeServlet.BandEntry(id, null, null), (key, oldValue) -> oldValue + 1);
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(resultsFileName), StandardCharsets.UTF_8);
            synchronized (GlasanjeRezultatiServlet.class) {
                votingResults.forEach((key, value) -> {
                    try {
                        bw.write(String.format("%d\t%d%n", key.getId(), value));
                    } catch (IOException e) {
                        failed[0] = false;
                    }

                });
                bw.flush();
            }
        } catch (Exception e) {
            failed[0] = true;
        }
        if (failed[0]) {
            req.setAttribute("errorMessage", "Error while writing/reading into results file.");
            req.getRequestDispatcher("/WEB-INF/pages/errorMessage.jsp").forward(req, resp);
            return;
        }

        req.setAttribute("votingResults", votingResults);
        resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
    }
}
