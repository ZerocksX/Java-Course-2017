package hr.fer.zemris.java.web.filters;

import hr.fer.zemris.java.web.dao.sql.SQLConnectionProvider;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Filter that sets up connection when user accesses servlets on "/servleti/*"
 *
 * @author Pavao Jerebić
 */
@WebFilter(urlPatterns = "/servleti/*")
public class ConnectionSetterFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        DataSource ds = (DataSource) request.getServletContext().getAttribute("hr.fer.zemris.dbpool");
        Connection con = null;
        try {
            con = ds.getConnection();
        } catch (SQLException e) {
            throw new IOException("Database is not available", e);
        }
        SQLConnectionProvider.setConnection(con);
        try {
            chain.doFilter(request, response);
        } finally {
            SQLConnectionProvider.setConnection(null);
            try {
                con.close();
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public void destroy() {

    }
}
