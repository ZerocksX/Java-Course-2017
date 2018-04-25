package hr.fer.zemris.java.web.servlets.servleti;

import hr.fer.zemris.java.web.dao.DAOProvider;
import hr.fer.zemris.java.web.model.PollOption;
import hr.fer.zemris.java.web.util.Utils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Servlet that creates a pie chart representing current band votes on '/servleti/glasanje-grafika' with given pollID as parameter
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "glasanjeGrafika", urlPatterns = "/servleti/glasanje-grafika")
public class GlasanjeGrafikaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int pollID = 0;
        try {
            pollID = Integer.parseInt(req.getParameter("pollID"));
        } catch (Exception e) {
            Utils.forwardToErrorPage(req, resp, "Error while reading parameter pollID");
            return;
        }
        List<PollOption> pollOptions;
        try {
            pollOptions = DAOProvider.getDao().getPollOptionsByPollID(pollID);
        } catch (Exception e) {
            Utils.forwardToErrorPage(req, resp, "Error while reading poll options");
            return;
        }
        DefaultPieDataset dataset = new DefaultPieDataset();
        pollOptions.forEach(pollOption -> dataset.setValue(pollOption.getOptionTitle(), pollOption.getVotesCount()));
        JFreeChart chart = ChartFactory.createPieChart3D(
                "",
                dataset,
                true,
                true,
                false
        );

        BufferedImage bim = chart.createBufferedImage(500, 270);
        resp.setContentType("image/png");

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bim, "png", bos);
            resp.getOutputStream().write(bos.toByteArray());
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
