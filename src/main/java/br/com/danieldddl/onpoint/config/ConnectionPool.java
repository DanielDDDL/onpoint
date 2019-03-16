package br.com.danieldddl.onpoint.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

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
            LOGGER.error("Error while getting connection from datasource");
            throw new IllegalStateException(e);
        }
    }

    private static DataSource setupDatasource() {

        String url = Settings.getProperty("database.url");
        String username = Settings.getProperty("database.username");
        String password = Settings.getProperty("database.password");

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
}
