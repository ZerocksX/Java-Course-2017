package hr.fer.zemris.java.web.servlets;

import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.encoder.Encoder;
import hr.fer.zemris.java.model.BlogUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet that checks if given $nick$ and $password$ exist in the DB<br/>
 * If the login was successful new attribute 'principal' is created in the {@link HttpServletRequest#getSession()}
 * Processes post on '/servleti/login'
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "login", value = {"/servleti/login"})
public class Login extends HttpServlet {
    /**
	 * UID
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String nick = req.getParameter("nick");
        String password = Encoder.encode(req.getParameter("password"));
        System.out.println(nick + password);
        BlogUser blogUser = DAOProvider.getDAO().getBlogUserByNick(nick);
        if (blogUser == null || !blogUser.getPasswordHash().equals(password)) {
            resp.sendRedirect(req.getContextPath() + "/servleti/main?login=failed");
        } else {
            req.getSession().setAttribute("principal", blogUser);
            resp.sendRedirect(req.getContextPath() + "/servleti/author/" + nick);
        }
    }
}
