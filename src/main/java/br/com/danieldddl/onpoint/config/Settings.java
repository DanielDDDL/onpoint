package br.com.danieldddl.onpoint.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Responsible for loading configuration file
 * and providing an interface for its values
 * */
class Settings {

    private static Map fileProperties;

    static {
        fileProperties = setupProperties();
    }

    static String getProperty (String propertyName) {
        //we can make this casting
        //as we are only retrieving strings from the properties file
        return (String) fileProperties.get(propertyName);
    }

    /**
     * Reads from config.properties file stored in the resources folder.
     * Sets up the url, username, and password we are going to use to establish
     * a connection with the database.
     * */
    private static Map setupProperties () {

        String filename = "config.properties";
        InputStream inputStream = ConnectionPool.class.getClassLoader().getResourceAsStream(filename);

        if (inputStream != null) {

            Properties properties = new Properties();

            try {
                properties.load(inputStream);
            } catch (IOException e) {
                throw new IllegalStateException("Error while trying to load configuration file from InputStream", e);
            }

            return new HashMap<>(properties);

        } else {
            throw new IllegalStateException("Configuration file  " + filename + " was not found");
        }
    }

}
