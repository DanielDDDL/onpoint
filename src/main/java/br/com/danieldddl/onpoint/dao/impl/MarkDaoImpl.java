package br.com.danieldddl.onpoint.dao.impl;

import br.com.danieldddl.onpoint.config.ConnectionPool;
import br.com.danieldddl.onpoint.dao.api.IMarkDao;
import br.com.danieldddl.onpoint.dao.api.IMarkTypeDao;
import br.com.danieldddl.onpoint.model.Mark;
import br.com.danieldddl.onpoint.model.MarkType;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class MarkDaoImpl implements IMarkDao {

    private static final Logger LOGGER = LogManager.getLogger(MarkDaoImpl.class);

    private static final String INSERT_QUERY = "INSERT INTO mark (when_happened, marked_date, marked_type_id) " +
                                               "VALUES (?,?,?)";

    private static final String SELECT_LIMIT = "SELECT id, when_happened, marked_date, marked_type_id " +
                                               "FROM mark " +
                                               "ORDER BY when_happened DESC " +
                                               "LIMIT ?;";

    private static final String SELECT_SINCE = "SELECT id, when_happened, marked_date, marked_type_id " +
                                               "FROM mark " +
                                               "WHERE when_happened > ?";

    private static final String SELECT_BETWEEN = "SELECT id, when_happened, marked_date, marked_type_id " +
                                                 "FROM mark " +
                                                 "WHERE when_happened BETWEEN ? AND ?";

    private IMarkTypeDao markTypeDao;

    @Inject
    public MarkDaoImpl (IMarkTypeDao markTypeDao) {
        this.markTypeDao = markTypeDao;
    }

    @Override
    public Mark persist(Mark mark) {

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_QUERY,
                                                                Statement.RETURN_GENERATED_KEYS)) {

            //in case we are persisting a mark without a type
            Integer markedTypeId = mark.getMarkType() == null ? null : mark.getMarkType().getId();

            ps.setObject(1, mark.getWhen());
            ps.setObject(2, mark.getMarkedDate());
            ps.setObject(3, markedTypeId);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {

                    //successfully persisted
                    Integer createdId = rs.getInt(1);
                    mark.setId(createdId);

                    return mark;
                }

                throw new IllegalStateException("Error while getting generated id");
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Error while inserting mark without id", e);
        }
    }

    @Override
    public List<Mark> listLastMarks (int amount) {

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_LIMIT)) {

            //number of rows we are going to retrieve
            ps.setObject(1, amount);

            try (ResultSet rs = ps.executeQuery()) {
                return extractListOfMarksFromResultSet(rs);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Problem while trying to retrieve marks using limit clause", e);
        }
    }

    @Override
    public List<Mark> listSince(LocalDateTime sinceWhen) {

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_SINCE)) {

            //the date we are going to use as base
            ps.setObject(1, sinceWhen);

            try (ResultSet rs = ps.executeQuery()) {
                return extractListOfMarksFromResultSet(rs);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Error while retrieving Marks since a specific date", e);
        }
    }

    @Override
    public List<Mark> listBetween (LocalDateTime lowerDate, LocalDateTime upperDate) {

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BETWEEN)) {

            ps.setObject(1, lowerDate);
            ps.setObject(2, upperDate);

            try (ResultSet rs = ps.executeQuery()) {
                return extractListOfMarksFromResultSet(rs);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Error while retrieving Marks between two dates", e);
        }
    }

    /**
     * This methods extract a Mark from the current row of a ResultSet.
     * This does not close the ResultSet passed as parameter.
     * */
    private Mark extractMarkFromResultSet (ResultSet rs) throws SQLException {

        Integer id = rs.getInt("id");
        LocalDateTime when = rs.getTimestamp("when_happened").toLocalDateTime();
        LocalDateTime markedDate = rs.getTimestamp("marked_date").toLocalDateTime();

        //checking if markTypeId is null and setting markType attribute properly
        Integer markedTypeId = rs.getInt("marked_type_id");
        MarkType markType = rs.wasNull() ? null : markTypeDao.findMarkTypeById(markedTypeId);

        return new Mark(id, when, markedDate, markType);
    }

    /**
     * This methods assumes that we have a clean ResultSet. We are
     * going to extract all Marks from ResultSet passed as parameter.
     * This does not close such ResultSet.
     * */
    private List<Mark> extractListOfMarksFromResultSet (ResultSet rs) throws SQLException {

        List<Mark> marks = new ArrayList<>();

        while (rs.next()) {
            Mark extractMark = extractMarkFromResultSet(rs);
            marks.add(extractMark);
        }

        return marks;
    }

}
