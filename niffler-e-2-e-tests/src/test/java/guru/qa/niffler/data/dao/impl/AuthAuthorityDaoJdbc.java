package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.userAuth.AuthorityEntity;

import java.sql.*;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {
    private final Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createUser(AuthorityEntity... authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"authority\" (user_id, authority) " +
                "VALUES (?, ? )",
                Statement.RETURN_GENERATED_KEYS
        )) {
            for (AuthorityEntity authorityEntity : authority) {
                ps.setObject(1, authorityEntity.getUserId());
                ps.setString(2, authorityEntity.getAuthority().name());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
