package br.com.danieldddl.onpoint.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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

                //TODO evaluate if we are going to need to threat the output of this file first
                //getting rid of the new line characters
//                value = value.replaceAll("\n","");

                loadedQueries.put(key, value);
            });

            return loadedQueries;
        }

        throw new IllegalStateException("Queries file not found at " + filename);
    }


    public static String get (String queryName) {

        String queryFetched = queries.get(queryName);
        if (queryFetched == null){
            throw new IllegalArgumentException("Query not defined for name " + queryName);
        }

        return queryFetched;
    }

}
