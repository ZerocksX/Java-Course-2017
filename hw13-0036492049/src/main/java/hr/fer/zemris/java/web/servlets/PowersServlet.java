package hr.fer.zemris.java.web.servlets;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet that produces an XLS file containing n sheets with all integers from a to b power of number of the current sheet<br/>
 * when get request is given on '/powers'
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "powers", urlPatterns = "/powers")
public class PowersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer a = null, b = null, n = null;
        StringBuilder errorMesage = new StringBuilder();

        try {
            a = Integer.parseInt(req.getParameter("a"));
        } catch (Exception e) {
            errorMesage.append("Invalid parameter a<br>");
        }

        try {
            b = Integer.parseInt(req.getParameter("b"));
        } catch (Exception e) {
            errorMesage.append("Invalid parameter b<br>");
        }

        try {
            n = Integer.parseInt(req.getParameter("n"));
        } catch (Exception e) {
            errorMesage.append("Invalid parameter n<br>");
        }

        if (a == null || b == null || n == null) {
            req.setAttribute("errorMessage", errorMesage.toString());
            req.getRequestDispatcher("WEB-INF/pages/errorMessage.jsp").forward(req, resp);
            return;
        }

        boolean wrongRange = false;

        if (a > 100 || a < -100) {
            errorMesage.append("Parameter a is not in interval [-100,100]<br>");
            wrongRange = true;
        }

        if (b > 100 || b < -100) {
            errorMesage.append("Parameter b is not in interval [-100,100]<br>");
            wrongRange = true;
        }

        if (n > 5 || n < 1) {
            errorMesage.append("Parameter n is not in interval [1,5]<br>");
            wrongRange = true;
        }

        if (a > b) {
            errorMesage.append("Parameter a is greater than parameter b<br>");
            wrongRange = true;
        }

        if (wrongRange) {
            req.setAttribute("errorMessage", errorMesage.toString());
            req.getRequestDispatcher("WEB-INF/pages/errorMessage.jsp").forward(req, resp);
            return;
        }

        resp.setContentType("application/vnd.ms-excel");
        HSSFWorkbook workbook = new HSSFWorkbook();
        for (int i = 1; i <= n; i++) {
            HSSFSheet sheet = workbook.createSheet(Integer.toString(i));
            for (int j = 0; j <= b - a; j++) {
                HSSFRow row = sheet.createRow(j);
                row.createCell(0).setCellValue(a + j);
                row.createCell(1).setCellValue(Math.pow(a + j, i));
            }
        }
        workbook.write(resp.getOutputStream());
    }
}
