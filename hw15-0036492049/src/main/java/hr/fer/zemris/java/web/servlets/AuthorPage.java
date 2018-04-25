package hr.fer.zemris.java.web.servlets;

import hr.fer.zemris.java.dao.DAOException;
import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.model.BlogEntry;
import hr.fer.zemris.java.model.BlogUser;
import hr.fer.zemris.java.web.util.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/**
 * Servlet that processes all author features<br/>
 * If accessed with get on /servleti/$AUTHOR$ user will be given all blog entries of $AUTHOR$<br/>
 * If currently logged in user (principal) is the same as $AUTHOR$ then the user will be given an option to create a new blog entry<br/>
 * If accessed with get on '/servleti/$AUTHOR$/$id$ user will be given a blog entry with the given $id$<br/>
 * If accessed with get on '/servleti/$AUTHOR$/new' If currently logged in user (principal) is the same as $AUTHOR$ then the user will be given a form to create a new blog entry<br/>
 * If accessed with get on '/servleti/$AUTHOR$/edit?id=$id$' If currently logged in user (principal) is the same as $AUTHOR$ then the user will be given a form to edit a  blog entry with given $id$<br/>
 * If accessed with post on '/servleti/$AUTHOR$/save?id=$id$' If currently logged in user (principal)<br/>
 * is the same as $AUTHOR$ then given title and message will be saved to a new entry if the $id$ does not exist, or to blog entry with given $id$<br/>
 *
 * @author Pavao Jerebić
 */
@WebServlet(name = "authorPage", urlPatterns = "/servleti/author/*")
public class AuthorPage extends HttpServlet {
    /**
	 * UID
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlogUser principal = (BlogUser) req.getSession().getAttribute("principal");

        String data[] = req.getPathInfo().split("[/]");
        if (data.length != 2 && data.length != 3) {
            Utils.forwardToErrorPage(req, resp, "Invalid path");
            return;
        }
        data = Arrays.copyOfRange(data, 1, data.length);

        String nick = data[0];
        BlogUser blogUser = DAOProvider.getDAO().getBlogUserByNick(nick);
        if (blogUser == null) {
            Utils.forwardToErrorPage(req, resp, "User does not exist");
            return;
        }
        req.setAttribute("user", blogUser);
        if (blogUser.equals(principal)) {
            req.setAttribute("editAllowed", true);
        }
        if (data.length == 1) {
            req.getRequestDispatcher("/WEB-INF/pages/author.jsp").forward(req, resp);
        } else if (data.length == 2) {


            String info = data[1];
            Long id = null;
            try {
                id = Long.parseLong(info);
            } catch (Exception ignored) {
            }
            if (id != null) {
                BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(id);
                if (blogEntry != null) {
                    req.setAttribute("blogEntry", blogEntry);
                }
                req.getRequestDispatcher("/WEB-INF/pages/displayBlog.jsp").forward(req, resp);
                return;
            }


            if (!blogUser.equals(principal)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            switch (info) {

                case "edit":
                    String idParameter = req.getParameter("id");
                    if (idParameter == null) {
                        Utils.forwardToErrorPage(req, resp, "Id is nod given");
                        return;
                    }
                    try {
                        id = Long.parseLong(idParameter);
                    } catch (Exception e) {
                        Utils.forwardToErrorPage(req, resp, "Given id is invalid");
                        return;
                    }
                    BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(id);
                    req.setAttribute("blogEntry", blogEntry);
                    req.getRequestDispatcher("/WEB-INF/pages/editBlog.jsp").forward(req, resp);
                    return;
                case "new":
                    req.getRequestDispatcher("/WEB-INF/pages/editBlog.jsp").forward(req, resp);
                    return;
                default:
                    Utils.forwardToErrorPage(req, resp, "Given operation is invalid");
            }

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String data[] = req.getPathInfo().split("[/]");
        if (data.length != 3) {
            Utils.forwardToErrorPage(req, resp, "Invalid path");
            return;
        }
        data = Arrays.copyOfRange(data, 1, data.length);

        String nick = data[0];
        BlogUser blogUser = DAOProvider.getDAO().getBlogUserByNick(nick);
        if (blogUser == null) {
            Utils.forwardToErrorPage(req, resp, "User does not exist");
            return;
        }

        BlogUser principal = (BlogUser) req.getSession().getAttribute("principal");
        if (!blogUser.equals(principal)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        req.setAttribute("user", blogUser);

        String info = data[1];

        if (info.equals("save")) {
            String idParameter = req.getParameter("id");
            if (idParameter == null) {
                Utils.forwardToErrorPage(req, resp, "Id is nod given");
                return;
            }


            BlogEntry blogEntry = null;

            if (!idParameter.isEmpty()) {
                Long id;
                try {
                    id = Long.parseLong(idParameter);
                } catch (Exception e) {
                    Utils.forwardToErrorPage(req, resp, "Given id is invalid");
                    return;
                }
                blogEntry = DAOProvider.getDAO().getBlogEntry(id);
            }

            String title = req.getParameter("title");
            String text = req.getParameter("text");
            if (title.trim().isEmpty() || text.trim().isEmpty()) {
                Utils.forwardToErrorPage(req, resp, "Given title and/or text is empty");
                return;
            }

            if (blogEntry == null) {
                blogEntry = new BlogEntry();
                blogEntry.setCreator(blogUser);
                blogEntry.setCreatedAt(new Date());
                blogEntry.setLastModifiedAt(new Date());
                blogEntry.setTitle(title);
                blogEntry.setText(text);
                try {
                    DAOProvider.getDAO().persistBlogEntry(blogEntry);
                } catch (DAOException e) {
                    Utils.forwardToErrorPage(req, resp, e.getMessage());
                }
            } else {
                blogEntry.setTitle(title);
                blogEntry.setText(text);
                blogEntry.setLastModifiedAt(new Date());
            }

            req.setAttribute("blogEntry", blogEntry);
            resp.sendRedirect(req.getContextPath() + "/servleti/author/" + blogUser.getNick() + "/" + blogEntry.getId());
        } else {

            Utils.forwardToErrorPage(req, resp, "Given operation is invalid");
        }

    }
}
