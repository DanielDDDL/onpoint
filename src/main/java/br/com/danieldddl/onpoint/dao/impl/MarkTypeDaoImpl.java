package br.com.danieldddl.onpoint.dao.impl;

import br.com.danieldddl.onpoint.config.ConnectionPool;
import br.com.danieldddl.onpoint.config.QueryLoader;
import br.com.danieldddl.onpoint.dao.api.IMarkTypeDao;
import br.com.danieldddl.onpoint.model.MarkType;

import javax.validation.constraints.NotNull;
import java.sql.*;
import java.util.Objects;

public class MarkTypeDaoImpl implements IMarkTypeDao {

    private String insertQuery;
    private String selectExists;
    private String selectValues;
    private String selectById;

    public MarkTypeDaoImpl () {
        this.insertQuery = QueryLoader.get("INSERT_MARK_TYPE");
        this.selectExists = QueryLoader.get("SELECT_EXISTS_MARK_TYPE");
        this.selectValues = QueryLoader.get("SELECT_EXISTS_MARK_TYPE");
        this.selectById = QueryLoader.get("SELECT_MARK_TYPE_BY_ID");
    }

    @Override
    public MarkType getAndSaveMarkTypeByName(@NotNull String name) {

        Objects.requireNonNull(name);

        MarkType markType = findMarkTypeByName(name);
        if (markType == null) {
            markType = insert(new MarkType(name));
        }

        return markType;
    }

    @Override
    public MarkType findMarkTypeByName (@NotNull String name) {

        Objects.requireNonNull(name);

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectValues)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                return extractMarkTypeFromResultSet(rs);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Error while retrieving MarkType by its name", e);
        }

    }

    @Override
    public MarkType findMarkTypeById (@NotNull Integer id) {

        Objects.requireNonNull(id);

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectById)) {

            ps.setObject(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return extractMarkTypeFromResultSet(rs);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Error while retrieving MarkType by its name", e);
        }

    }

    @Override
    public MarkType insert (@NotNull MarkType markType) {

        Objects.requireNonNull(markType);
        Objects.requireNonNull(markType.getName(), "Trying to insert a MarkType without a name");

        if (existsMarkTypeWithName(markType.getName())) {
            throw new IllegalStateException("Trying to insert a MarkType already persisted");
        }

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

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
    public boolean existsMarkTypeWithName(@NotNull String name) {

        Objects.requireNonNull(name);

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectExists)) {

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
