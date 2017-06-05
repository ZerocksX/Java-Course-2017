package hr.fer.zemris.java.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet that sets attribute 'pickedBgCol' from given paramter 'color'<br/>
 * Allowed colors are white, red, green and cyan
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "setColor", urlPatterns = "/setcolor")
public class SetColorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String color = req.getParameter("color");
        if (color != null) {
            if (isValidColor(color)) {
                req.getSession().setAttribute("pickedBgCol", color);
            }
        }
        resp.sendRedirect(req.getContextPath() + "/");
    }

    /**
     * Helping method that checks if given color is allowed
     *
     * @param color color
     * @return true if color is valid
     */
    private boolean isValidColor(String color) {
        return color.equals("WHITE") || color.equals("RED") || color.equals("GREEN") || color.equals("CYAN");
    }
}
