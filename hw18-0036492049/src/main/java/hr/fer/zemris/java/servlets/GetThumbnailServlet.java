package hr.fer.zemris.java.servlets;

import hr.fer.zemris.java.dao.ImageProvider;
import hr.fer.zemris.java.model.Image;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Returns a thumbnail of an image with the given path
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "getThumbnail", value = "/getThumbnail")
public class GetThumbnailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getParameter("path");
        if (path == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        ImageProvider provider = (ImageProvider) req.getServletContext().getAttribute("hr.fer.zemris.java.dao.ImageProvider");
        Image image = provider.getImage(path);
        if (image == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if (image.getThumbnail() == null) {
            BufferedImage img = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);
            Path imagePath = Paths.get(req.getServletContext().getRealPath("WEB-INF/slike"), image.getPath());
            img.createGraphics().drawImage(ImageIO.read(new File(imagePath.toString())).getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
            Path thumbnails = Paths.get(req.getServletContext().getRealPath("WEB-INF"), "thumbnails");
            if (!Files.exists(thumbnails)) {
                Files.createDirectories(thumbnails);
            }
            Path thumbnail = Paths.get(thumbnails.toAbsolutePath().toString(), image.getPath());
            image.setThumbnail(thumbnail.toAbsolutePath().toString());
            ImageIO.write(img, "jpg", new File(image.getThumbnail()));
            System.out.println(image.getThumbnail());
        }
        Path thumbnailPath = Paths.get(image.getThumbnail());
        BufferedImage bim = ImageIO.read(thumbnailPath.toFile());
        resp.setContentType("image/jpeg");
        ImageIO.write(bim, "jpg", resp.getOutputStream());
        resp.getOutputStream().close();
    }
}
