package hr.fer.zemris.java.web.servlets.glasanje;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Servlet that creates a pie chart representing current band votes on '/glasanje-grafika'
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "glasanjeGrafika", urlPatterns = "/glasanje-grafika")
public class GlasanjeGrafikaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/png");
        Map<GlasanjeServlet.BandEntry, Integer> votingResults;
        try {
            votingResults = GlasanjeRezultatiServlet.getVotingResults(req);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error while writing/reading into results file.");
            req.getRequestDispatcher("/WEB-INF/pages/errorMessage.jsp").forward(req, resp);
            return;
        }
        DefaultPieDataset dataset = new DefaultPieDataset();
        votingResults.forEach((bandEntry, integer) -> dataset.setValue(bandEntry.getName(), integer));
        JFreeChart chart = ChartFactory.createPieChart3D(
                "OS usage",                  // chart title
                dataset,                // data
                true,                   // include legend
                true,
                false
        );
        BufferedImage bim = new BufferedImage(500, 270, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = bim.createGraphics();
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        plot.draw(g2d, new Rectangle(0, 0, bim.getWidth(), bim.getHeight()), null, null, null);
        g2d.dispose();
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bim, "png", bos);
            resp.getOutputStream().write(bos.toByteArray());
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
