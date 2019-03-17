package br.com.danieldddl.onpoint.config;

import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Responsible for reaching out to the project files
 * and get the database queries
 * */
public class QueryLoader {

    private static Map<String,String> queries;

    static {
        queries = setupQueries();
    }

    private static Map<String,String> setupQueries () {

        String filename = Settings.getProperty("queries.filename");
        InputStream inputStream = QueryLoader.class.getClassLoader().getResourceAsStream(filename);

        if (inputStream != null) {

            Properties properties = new Properties();

            try {
                properties.loadFromXML(inputStream);
            } catch (IOException e) {
                throw new IllegalStateException("Error while trying to load configuration file from InputStream", e);
            }

            Map<String,String> loadedQueries = new HashMap<>();

            properties.forEach((propertyKey, propertyValue) -> {

                String key = (String) propertyKey;
                String value = (String) propertyValue;

                loadedQueries.put(key, value);
            });

            return loadedQueries;
        }

        throw new IllegalStateException("Queries file not found at " + filename);
    }


    public static String get (@NotNull String queryName) {

        Objects.requireNonNull(queryName);

        Optional<String> queryFetched = Optional.ofNullable(queries.get(queryName));
        return queryFetched
                .orElseThrow(
                        () -> new IllegalArgumentException("Query not defined for name " + queryName));
    }

}
