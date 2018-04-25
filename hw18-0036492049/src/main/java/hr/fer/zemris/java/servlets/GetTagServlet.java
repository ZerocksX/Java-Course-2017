package hr.fer.zemris.java.servlets;

import com.google.gson.Gson;
import hr.fer.zemris.java.dao.ImageProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Returns all images with the given tag as json
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "getTag", value = "/getTag")
public class GetTagServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tag = req.getParameter("tag");
        if (tag == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        ImageProvider provider = (ImageProvider) req.getServletContext().getAttribute("hr.fer.zemris.java.dao.ImageProvider");

        Gson gson = new Gson();
        resp.setCharacterEncoding("UTF8");
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(provider.getImagesWithTag(tag)));
        resp.getWriter().flush();
    }
}
