package br.com.danieldddl.onpoint.dao.impl;

import br.com.danieldddl.onpoint.config.ConnectionPool;
import br.com.danieldddl.onpoint.dao.api.IMarkDao;
import br.com.danieldddl.onpoint.model.Mark;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;


public class MarkDaoImpl implements IMarkDao {

    private static final Logger LOGGER = LogManager.getLogger(MarkDaoImpl.class);

    private static final String INSERT_QUERY = "INSERT INTO mark (when_happened, marked_date, marked_type_id) " +
                                               "VALUES (?,?,?)";

    @Override
    public Mark persist (Mark mark) {

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

}
