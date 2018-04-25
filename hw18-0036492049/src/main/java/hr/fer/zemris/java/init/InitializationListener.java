package hr.fer.zemris.java.init;

import hr.fer.zemris.java.dao.ImageProvider;
import hr.fer.zemris.java.model.Image;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Listener that sets up image provider and maps it as an attribute on<br/>
 * "hr.fer.zemris.java.dao.ImageProvider"
 *
 * @author Pavao Jerebić
 */
@WebListener
public class InitializationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        Path root = Paths.get(context.getRealPath("WEB-INF")).toAbsolutePath();
        Path info = Paths.get(root.toString(), "opisnik.txt");
        try {
            List<String> infoLines = Files.readAllLines(info);
            List<Image> images = new ArrayList<>();
            for (int i = 0, n = infoLines.size(); i < n; i += 3) {
//                Path imagePath = Paths.get(root.toString(), "slike", infoLines.get(i));
                String name = infoLines.get(i + 1);
                List<String> tags = Arrays.stream(infoLines.get(i + 2).split("[,]")).map(String::trim).collect(Collectors.toList());
                Image image = new Image(infoLines.get(i), name, tags);
                Path thumbnailPath = Paths.get(context.getRealPath("WEB-INF"), "thumbnails", infoLines.get(i));
                if (Files.exists(thumbnailPath)) {
                    image.setThumbnail(thumbnailPath.toAbsolutePath().toString());
                }
                images.add(image);
            }
            ImageProvider imageProvider = new ImageProvider(images);
            context.setAttribute("hr.fer.zemris.java.dao.ImageProvider", imageProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
