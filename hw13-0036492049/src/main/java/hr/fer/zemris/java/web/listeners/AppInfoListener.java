package hr.fer.zemris.java.web.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * {@link ServletContextListener} that sets servlet attribute 'startTime' to {@link System#currentTimeMillis()} when application is started
 *
 * @author Pavao Jerebić
 */
@WebListener
public class AppInfoListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("startTime", System.currentTimeMillis());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
