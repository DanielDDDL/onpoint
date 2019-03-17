package br.com.danieldddl.onpoint.config;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Responsible for loading configuration file
 * and providing an interface for its values
 * */
class Settings {

    private static Map fileProperties;

    static {
        fileProperties = setupProperties();
    }

    static String getProperty (@NotNull String propertyName) {

        Objects.requireNonNull(propertyName);

        //we can make this casting
        //as we are only retrieving strings from the properties file
        Optional<String> wantedProperty = Optional.ofNullable((String) fileProperties.get(propertyName));
        return wantedProperty
                .orElseThrow(
                        () -> new IllegalArgumentException("Could not find wanted property: " + propertyName));
    }

    /**
     * Reads from config.properties file stored in the resources folder.
     * Sets up the url, username, and password we are going to use to establish
     * a connection with the database.
     * */
    private static Map setupProperties () {

        String filename = "config.properties";
        InputStream inputStream = Settings.class.getClassLoader().getResourceAsStream(filename);

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
