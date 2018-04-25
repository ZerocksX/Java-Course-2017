package hr.fer.zemris.java.web.servlets;

import hr.fer.zemris.java.dao.DAOException;
import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.model.BlogComment;
import hr.fer.zemris.java.model.BlogEntry;
import hr.fer.zemris.java.model.BlogUser;
import hr.fer.zemris.java.web.util.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Servlet that adds a comment with given message to blog entry with id given in path<br/>
 * Processes post requests on on '/servleti/addComment'<br/>
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "addComment", value = "/servleti/addComment")
public class AddComment extends HttpServlet {
    /**
	 * UID
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlogUser principal = (BlogUser) req.getSession().getAttribute("principal");
        String message = req.getParameter("message");
        Long id;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (Exception e) {
            Utils.forwardToErrorPage(req, resp, "Invalid blog entry id");
            return;
        }
        BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(id);
        if (blogEntry == null) {
            Utils.forwardToErrorPage(req, resp, "Blog with given id, " + id + ", does not exist");
            return;
        }
        message = message.trim();
        if (message.trim().isEmpty()) {
            Utils.forwardToErrorPage(req, resp, "Message text empty");
            return;
        }


        BlogComment comment = new BlogComment();
        comment.setUsersEMail(principal == null ? "Anonymous" : principal.getEmail());
        comment.setPostedOn(new Date());
        comment.setBlogEntry(blogEntry);
        comment.setMessage(message);
        try {
            DAOProvider.getDAO().persistBlogComment(comment);
        } catch (DAOException e) {
            Utils.forwardToErrorPage(req, resp, e.getMessage());
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/servleti/author/" + blogEntry.getCreator().getNick() + "/" + id);
    }
}
