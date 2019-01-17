package br.com.danieldddl.onpoint.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionPool {

    private static final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);

    private static DataSource dataSource;

    private static String url;
    private static String username;
    private static String password;

    static {
        setupProperties();
        dataSource = setupDatasource();
    }

    public static Connection getConnection () {

        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.error("Error while getting connection from datasource", e);
            throw new IllegalStateException(e);
        }
    }

    private static DataSource setupDatasource() {

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);

        return ds;
    }

    /**
     * Reads from config.properties file stored in the resources folder.
     * Sets up the url, username, and password we are going to use to establish
     * a connection with the database.
     * */
    private static void setupProperties () {

        String filename = "config.properties";
        InputStream inputStream = ConnectionPool.class.getClassLoader().getResourceAsStream(filename);

        if (inputStream != null) {

            Properties properties = new Properties();

            try {
                properties.load(inputStream);
            } catch (IOException e) {
                LOGGER.error("Error while trying to load properties file from InputStream", e);
                throw new IllegalStateException("Error while trying to load configuration file from InputStream", e);
            }

            url = properties.getProperty("database.url");
            username = properties.getProperty("database.username");
            password = properties.getProperty("database.password");

            LOGGER.info("Database properties load from {}. " +
                        "Database url: {}; username: {}; password: {}",
                        filename, url, username, password);

        } else {

            LOGGER.error("Configuration file {} was not found.", filename);
            throw new IllegalStateException("Configuration file not found");

        }
    }

}
