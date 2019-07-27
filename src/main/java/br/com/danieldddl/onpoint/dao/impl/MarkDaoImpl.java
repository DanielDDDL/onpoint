package br.com.danieldddl.onpoint.dao.impl;

import br.com.danieldddl.onpoint.config.ConnectionPool;
import br.com.danieldddl.onpoint.config.QueryLoader;
import br.com.danieldddl.onpoint.dao.api.MarkDao;
import br.com.danieldddl.onpoint.dao.api.TypeDao;
import br.com.danieldddl.onpoint.model.Mark;
import br.com.danieldddl.onpoint.model.Type;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.*;

public class MarkDaoImpl implements MarkDao {

    private TypeDao typeDao;

    private String insertQuery;
    private String selectLimit;
    private String selectSince;
    private String selectBetween;

    @Inject
    public MarkDaoImpl (TypeDao typeDao) {

        this.typeDao = typeDao;

        this.insertQuery = QueryLoader.get("INSERT_MARK");
        this.selectLimit = QueryLoader.get("SELECT_MARK_WITH_LIMIT");
        this.selectSince = QueryLoader.get("SELECT_MARK_SINCE");
        this.selectBetween = QueryLoader.get("SELECT_MARK_BETWEEN");
    }

    @Override
    public Mark persist(@NotNull Mark mark) {

        requireNonNull(mark);

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(insertQuery,
                                                                Statement.RETURN_GENERATED_KEYS)) {

            //in case we are persisting a mark without a type
            Integer markedTypeId = mark.getType() == null ? null : mark.getType().getId();

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
    public List<Mark> listLast(int amount) {

        if (amount < 0) {
            throw new IllegalArgumentException("When listing last marks, the amount should be positive");
        }

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectLimit)) {

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
    public List<Mark> listSince(@NotNull LocalDateTime sinceWhen) {

        requireNonNull(sinceWhen);

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectSince)) {

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
    public List<Mark> listBetween (@NotNull LocalDateTime lowerDate, @NotNull LocalDateTime upperDate) {

        requireNonNull(lowerDate);
        requireNonNull(upperDate);
        assert lowerDate.isBefore(upperDate);

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectBetween)) {

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

        //checking if markTypeId is null and setting type attribute properly
        Integer markedTypeId = rs.getInt("marked_type_id");
        Type type = rs.wasNull() ? null : typeDao.find(markedTypeId);

        return new Mark(id, when, markedDate, type);
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
