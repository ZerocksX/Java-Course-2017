package hr.fer.zemris.java.web.servlets;

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

/**
 * Servlet that generates a pie chart of a fixed dataset on get request on '/reportImage'
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "reportImage", urlPatterns = "/reportImage")
public class ReportImageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/png");
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Linux", 29);
        dataset.setValue("Mac", 20);
        dataset.setValue("Windows", 51);
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
