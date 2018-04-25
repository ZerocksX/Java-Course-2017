package hr.fer.zemris.java.web.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Utilities class
 *
 * @author Pavao Jerebić
 */
public class Utils {
    /**
     * Method that forwards page to an errorPage mapped to "/WEB-INF/pages/errorMessage.jsp",<br/>
     * and sets "errorMessage" attribute to the given message
     *
     * @param req     request
     * @param resp    response
     * @param message message
     * @throws ServletException if something fails
     * @throws IOException      if I/O fails
     */
    public static void forwardToErrorPage(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
        req.setAttribute("errorMessage", message);
        req.getRequestDispatcher("/WEB-INF/pages/errorMessage.jsp").forward(req, resp);
    }
}
