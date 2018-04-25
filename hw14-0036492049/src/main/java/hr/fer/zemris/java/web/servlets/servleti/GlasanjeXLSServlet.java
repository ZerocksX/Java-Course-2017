package hr.fer.zemris.java.web.servlets.servleti;

import hr.fer.zemris.java.web.dao.DAOProvider;
import hr.fer.zemris.java.web.model.PollOption;
import hr.fer.zemris.java.web.util.Utils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet that produces an XLS file on get request on '/servleti/glasanje-xls' with given pollID as parameter
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "glasanjeXLS", urlPatterns = "/servleti/glasanje-xls")
public class GlasanjeXLSServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int pollID = 0;
        try {
            pollID = Integer.parseInt(req.getParameter("pollID"));
        } catch (Exception e) {
            Utils.forwardToErrorPage(req, resp, "Error while reading parameter id");
            return;
        }
        List<PollOption> pollOptions;
        try {
            pollOptions = DAOProvider.getDao().getPollOptionsByPollID(pollID);
        } catch (Exception e) {
            Utils.forwardToErrorPage(req, resp, "Error while reading poll otpions");
            return;
        }

        resp.setContentType("application/vnd.ms-excel");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Results");
        int i = 1;
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("Option name");
        row.createCell(1).setCellValue("Vote count");
        for (PollOption pollOption : pollOptions) {
            row = sheet.createRow(i);
            row.createCell(0).setCellValue(pollOption.getOptionTitle());
            row.createCell(1).setCellValue(pollOption.getVotesCount());
            i++;
        }
        workbook.close();
        workbook.write(resp.getOutputStream());
    }
}
