package br.com.danieldddl.onpoint;

import br.com.danieldddl.onpoint.config.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws SQLException {

        String insert = "INSERT INTO books (title, author) VALUES (?,?)";
        for (int i = 0; i < 10; i++) {
            try (Connection connection = ConnectionPool.getConnection();
                 PreparedStatement ps = connection.prepareStatement(insert)) {

                ps.clearParameters();

                ps.setObject(1,"Livro x" + i);
                ps.setObject(2, "Autor qualquer" + i);

                ps.executeUpdate();

            }
        }

        String select = "SELECT id, title, author FROM books";
        try (Connection connection = ConnectionPool.getConnection();
             Statement stm = connection.createStatement();
             ResultSet rs = stm.executeQuery(select)) {

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");

                LOGGER.debug("Fetched book: {}:{} - {}", id, title, author);
            }

        }

    }

}
