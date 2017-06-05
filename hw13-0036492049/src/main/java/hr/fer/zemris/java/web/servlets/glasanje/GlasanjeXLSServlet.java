package hr.fer.zemris.java.web.servlets.glasanje;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Servlet that produces an XLS file on get request on '/glasanje-xls'
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "glasanjeXLS", urlPatterns = "/glasanje-xls")
public class GlasanjeXLSServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/vnd.ms-excel");
        HSSFWorkbook workbook = new HSSFWorkbook();
        Map<GlasanjeServlet.BandEntry, Integer> votingResults;
        try {
            votingResults = GlasanjeRezultatiServlet.getVotingResults(req);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error while writing/reading into results file.");
            req.getRequestDispatcher("/WEB-INF/pages/errorMessage.jsp").forward(req, resp);
            return;
        }
        HSSFSheet sheet = workbook.createSheet("Results");
        int i = 1;
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("Band name");
        row.createCell(1).setCellValue("Vote count");
        for (Map.Entry<GlasanjeServlet.BandEntry, Integer> entry : votingResults.entrySet()) {
            row = sheet.createRow(i);
            row.createCell(0).setCellValue(entry.getKey().getName());
            row.createCell(1).setCellValue(entry.getValue());
            i++;
        }
        workbook.write(resp.getOutputStream());
    }
}
