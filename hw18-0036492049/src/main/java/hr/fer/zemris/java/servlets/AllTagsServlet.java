package hr.fer.zemris.java.servlets;

import com.google.gson.Gson;
import hr.fer.zemris.java.dao.ImageProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Servlet that returns all tags as json
 * @author Pavao Jerebić
 */
@WebServlet(name = "allTags", value = "/allTags")
public class AllTagsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ImageProvider provider = (ImageProvider) req.getServletContext().getAttribute("hr.fer.zemris.java.dao.ImageProvider");

        Gson gson = new Gson();
        resp.setCharacterEncoding("UTF8");
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(provider.getTags()));
        resp.getWriter().flush();
    }
}
