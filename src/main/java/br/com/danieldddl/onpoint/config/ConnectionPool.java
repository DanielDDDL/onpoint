package br.com.danieldddl.onpoint.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class ConnectionPool {

    private static final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);
    private static DataSource dataSource;

    private ConnectionPool () {
        //preventing class from being instantiated
    }

    static {
        dataSource = setupDatasource();
    }

    public static Connection getConnection () {

        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException("Error while getting connection from datasource: ", e);
        }
    }

    private static DataSource setupDatasource() {

        String url = Settings.getProperty("database.url");
        String username = Settings.getProperty("database.username");
        String password = Settings.getProperty("database.password");

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(Objects.requireNonNull(url));
        ds.setUsername(Objects.requireNonNull(username));
        ds.setPassword(Objects.requireNonNull(password));

        LOGGER.debug("Setting up datasource using url {} and username {}", url, username);

        return ds;
    }
}
