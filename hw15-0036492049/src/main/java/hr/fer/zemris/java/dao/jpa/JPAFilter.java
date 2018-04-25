package hr.fer.zemris.java.dao.jpa;

import hr.fer.zemris.java.dao.DAOException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Filter that closes {@link JPAEMFProvider} when request is processed
 *
 * @author Pavao Jerebić
 */
@WebFilter("/servleti/*")
public class JPAFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            try {
                JPAEMProvider.close();
            } catch (DAOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void destroy() {
    }

}
