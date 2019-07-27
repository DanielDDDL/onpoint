package br.com.danieldddl.onpoint.dao.impl;

import br.com.danieldddl.onpoint.config.ConnectionPool;
import br.com.danieldddl.onpoint.config.QueryLoader;
import br.com.danieldddl.onpoint.dao.api.TypeDao;
import br.com.danieldddl.onpoint.model.Type;

import javax.validation.constraints.NotNull;
import java.sql.*;

import static java.util.Objects.*;

public class TypeDaoImpl implements TypeDao {

    private String insertQuery;
    private String selectExists;
    private String selectValues;
    private String selectById;

    public TypeDaoImpl() {
        this.insertQuery = QueryLoader.get("INSERT_MARK_TYPE");
        this.selectExists = QueryLoader.get("SELECT_EXISTS_MARK_TYPE");
        this.selectValues = QueryLoader.get("SELECT_MARK_TYPE_BY_NAME");
        this.selectById = QueryLoader.get("SELECT_MARK_TYPE_BY_ID");
    }

    @Override
    public Type getOrElsePersistWithName(@NotNull String name) {

        requireNonNull(name);

        Type type = find(name);
        if (type == null) {
            type = insert(new Type(name));
        }

        return type;
    }

    @Override
    public Type find(@NotNull String name) {

        requireNonNull(name);

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectValues)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                return extractMarkTypeFromResultSet(rs);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Error while retrieving Type by its name", e);
        }

    }

    @Override
    public Type find(@NotNull Integer id) {

        requireNonNull(id);

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectById)) {

            ps.setObject(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return extractMarkTypeFromResultSet(rs);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Error while retrieving Type by its name", e);
        }

    }

    @Override
    public Type insert (@NotNull Type type) {

        requireNonNull(type);
        requireNonNull(type.getName(), "Trying to insert a Type without a name");

        if (exists(type.getName())) {
            throw new IllegalStateException("Trying to insert a Type already persisted");
        }

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, type.getName());
            ps.executeUpdate();

            try (ResultSet resultingKeys = ps.getGeneratedKeys()) {

                if (resultingKeys.next()) {
                    Integer id = resultingKeys.getInt(1);
                    type.setId(id);

                    return type;
                } else {
                    //this means that nothing was inserted
                    throw new IllegalStateException("Error while getting generated key from persisted new Type");
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Error while persisting new Type", e);
        }
    }

    @Override
    public boolean exists(@NotNull String name) {

        requireNonNull(name);

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
            throw new IllegalStateException("Error while checking if Type is unique", e);
        }
    }

    /**
     * This method assumes that we have a "clean" ResultSet,
     * which means that we are going to get the first result
     * of the current rs, if there is a next row.
     *
     * This does not close the ResultSet passed as parameter.
     * */
    private Type extractMarkTypeFromResultSet (ResultSet rs) throws SQLException {


        if (rs.next()) {
            Integer fetchedId = rs.getInt("id");
            String fetchedName = rs.getString("name");

            return new Type(fetchedId, fetchedName);
        }

        return null;

    }

}
