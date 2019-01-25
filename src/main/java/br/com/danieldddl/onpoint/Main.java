package br.com.danieldddl.onpoint;

import br.com.danieldddl.onpoint.config.ConnectionPool;
import br.com.danieldddl.onpoint.dao.api.IMarkDao;
import br.com.danieldddl.onpoint.dao.impl.MarkTypeDaoImpl;
import br.com.danieldddl.onpoint.model.Mark;
import br.com.danieldddl.onpoint.model.MarkType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Objects;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws SQLException {

        MarkTypeDaoImpl markTypeDao = new MarkTypeDaoImpl();

        MarkType markType = new MarkType("Lunch");
        markTypeDao.insert(markType);


        /*

        String insertMarkType = "INSERT INTO mark_type (name) VALUES (?);";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(insertMarkType)) {

            ps.setObject(1, "Saida para almoço");
            ps.executeUpdate();
        }

        Mark mark = new Mark(LocalDateTime.now(), LocalDateTime.now(), getMarkType());

        String insertMark = "INSERT INTO mark (when_happened, marked_date, marked_type_id) VALUES (?,?,?)";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(insertMark)) {

            ps.setObject(1, mark.getWhen());
            ps.setObject(2, mark.getMarkedDate());
            ps.setObject(3, mark.getMarkType().getId());

            ps.executeUpdate();
        }

        Mark fetchMark = null;

        String selectMark = "SELECT id, when_happened, marked_date, marked_type_id FROM mark";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectMark);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                LOGGER.debug("Just printing the id: {}", rs.getLong("marked_type_id"));

                Long id = rs.getLong("id");
                LocalDateTime when = rs.getTimestamp("when_happened").toLocalDateTime();
                LocalDateTime markedDate = rs.getTimestamp("marked_date").toLocalDateTime();

                LOGGER.debug("Getting mark type one more time");
                MarkType markType = getMarkType();

                Mark fetchedMark = new Mark(id, when, markedDate, markType);

                LOGGER.debug(fetchedMark.toString());
            }

        }

        */

    }

    /*
    private static MarkType getMarkType () throws SQLException {

        LOGGER.debug("Getting mark type");

        MarkType mt = null;

        String selectMarkType = "SELECT id, name FROM mark_type;";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectMarkType);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                LOGGER.debug("Found one.");

                Long id = rs.getLong("id");
                String name = rs.getString("name");
                mt = new MarkType(id, name);

                LOGGER.debug(mt.toString());
            }
        }

        return mt;
    }
    */

}
