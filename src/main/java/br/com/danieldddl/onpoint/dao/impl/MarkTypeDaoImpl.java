package br.com.danieldddl.onpoint.dao.impl;

import br.com.danieldddl.onpoint.config.ConnectionPool;
import br.com.danieldddl.onpoint.dao.api.IMarkTypeDao;
import br.com.danieldddl.onpoint.model.MarkType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Objects;

public class MarkTypeDaoImpl implements IMarkTypeDao {

    private static final Logger LOGGER = LogManager.getLogger(MarkTypeDaoImpl.class);

    private static final String INSERT_QUERY =  "INSERT INTO mark_type (name) " +
                                                "VALUES (?)";

    private static final String SELECT_EXISTS = "SELECT count(*) " +
                                                "AS total " +
                                                "FROM mark_type " +
                                                "WHERE name = ?";

    private static final String SELECT_VALUES = "SELECT id, name " +
                                                "FROM mark_type " +
                                                "WHERE name = ?";

    private static final String SELECT_BY_ID  = "SELECT id, name " +
                                                "FROM mark_type " +
                                                "WHERE id = ?";



    @Override
    public MarkType getAndSaveMarkTypeByName(String name) {

        MarkType markType = findMarkTypeByName(name);
        if (markType == null) {
            markType = insert(new MarkType(name));
        }

        return markType;
    }

    @Override
    public MarkType findMarkTypeByName (String name) {

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_VALUES)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                return extractMarkTypeFromResultSet(rs);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Error while retrieving MarkType by its name", e);
        }

    }

    @Override
    public MarkType findMarkTypeById (Integer id) {

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)) {

            ps.setObject(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return extractMarkTypeFromResultSet(rs);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Error while retrieving MarkType by its name", e);
        }

    }

    @Override
    public MarkType insert (MarkType markType) {

        Objects.requireNonNull(markType);
        Objects.requireNonNull(markType.getName(), "Trying to insert a MarkType without a name");

        if (existsMarkTypeWithName(markType.getName())) {
            throw new IllegalStateException("Trying to insert a MarkType already persisted");
        }

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, markType.getName());
            ps.executeUpdate();

            try (ResultSet resultingKeys = ps.getGeneratedKeys()) {

                if (resultingKeys.next()) {
                    Integer id = resultingKeys.getInt(1);
                    markType.setId(id);

                    return markType;
                } else {
                    //this means that nothing was inserted
                    throw new IllegalStateException("Error while getting generated key from persisted new MarkType");
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Error while persisting new MarkType", e);
        }
    }

    @Override
    public boolean existsMarkTypeWithName(String name) {

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_EXISTS)) {

            ps.setString(1, name);

            try (ResultSet totalRs = ps.executeQuery()) {
                if (totalRs.next()) {
                    Integer total = totalRs.getInt("total");
                    return total > 0;
                }

                throw new IllegalStateException("No result for count select");
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Error while checking if MarkType is unique", e);
        }
    }

    /**
     * This method assumes that we have a "clean" ResultSet,
     * which means that we are going to get the first result
     * of the current rs, if there is a next row.
     *
     * This does not close the ResultSet passed as parameter.
     * */
    private MarkType extractMarkTypeFromResultSet (ResultSet rs) throws SQLException {


        if (rs.next()) {
            Integer fetchedId = rs.getInt("id");
            String fetchedName = rs.getString("name");

            return new MarkType(fetchedId, fetchedName);
        }

        return null;

    }

}
