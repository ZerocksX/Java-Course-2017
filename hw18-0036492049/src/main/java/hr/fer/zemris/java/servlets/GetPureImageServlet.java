package hr.fer.zemris.java.servlets;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Servlet that returns an image with the given path relative to WEB-INF/slike
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "getPureImage", value = "/getPureImage")
public class GetPureImageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getParameter("path");
        if (path == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Path imagePath = Paths.get(req.getServletContext().getRealPath("WEB-INF/slike"), path);
        BufferedImage bim = ImageIO.read(imagePath.toFile());
        resp.setContentType("image/jpeg");
        ImageIO.write(bim, "jpg", resp.getOutputStream());
        resp.getOutputStream().close();
    }
}
