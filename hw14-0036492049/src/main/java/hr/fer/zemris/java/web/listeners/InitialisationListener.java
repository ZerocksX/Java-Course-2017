package hr.fer.zemris.java.web.listeners;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import hr.fer.zemris.java.web.dao.DAOProvider;
import hr.fer.zemris.java.web.dao.sql.SQLConnectionProvider;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Listener that initializes database pool connections as servlet context attribute 'hr.fer.zemris.dbpool'<br/>
 * Also initializes database if it is not yet initialized, file used for initialization should be in<br/>
 * "/WEB-INF/polls-definitions.txt"<br/>
 *
 * @author Pavao Jerebić
 * @see hr.fer.zemris.java.web.dao.DAO#initializeDatabase(Path)
 */
@WebListener
public class InitialisationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        Properties properties = new Properties();
        try {
            properties.load(Files.newInputStream(Paths.get(sce.getServletContext().getRealPath("/WEB-INF/dbsettings.properties"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String dbName = properties.getProperty("name");
        String connectionURL =
                "jdbc:derby://"
                        + properties.getProperty("host") + ":" + properties.getProperty("port") + "/"
                        + dbName
                        + ";user=" + properties.getProperty("user") + ";password=" + properties.getProperty("password");

        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
        } catch (PropertyVetoException e1) {
            throw new RuntimeException("Failed to initialize pool.", e1);
        }
        cpds.setJdbcUrl(connectionURL);

        try (Connection con = cpds.getConnection()) {
            SQLConnectionProvider.setConnection(con);
            DAOProvider.getDao().
                    initializeDatabase(Paths.get(sce.getServletContext().getRealPath("/WEB-INF/polls-definitions.txt")));
            SQLConnectionProvider.setConnection(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ComboPooledDataSource cpds = (ComboPooledDataSource) sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
        if (cpds != null) {
            try {
                DataSources.destroy(cpds);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
