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
 * Servlet that for a given path returns {@link hr.fer.zemris.java.model.Image} object as json
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "getImage", value = "/getImage")
public class GetImageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getParameter("path");
        if (path == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        ImageProvider provider = (ImageProvider) req.getServletContext().getAttribute("hr.fer.zemris.java.dao.ImageProvider");

        Gson gson = new Gson();
        resp.setCharacterEncoding("UTF8");
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(provider.getImage(path)));
        resp.getWriter().flush();
    }
}
