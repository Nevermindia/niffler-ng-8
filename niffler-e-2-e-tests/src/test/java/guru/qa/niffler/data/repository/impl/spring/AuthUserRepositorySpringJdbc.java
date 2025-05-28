package guru.qa.niffler.data.repository.impl.spring;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.userAuth.AuthUserEntity;
import guru.qa.niffler.data.entity.userAuth.AuthorityEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class AuthUserRepositorySpringJdbc implements AuthUserRepository {
    private static final Config CFG = Config.getInstance();
    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();

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
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Nonnull
    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return authUserDao.findById(id);
    }

    @Nonnull
    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        return authUserDao.findByUsername(username);
    }

    @Override
    public void remove(AuthUserEntity user) {
        authUserDao.delete(user.getId());
    }
}
