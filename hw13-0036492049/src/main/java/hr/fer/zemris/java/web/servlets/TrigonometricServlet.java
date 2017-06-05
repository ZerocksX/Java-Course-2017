package hr.fer.zemris.java.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet that calculates cosine and sine values for all integers between given parameters 'a' and 'b'<br>
 * on get request on '/trigonometric'
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "trigonometric", urlPatterns = "/trigonometric")
public class TrigonometricServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int a = 0, b = 360;
        try {
            a = Integer.valueOf(req.getParameter("a"));
        } catch (Exception ignored) {
        }
        try {
            b = Integer.valueOf(req.getParameter("b"));
        } catch (Exception ignored) {
        }
        if (a > b) {
            a += b;
            b = a - b;
            a = a - b;
        }

        if (b > a + 720) {
            b = a + 720;
        }

        List<AngleandCosSin> trigonometricList = new ArrayList<>();
        for (int i = a; i <= b; i++) {
            double angleAsRad = Math.toRadians(i);
            trigonometricList.add(new AngleandCosSin(i, Math.cos(angleAsRad), Math.sin(angleAsRad)));
        }

        req.setAttribute("trigonometricList", trigonometricList);
        req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);

    }

    /**
     * Helping class that stores information on angle and its cosine and sine values
     */
    public static class AngleandCosSin {
        /**
         * angle
         */
        private int angle;
        /**
         * cosine
         */
        private double cos;
        /**
         * sine
         */
        private double sin;

        /**
         * Basic constructor
         *
         * @param angle angle
         * @param cos   cosine
         * @param sin   sine
         */
        public AngleandCosSin(int angle, double cos, double sin) {
            this.angle = angle;
            this.cos = cos;
            this.sin = sin;
        }

        /**
         * Getter for angle
         *
         * @return angle
         */
        public int getAngle() {
            return angle;
        }

        /**
         * Getter for cosine
         *
         * @return cosine
         */
        public double getCos() {
            return cos;
        }

        /**
         * Getter for sine
         *
         * @return sine
         */
        public double getSin() {
            return sin;
        }
    }
}
