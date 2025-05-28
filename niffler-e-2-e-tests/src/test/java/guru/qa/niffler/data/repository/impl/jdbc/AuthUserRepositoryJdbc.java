package guru.qa.niffler.data.repository.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.userAuth.AuthUserEntity;
import guru.qa.niffler.data.entity.userAuth.AuthorityEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

@ParametersAreNonnullByDefault
public class AuthUserRepositoryJdbc implements AuthUserRepository {
    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc();


    @Nonnull
    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        authUserDao.createUser(user);
        for (AuthorityEntity authority : user.getAuthorities()) {
            authAuthorityDao.create(authority);
        }
        return user;
    }

    @Nonnull
    @Override
    public AuthUserEntity update(AuthUserEntity user) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                """
                        UPDATE \"user\" 
                        SET 
                        username = ?,
                        password = ?,
                        enabled = ?,
                        account_non_expired = ?,
                        account_non_locked = ?,
                        credentials_non_expired = ?
                        WHERE id = ?
                        """
        )) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.getEnabled());
            ps.setBoolean(4, user.getAccountNonExpired());
            ps.setBoolean(5, user.getAccountNonLocked());
            ps.setBoolean(6, user.getCredentialsNonExpired());
            ps.setObject(7, user.getId());
            ps.executeUpdate();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Nonnull
    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return authUserDao.findById(id);
    }

    @Nonnull
    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        return findByUsername(username);
    }

    @Override
    public void remove(AuthUserEntity user) {
        authUserDao.delete(user.getId());
    }
}
