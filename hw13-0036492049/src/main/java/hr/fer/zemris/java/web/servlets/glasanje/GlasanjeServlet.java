package hr.fer.zemris.java.web.servlets.glasanje;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * Servlet that provides user with a list of band names for whom can the user vote<br/>
 * Processes get request on '/glasanje'
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "glasanje", urlPatterns = "/glasanje")
public class GlasanjeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
        Set<BandEntry> bands = new HashSet<>();
        try {
            Files.readAllLines(Paths.get(fileName)).forEach(s -> {
                String[] data = s.split("\t");
                Integer id = Integer.parseInt(data[0]);
                String name = data[1], link = data[2];
                bands.add(new BandEntry(id, name, link));
            });
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error while reading band file.<br>" + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/pages/errorMessage.jsp").forward(req, resp);
            return;
        }

        req.setAttribute("bands", bands);
        req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);

    }

    /**
     * Class that stores info on bands<br/>
     * Band are equal if their id is the same
     */
    public static class BandEntry {
        /**
         * Id
         */
        private Integer id;
        /**
         * Name
         */
        private String name;
        /**
         * Link
         */
        private String link;

        /**
         * Basic constructor
         *
         * @param id   id
         * @param name name
         * @param link link
         * @throws IllegalArgumentException if id is null
         */
        public BandEntry(Integer id, String name, String link) {
            if (id == null
                    ) {
                throw new IllegalArgumentException("All parameters must not be null");
            }
            this.id = id;
            this.name = name;
            this.link = link;
        }

        /**
         * Getter for id
         *
         * @return id
         */
        public Integer getId() {
            return id;
        }

        /**
         * Getter for name
         *
         * @return name
         */
        public String getName() {
            return name;
        }

        /**
         * Getter for link
         *
         * @return link
         */
        public String getLink() {
            return link;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BandEntry)) return false;

            BandEntry bandEntry = (BandEntry) o;

            return id.equals(bandEntry.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }

}
