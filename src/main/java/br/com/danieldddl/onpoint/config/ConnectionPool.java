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

import static java.sql.Connection.TRANSACTION_READ_COMMITTED;

public class ConnectionPool {

    private static final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);

    private static DataSource dataSource;

    private static String url;
    private static String username;
    private static String password;

    private ConnectionPool () {
        //preventing class from being instantiated
    }

    static {
        setupProperties();
        dataSource = setupDatasource();
    }

    public static Connection getConnection () {

        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.error("Error while getting connection from datasource");
            throw new IllegalStateException(e);
        }
    }

    private static DataSource setupDatasource() {

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);

        //TODO read about the different types of transaction isolation levels
        //TODO and set the most appropriated value in here
//        ds.setDefaultTransactionIsolation(TRANSACTION_READ_COMMITTED);
//        ds.setDefaultAutoCommit(false);

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
                throw new IllegalStateException("Error while trying to load configuration file from InputStream", e);
            }

            url = properties.getProperty("database.url");
            username = properties.getProperty("database.username");
            password = properties.getProperty("database.password");

            LOGGER.info("Database properties load from {}. " +
                        "Database url: {}; username: {}; password: {}",
                        filename, url, username, password);

        } else {
            throw new IllegalStateException("Configuration file  " + filename + " was not found");
        }
    }

}
